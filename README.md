# eDocumentGeneration

A Java library for generating and managing electronically signable documents using the DocuSign platform.

## Features

- **Document Generation**: Create signable documents from files or byte arrays
- **DocuSign Integration**: Seamlessly integrate with DocuSign's eSignature API
- **Multiple Signing Methods**: Support for both email-based and embedded signing
- **Flexible Configuration**: Easy configuration management for DocuSign credentials
- **Type-Safe Models**: Strongly-typed model classes for documents, signers, and envelopes

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- DocuSign Developer Account (free at https://developers.docusign.com/)

## Installation

### Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.pocketbroker</groupId>
    <artifactId>edocument-generation</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Build from Source

```bash
git clone https://github.com/pocketbroker/eDocumentGeneration.git
cd eDocumentGeneration
mvn clean install
```

## DocuSign Setup

1. **Create a DocuSign Developer Account**: Visit https://developers.docusign.com/ and sign up for a free developer account.

2. **Create an Integration Key**:
   - Log into your DocuSign Developer Account
   - Go to Settings → Apps and Keys
   - Click "Add App and Integration Key"
   - Note the Integration Key (Client ID)

3. **Generate RSA Key Pair**:
   - In the same Apps and Keys page, under "Authentication" section
   - Click "Add RSA Keypair"
   - Download and save the private key file
   - Add the public key to your integration

4. **Grant Consent**:
   - Use the following URL to grant consent (replace YOUR_INTEGRATION_KEY):
   ```
   https://account-d.docusign.com/oauth/auth?response_type=code&scope=signature%20impersonation&client_id=YOUR_INTEGRATION_KEY&redirect_uri=https://www.docusign.com
   ```

5. **Get Your User ID and Account ID**:
   - User ID (GUID): Found in your Apps and Keys page
   - Account ID: Found in your account settings

## Quick Start

### Example 1: Send Document for Signature via Email

```java
import com.pocketbroker.edocument.config.DocuSignConfig;
import com.pocketbroker.edocument.model.Document;
import com.pocketbroker.edocument.model.EnvelopeConfig;
import com.pocketbroker.edocument.model.Signer;
import com.pocketbroker.edocument.service.DocumentGenerator;
import com.pocketbroker.edocument.service.DocuSignIntegration;

public class SigningExample {
    public static void main(String[] args) throws Exception {
        // 1. Configure DocuSign
        DocuSignConfig config = new DocuSignConfig();
        config.setIntegrationKey("YOUR_INTEGRATION_KEY");
        config.setUserId("YOUR_USER_ID");
        config.setAccountId("YOUR_ACCOUNT_ID");
        config.setPrivateKeyPath("/path/to/private.key");

        // 2. Create document generator
        DocumentGenerator generator = new DocumentGenerator();

        // 3. Create a document from file
        Document document = generator.createDocumentFromFile(
            "/path/to/contract.pdf",
            "Contract Agreement"
        );

        // 4. Create signer
        Signer signer = generator.createSigner(
            "signer@example.com",
            "John Doe"
        );

        // 5. Create envelope configuration
        EnvelopeConfig envelope = generator.createEnvelopeConfig(
            "Please sign this contract"
        );
        envelope.addDocument(document);
        envelope.addSigner(signer);

        // 6. Send for signature
        DocuSignIntegration docusign = new DocuSignIntegration(config);
        docusign.authenticate();
        String envelopeId = docusign.sendEnvelopeForSignature(envelope);

        System.out.println("Envelope sent! ID: " + envelopeId);
    }
}
```

### Example 2: Embedded Signing

```java
// Create embedded signer (requires clientUserId)
Signer embeddedSigner = generator.createEmbeddedSigner(
    "signer@example.com",
    "John Doe",
    "user123" // Your internal user ID
);

// Add to envelope
EnvelopeConfig envelope = generator.createEnvelopeConfig("Sign this");
envelope.addDocument(document);
envelope.addSigner(embeddedSigner);

// Get signing URL
DocuSignIntegration docusign = new DocuSignIntegration(config);
docusign.authenticate();
String signingUrl = docusign.createEmbeddedSigningUrl(
    envelope,
    "https://yourapp.com/signing-complete"
);

// Redirect user to signingUrl to sign within your application
System.out.println("Signing URL: " + signingUrl);
```

### Example 3: Multiple Signers

```java
// Create multiple signers
Signer signer1 = generator.createSigner("signer1@example.com", "John Doe");
signer1.setRoutingOrder(1);

Signer signer2 = generator.createSigner("signer2@example.com", "Jane Smith");
signer2.setRoutingOrder(2);

// Add to envelope
envelope.addSigner(signer1);
envelope.addSigner(signer2);

// Documents will be sent to signer1 first, then to signer2 after signing
```

## API Documentation

### DocumentGenerator

The `DocumentGenerator` class provides methods for creating documents and related objects:

- `createDocumentFromFile(String filePath, String documentName)`: Create a document from a file
- `createDocumentFromBytes(byte[] content, String documentName, String fileExtension)`: Create a document from byte array
- `createSigner(String email, String name)`: Create a signer for email-based signing
- `createEmbeddedSigner(String email, String name, String clientUserId)`: Create a signer for embedded signing
- `createEnvelopeConfig(String emailSubject)`: Create an envelope configuration

### DocuSignIntegration

The `DocuSignIntegration` class handles DocuSign API interactions:

- `authenticate()`: Authenticate with DocuSign using JWT
- `sendEnvelopeForSignature(EnvelopeConfig config)`: Send envelope for signature via email
- `createEmbeddedSigningUrl(EnvelopeConfig config, String returnUrl)`: Create URL for embedded signing
- `getEnvelopeStatus(String envelopeId)`: Get the status of an envelope

### Model Classes

- **Document**: Represents a document to be signed
- **Signer**: Represents a person who will sign the document
- **EnvelopeConfig**: Configuration for an envelope containing documents and signers
- **DocuSignConfig**: Configuration for DocuSign API credentials

## Document Signature Placement

The library automatically places signature fields using anchor strings. Add one of these strings to your PDF documents where you want signatures:

- `/sn1/` - Primary signature location
- `**signature_1**` - Alternative signature location

## Testing

Run the test suite:

```bash
mvn test
```

## Building

Build the library:

```bash
mvn clean package
```

This will create a JAR file in the `target` directory.

## Examples

Check out the `Example.java` class for complete working examples:

```bash
src/main/java/com/pocketbroker/edocument/Example.java
```

## Troubleshooting

### Authentication Issues

- Ensure your private key file is accessible and readable
- Verify your Integration Key, User ID, and Account ID are correct
- Make sure you've granted consent using the OAuth URL

### Envelope Creation Issues

- Ensure documents contain anchor strings for signature placement (`/sn1/` or `**signature_1**`)
- Verify file paths are correct and files are readable
- Check that signer email addresses are valid

### Embedded Signing Issues

- Ensure `clientUserId` is set for embedded signers
- Verify the return URL is a valid HTTPS URL (for production)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License.

## Support

For issues and questions:
- GitHub Issues: https://github.com/pocketbroker/eDocumentGeneration/issues
- DocuSign Support: https://developers.docusign.com/support

## References

- [DocuSign Developer Center](https://developers.docusign.com/)
- [DocuSign Java SDK](https://github.com/docusign/docusign-esign-java-client)
- [DocuSign Code Examples](https://github.com/docusign/code-examples-java)
