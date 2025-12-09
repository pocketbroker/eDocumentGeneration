# eDocumentGeneration - Usage Guide

This guide provides detailed examples and best practices for using the eDocumentGeneration library.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Configuration](#configuration)
3. [Creating Documents](#creating-documents)
4. [Sending for Signature](#sending-for-signature)
5. [Advanced Features](#advanced-features)
6. [Best Practices](#best-practices)
7. [Troubleshooting](#troubleshooting)

## Getting Started

### 1. Set Up Your DocuSign Developer Account

Before using this library, you need a DocuSign developer account:

1. Sign up at https://developers.docusign.com/
2. Create an Integration Key
3. Generate an RSA key pair
4. Grant consent to your application
5. Note your User ID and Account ID

Detailed setup instructions are in the main README.md file.

### 2. Add the Library to Your Project

Maven:
```xml
<dependency>
    <groupId>com.pocketbroker</groupId>
    <artifactId>edocument-generation</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Configuration

### Method 1: Programmatic Configuration

```java
import com.pocketbroker.edocument.config.DocuSignConfig;

DocuSignConfig config = new DocuSignConfig();
config.setIntegrationKey("your-integration-key");
config.setUserId("your-user-id");
config.setAccountId("your-account-id");
config.setPrivateKeyPath("/path/to/private.key");
```

### Method 2: Properties File Configuration

Create a `config.properties` file:

```properties
docusign.integrationKey=your-integration-key
docusign.userId=your-user-id
docusign.accountId=your-account-id
docusign.privateKeyPath=/path/to/private.key
docusign.basePath=https://demo.docusign.net/restapi
docusign.oAuthBasePath=account-d.docusign.com
```

Load it in your code:

```java
import com.pocketbroker.edocument.util.ConfigLoader;
import com.pocketbroker.edocument.config.DocuSignConfig;

DocuSignConfig config = ConfigLoader.loadFromPropertiesFile("config.properties");
```

### Environment-Specific Configuration

For production environments, update the base paths:

```java
config.setBasePath("https://www.docusign.net/restapi");
config.setOAuthBasePath("account.docusign.com");
```

## Creating Documents

### From a File

```java
import com.pocketbroker.edocument.service.DocumentGenerator;
import com.pocketbroker.edocument.model.Document;

DocumentGenerator generator = new DocumentGenerator();
Document document = generator.createDocumentFromFile(
    "/path/to/contract.pdf",
    "Contract Agreement"
);
```

### From Byte Array

```java
byte[] pdfContent = // ... your PDF content
Document document = generator.createDocumentFromBytes(
    pdfContent,
    "Generated Contract",
    "pdf"
);
```

### Document Formats

The library supports various document formats:
- PDF (`.pdf`)
- Word Documents (`.docx`, `.doc`)
- Plain Text (`.txt`)
- HTML (`.html`)

### Preparing Documents for Signing

To enable automatic signature field placement, add anchor strings to your documents:

- `/sn1/` - Primary signature location
- `**signature_1**` - Alternative signature location

Example in a Word document:
```
Signature: /sn1/
Date: __________
```

## Sending for Signature

### Email-Based Signing (Recommended for Most Cases)

Recipients receive an email with a link to sign the document:

```java
import com.pocketbroker.edocument.service.DocuSignIntegration;
import com.pocketbroker.edocument.model.EnvelopeConfig;
import com.pocketbroker.edocument.model.Signer;

// Create signers
Signer signer = generator.createSigner(
    "john.doe@example.com",
    "John Doe"
);

// Create envelope
EnvelopeConfig envelope = generator.createEnvelopeConfig(
    "Please sign this contract"
);
envelope.setEmailBody("This contract requires your signature. Please review and sign.");
envelope.addDocument(document);
envelope.addSigner(signer);

// Send for signature
DocuSignIntegration docusign = new DocuSignIntegration(config);
docusign.authenticate();
String envelopeId = docusign.sendEnvelopeForSignature(envelope);

System.out.println("Envelope sent! ID: " + envelopeId);
```

### Embedded Signing (For In-App Signing)

Users sign documents within your application:

```java
// Create embedded signer (requires clientUserId)
Signer embeddedSigner = generator.createEmbeddedSigner(
    "john.doe@example.com",
    "John Doe",
    "user-123" // Your internal user ID
);

// Create envelope
EnvelopeConfig envelope = generator.createEnvelopeConfig(
    "Sign this document"
);
envelope.addDocument(document);
envelope.addSigner(embeddedSigner);

// Get signing URL
DocuSignIntegration docusign = new DocuSignIntegration(config);
docusign.authenticate();
String signingUrl = docusign.createEmbeddedSigningUrl(
    envelope,
    "https://yourapp.com/signing-complete"
);

// Redirect user to signingUrl
System.out.println("Signing URL: " + signingUrl);
```

### Multiple Signers with Routing Order

Control the order in which signers receive documents:

```java
Signer firstSigner = generator.createSigner("first@example.com", "First Signer");
firstSigner.setRoutingOrder(1);

Signer secondSigner = generator.createSigner("second@example.com", "Second Signer");
secondSigner.setRoutingOrder(2);

Signer thirdSigner = generator.createSigner("third@example.com", "Third Signer");
thirdSigner.setRoutingOrder(3);

envelope.addSigner(firstSigner);
envelope.addSigner(secondSigner);
envelope.addSigner(thirdSigner);

// Documents will be sent in order: first -> second -> third
```

### Parallel Signing

Multiple signers can sign simultaneously:

```java
Signer signer1 = generator.createSigner("signer1@example.com", "Signer One");
signer1.setRoutingOrder(1);

Signer signer2 = generator.createSigner("signer2@example.com", "Signer Two");
signer2.setRoutingOrder(1); // Same routing order = parallel

envelope.addSigner(signer1);
envelope.addSigner(signer2);
```

## Advanced Features

### Multiple Documents in One Envelope

```java
Document contract = generator.createDocumentFromFile(
    "/path/to/contract.pdf", 
    "Main Contract"
);
Document appendix = generator.createDocumentFromFile(
    "/path/to/appendix.pdf",
    "Appendix A"
);

EnvelopeConfig envelope = generator.createEnvelopeConfig(
    "Contract Package - Please Sign"
);
envelope.addDocument(contract);
envelope.addDocument(appendix);
envelope.addSigner(signer);
```

### Checking Envelope Status

```java
String envelopeId = docusign.sendEnvelopeForSignature(envelope);

// Check status later
String status = docusign.getEnvelopeStatus(envelopeId);
System.out.println("Status: " + status);

// Possible statuses:
// - "sent" - Envelope has been sent
// - "delivered" - Envelope has been delivered
// - "completed" - All signers have signed
// - "declined" - A signer declined to sign
// - "voided" - Envelope has been voided
```

### Creating Draft Envelopes

Create an envelope without sending it immediately:

```java
EnvelopeConfig envelope = generator.createEnvelopeConfig("Draft Contract");
envelope.setStatus("created"); // Set to "created" instead of "sent"
envelope.addDocument(document);
envelope.addSigner(signer);

String envelopeId = docusign.sendEnvelopeForSignature(envelope);
// Envelope is created but not sent
```

## Best Practices

### 1. Configuration Management

- **Never commit credentials**: Keep configuration files out of version control
- **Use environment variables**: For production, use environment-specific configuration
- **Secure private keys**: Store RSA private keys securely with restricted access

### 2. Error Handling

Always wrap API calls in try-catch blocks:

```java
try {
    docusign.authenticate();
    String envelopeId = docusign.sendEnvelopeForSignature(envelope);
    System.out.println("Success: " + envelopeId);
} catch (ApiException e) {
    System.err.println("DocuSign API Error: " + e.getMessage());
    e.printStackTrace();
} catch (IOException e) {
    System.err.println("File I/O Error: " + e.getMessage());
    e.printStackTrace();
}
```

### 3. Document Preparation

- **Add anchor strings**: Always include `/sn1/` or `**signature_1**` in your documents
- **Test documents first**: Verify documents render correctly in DocuSign
- **Use clear names**: Give documents descriptive names for signers

### 4. Testing

- **Use DocuSign Demo environment**: Test with demo credentials before production
- **Validate email addresses**: Ensure signer emails are valid
- **Test signature placement**: Verify anchor strings work correctly

### 5. Performance

- **Reuse authentication**: Don't authenticate for every envelope if sending multiple
- **Batch operations**: Use the same authenticated session for multiple envelopes

```java
DocuSignIntegration docusign = new DocuSignIntegration(config);
docusign.authenticate();

// Send multiple envelopes without re-authenticating
for (EnvelopeConfig envelope : envelopes) {
    String envelopeId = docusign.sendEnvelopeForSignature(envelope);
    System.out.println("Sent: " + envelopeId);
}
```

## Troubleshooting

### Authentication Failures

**Problem**: "USER_AUTHENTICATION_FAILED" error

**Solutions**:
1. Verify your Integration Key, User ID, and Account ID are correct
2. Ensure you've granted consent via the OAuth URL
3. Check that your private key file is readable and not corrupted
4. Verify you're using the correct environment (demo vs production)

### File Not Found Errors

**Problem**: "File does not exist" when creating documents

**Solutions**:
1. Use absolute paths or verify relative paths are correct
2. Check file permissions
3. Ensure the file exists at the specified location

### Signature Fields Not Appearing

**Problem**: Documents don't show signature fields

**Solutions**:
1. Add anchor strings (`/sn1/` or `**signature_1**`) to your documents
2. Verify anchor strings are visible in the document (not in headers/footers)
3. Check that anchor strings match exactly (case-sensitive)

### Envelope Status Issues

**Problem**: Envelope stuck in "sent" status

**Solutions**:
1. Check that recipient email addresses are valid
2. Ask recipients to check spam/junk folders
3. Verify envelope was sent to the correct email addresses

### Embedded Signing URL Expired

**Problem**: "TOKEN_EXPIRED" error on signing URL

**Solutions**:
1. Signing URLs expire after 5 minutes of inactivity
2. Generate a new URL if needed
3. Store the envelope ID to regenerate URLs

## Examples

Check the `Example.java` file for complete working examples:
- Email-based signing
- Embedded signing
- Multiple signers
- Multiple documents
- Document generation from bytes

## Support

For additional help:
- [DocuSign Developer Center](https://developers.docusign.com/)
- [GitHub Issues](https://github.com/pocketbroker/eDocumentGeneration/issues)
- [DocuSign Support](https://developers.docusign.com/support)

## Additional Resources

- [DocuSign eSignature REST API Documentation](https://developers.docusign.com/docs/esign-rest-api/)
- [DocuSign Java SDK](https://github.com/docusign/docusign-esign-java-client)
- [DocuSign Code Examples](https://github.com/docusign/code-examples-java)
