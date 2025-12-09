package com.pocketbroker.edocument.example;

import com.pocketbroker.edocument.api.DocumentGenerationException;
import com.pocketbroker.edocument.api.DocumentRequest;
import com.pocketbroker.edocument.api.SignableDocument;
import com.pocketbroker.edocument.config.DocumentGenerationConfig;
import com.pocketbroker.edocument.service.DocumentGenerationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Example application demonstrating how to use the document generation system.
 */
public class DocumentGenerationExample {
    
    public static void main(String[] args) {
        // Initialize Spring context
        ApplicationContext context = new AnnotationConfigApplicationContext(DocumentGenerationConfig.class);
        DocumentGenerationService service = context.getBean(DocumentGenerationService.class);
        
        System.out.println("=== Electronic Document Generation Example ===\n");
        
        // Example 1: Generate document for Partner A
        try {
            System.out.println("1. Generating document for Partner A...");
            DocumentRequest requestA = DocumentRequest.builder()
                    .templateName("loan-agreement")
                    .addData("customerName", "John Doe")
                    .addData("accountNumber", "ACC-12345")
                    .addData("amount", 25000.00)
                    .addMetadata("documentType", "loan")
                    .addMetadata("department", "lending")
                    .build();
            
            SignableDocument documentA = service.generateDocument("Partner A", requestA);
            printDocumentInfo(documentA);
            
        } catch (DocumentGenerationException e) {
            System.err.println("Error generating Partner A document: " + e.getMessage());
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Example 2: Generate document for Partner B
        try {
            System.out.println("2. Generating document for Partner B...");
            DocumentRequest requestB = DocumentRequest.builder()
                    .templateName("PB_contract")
                    .addData("clientName", "Jane Smith")
                    .addData("contractId", "CTR-67890")
                    .addData("startDate", "2025-01-01")
                    .addMetadata("region", "north")
                    .addMetadata("priority", "high")
                    .build();
            
            SignableDocument documentB = service.generateDocument("Partner B", requestB);
            printDocumentInfo(documentB);
            
        } catch (DocumentGenerationException e) {
            System.err.println("Error generating Partner B document: " + e.getMessage());
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Example 3: Template validation
        System.out.println("3. Template validation examples:");
        System.out.println("   Partner A 'loan-agreement': " + 
                service.validateTemplate("Partner A", "loan-agreement"));
        System.out.println("   Partner A 'invalid template': " + 
                service.validateTemplate("Partner A", "invalid template"));
        System.out.println("   Partner B 'PB_contract': " + 
                service.validateTemplate("Partner B", "PB_contract"));
        System.out.println("   Partner B 'contract': " + 
                service.validateTemplate("Partner B", "contract"));
        
        System.out.println("\n=== Example Complete ===");
    }
    
    private static void printDocumentInfo(SignableDocument document) {
        System.out.println("   Document ID: " + document.getDocumentId());
        System.out.println("   Document Type: " + document.getDocumentType());
        System.out.println("   Created At: " + document.getCreatedAt());
        System.out.println("   Ready for Signing: " + document.isReadyForSigning());
        System.out.println("   Content Size: " + document.getContent().length + " bytes");
        System.out.println("   Metadata: " + document.getMetadata());
        System.out.println("   Content Preview:");
        String content = new String(document.getContent());
        String[] lines = content.split("\n");
        for (int i = 0; i < Math.min(lines.length, 8); i++) {
            System.out.println("      " + lines[i]);
        }
        if (lines.length > 8) {
            System.out.println("      ...");
        }
    }
}
