package com.pocketbroker.edocument.partner;

import com.pocketbroker.edocument.api.DocumentGenerationException;
import com.pocketbroker.edocument.api.DocumentGenerator;
import com.pocketbroker.edocument.api.SignableDocument;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Partner A specific implementation of DocumentGenerator.
 * This implementation creates documents according to Partner A's specifications.
 */
@Component("partnerADocumentGenerator")
public class PartnerADocumentGenerator implements DocumentGenerator {
    
    private static final String PARTNER_NAME = "Partner A";
    
    @Override
    public SignableDocument createDocument(String templateName, Map<String, Object> data,
                                          Map<String, String> metadata) throws DocumentGenerationException {
        
        if (!validateTemplate(templateName)) {
            throw new DocumentGenerationException("Invalid template: " + templateName);
        }
        
        try {
            // Generate unique document ID
            String documentId = UUID.randomUUID().toString();
            
            // Create document content based on template and data
            String content = generateDocumentContent(templateName, data);
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            
            // Set document type
            String documentType = "PDF";
            
            // Create timestamp
            LocalDateTime createdAt = LocalDateTime.now();
            
            // Document is ready for signing after generation
            boolean readyForSigning = true;
            
            return new DefaultSignableDocument(documentId, contentBytes, documentType,
                    createdAt, metadata, readyForSigning);
            
        } catch (Exception e) {
            throw new DocumentGenerationException("Failed to create document", e);
        }
    }
    
    @Override
    public boolean validateTemplate(String templateName) {
        // Partner A specific template validation logic
        if (templateName == null || templateName.isEmpty()) {
            return false;
        }
        
        // For this example, we accept standard templates
        return templateName.matches("^[a-zA-Z0-9_-]+$");
    }
    
    @Override
    public String getPartnerName() {
        return PARTNER_NAME;
    }
    
    /**
     * Generates document content based on the template and data.
     * This is a simplified implementation for Partner A.
     */
    private String generateDocumentContent(String templateName, Map<String, Object> data) {
        StringBuilder content = new StringBuilder();
        content.append("Partner A Document\n");
        content.append("==================\n\n");
        content.append("Template: ").append(templateName).append("\n\n");
        content.append("Document Data:\n");
        
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            content.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        
        return content.toString();
    }
}
