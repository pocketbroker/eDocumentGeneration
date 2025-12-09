package com.pocketbroker.edocument;

import com.pocketbroker.edocument.config.DocuSignConfig;
import com.pocketbroker.edocument.model.Document;
import com.pocketbroker.edocument.model.EnvelopeConfig;
import com.pocketbroker.edocument.model.Signer;
import com.pocketbroker.edocument.service.DocumentGenerator;
import com.pocketbroker.edocument.service.DocuSignIntegration;

/**
 * Example demonstrating how to use the eDocument Generation library.
 * 
 * This example shows:
 * 1. How to configure DocuSign credentials
 * 2. How to create documents for signing
 * 3. How to send documents for electronic signature via email
 * 4. How to create embedded signing URLs
 */
public class Example {

    public static void main(String[] args) {
        try {
            // Example 1: Sending document for signature via email
            sendDocumentViaEmail();
            
            // Example 2: Creating embedded signing URL
            // createEmbeddedSigningUrl();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example: Send a document for signature via email.
     * The recipient will receive an email with a link to sign the document.
     */
    public static void sendDocumentViaEmail() throws Exception {
        System.out.println("=== Example: Sending Document for Signature via Email ===\n");

        // Step 1: Configure DocuSign credentials
        // IMPORTANT: Replace these placeholder values with your actual DocuSign credentials
        // For better security, consider using ConfigLoader to load from a config.properties file
        // See USAGE_GUIDE.md for setup instructions
        // These placeholder values will cause authentication to fail - you MUST replace them
        DocuSignConfig config = new DocuSignConfig();
        config.setIntegrationKey("REPLACE_WITH_YOUR_INTEGRATION_KEY");
        config.setUserId("REPLACE_WITH_YOUR_USER_ID");
        config.setAccountId("REPLACE_WITH_YOUR_ACCOUNT_ID");
        config.setPrivateKeyPath("/path/to/your/private.key");
        
        System.out.println("1. DocuSign configuration created");

        // Step 2: Create document generator
        DocumentGenerator generator = new DocumentGenerator();
        
        // Step 3: Create a document from a PDF file
        // Note: Replace with actual file path
        Document document = generator.createDocumentFromFile(
                "/path/to/document.pdf",
                "Contract Agreement"
        );
        System.out.println("2. Document created: " + document.getName());

        // Step 4: Create signers
        Signer signer1 = generator.createSigner(
                "signer1@example.com",
                "John Doe"
        );
        signer1.setRoutingOrder(1);
        
        Signer signer2 = generator.createSigner(
                "signer2@example.com",
                "Jane Smith"
        );
        signer2.setRoutingOrder(2);
        
        System.out.println("3. Signers created: " + signer1.getName() + ", " + signer2.getName());

        // Step 5: Create envelope configuration
        EnvelopeConfig envelopeConfig = generator.createEnvelopeConfig(
                "Please sign this important contract"
        );
        envelopeConfig.setEmailBody("This is a contract that requires your signature.");
        envelopeConfig.addDocument(document);
        envelopeConfig.addSigner(signer1);
        envelopeConfig.addSigner(signer2);
        
        System.out.println("4. Envelope configuration created");

        // Step 6: Authenticate with DocuSign and send envelope
        DocuSignIntegration docuSignIntegration = new DocuSignIntegration(config);
        docuSignIntegration.authenticate();
        System.out.println("5. Authenticated with DocuSign");

        String envelopeId = docuSignIntegration.sendEnvelopeForSignature(envelopeConfig);
        System.out.println("6. Envelope sent! Envelope ID: " + envelopeId);

        // Step 7: Check envelope status
        String status = docuSignIntegration.getEnvelopeStatus(envelopeId);
        System.out.println("7. Envelope status: " + status);
        
        System.out.println("\n=== Document sent successfully! ===");
    }

    /**
     * Example: Create an embedded signing URL.
     * This allows the signer to sign within your application without leaving your site.
     */
    public static void createEmbeddedSigningUrl() throws Exception {
        System.out.println("=== Example: Creating Embedded Signing URL ===\n");

        // Step 1: Configure DocuSign credentials
        // IMPORTANT: Replace these placeholder values with your actual DocuSign credentials
        // For better security, consider using ConfigLoader to load from a config.properties file
        // See USAGE_GUIDE.md for setup instructions
        // These placeholder values will cause authentication to fail - you MUST replace them
        DocuSignConfig config = new DocuSignConfig();
        config.setIntegrationKey("REPLACE_WITH_YOUR_INTEGRATION_KEY");
        config.setUserId("REPLACE_WITH_YOUR_USER_ID");
        config.setAccountId("REPLACE_WITH_YOUR_ACCOUNT_ID");
        config.setPrivateKeyPath("/path/to/your/private.key");
        
        System.out.println("1. DocuSign configuration created");

        // Step 2: Create document generator
        DocumentGenerator generator = new DocumentGenerator();
        
        // Step 3: Create a document
        Document document = generator.createDocumentFromFile(
                "/path/to/document.pdf",
                "Agreement"
        );
        System.out.println("2. Document created: " + document.getName());

        // Step 4: Create embedded signer (note: clientUserId is required for embedded signing)
        Signer signer = generator.createEmbeddedSigner(
                "signer@example.com",
                "John Doe",
                "user123" // This is your internal user ID
        );
        System.out.println("3. Embedded signer created: " + signer.getName());

        // Step 5: Create envelope configuration
        EnvelopeConfig envelopeConfig = generator.createEnvelopeConfig(
                "Please sign this document"
        );
        envelopeConfig.addDocument(document);
        envelopeConfig.addSigner(signer);
        
        System.out.println("4. Envelope configuration created");

        // Step 6: Authenticate with DocuSign and create embedded signing URL
        DocuSignIntegration docuSignIntegration = new DocuSignIntegration(config);
        docuSignIntegration.authenticate();
        System.out.println("5. Authenticated with DocuSign");

        String signingUrl = docuSignIntegration.createEmbeddedSigningUrl(
                envelopeConfig,
                "https://yourapp.com/signing-complete" // Return URL after signing
        );
        System.out.println("6. Embedded signing URL created: " + signingUrl);
        System.out.println("   Redirect the user to this URL to sign the document.");
        
        System.out.println("\n=== Embedded signing URL created successfully! ===");
    }

    /**
     * Example: Create a document from byte array (useful for generating documents on the fly).
     */
    public static void createDocumentFromBytes() throws Exception {
        System.out.println("=== Example: Creating Document from Bytes ===\n");

        DocumentGenerator generator = new DocumentGenerator();
        
        // Example: Create a simple text document
        String content = "This is a sample document content.";
        byte[] documentBytes = content.getBytes();
        
        Document document = generator.createDocumentFromBytes(
                documentBytes,
                "Sample Document",
                "txt"
        );
        
        System.out.println("Document created from bytes: " + document.getName());
        System.out.println("Document size: " + document.getContent().length + " bytes");
    }
}
