package com.pocketbroker.edocument.integration;

import com.pocketbroker.edocument.api.DocumentGenerationException;
import com.pocketbroker.edocument.api.DocumentRequest;
import com.pocketbroker.edocument.api.SignableDocument;
import com.pocketbroker.edocument.config.DocumentGenerationConfig;
import com.pocketbroker.edocument.service.DocumentGenerationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test demonstrating full Spring dependency injection.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DocumentGenerationConfig.class})
class DocumentGenerationIntegrationTest {
    
    @Autowired
    private DocumentGenerationService documentGenerationService;
    
    @Test
    void testSpringContextLoads() {
        assertNotNull(documentGenerationService);
    }
    
    @Test
    void testGenerateDocumentForPartnerA() throws DocumentGenerationException {
        Map<String, Object> data = new HashMap<>();
        data.put("customerName", "Alice Johnson");
        data.put("accountNumber", "12345");
        data.put("amount", 5000.00);
        
        DocumentRequest request = DocumentRequest.builder()
                .templateName("loan-agreement")
                .data(data)
                .addMetadata("documentType", "loan")
                .addMetadata("department", "lending")
                .build();
        
        SignableDocument document = documentGenerationService.generateDocument("Partner A", request);
        
        assertNotNull(document);
        assertNotNull(document.getDocumentId());
        assertNotNull(document.getContent());
        assertEquals("PDF", document.getDocumentType());
        assertTrue(document.isReadyForSigning());
        assertEquals("loan", document.getMetadata().get("documentType"));
        
        String content = new String(document.getContent());
        assertTrue(content.contains("Alice Johnson"));
        assertTrue(content.contains("12345"));
    }
    
    @Test
    void testGenerateDocumentForPartnerB() throws DocumentGenerationException {
        Map<String, Object> data = new HashMap<>();
        data.put("clientName", "Bob Smith");
        data.put("contractId", "PB-67890");
        
        DocumentRequest request = DocumentRequest.builder()
                .templateName("PB_contract")
                .data(data)
                .addMetadata("region", "north")
                .build();
        
        SignableDocument document = documentGenerationService.generateDocument("Partner B", request);
        
        assertNotNull(document);
        assertTrue(document.getDocumentId().startsWith("PB-"));
        assertNotNull(document.getContent());
        assertEquals("PDF", document.getDocumentType());
        assertTrue(document.isReadyForSigning());
        
        String content = new String(document.getContent());
        assertTrue(content.contains("Bob Smith"));
        assertTrue(content.contains("PB-67890"));
    }
    
    @Test
    void testValidateTemplates() {
        // Partner A templates
        assertTrue(documentGenerationService.validateTemplate("Partner A", "loan-agreement"));
        assertTrue(documentGenerationService.validateTemplate("Partner A", "account-statement"));
        assertFalse(documentGenerationService.validateTemplate("Partner A", "invalid template"));
        
        // Partner B templates
        assertTrue(documentGenerationService.validateTemplate("Partner B", "PB_contract"));
        assertTrue(documentGenerationService.validateTemplate("Partner B", "PB_invoice"));
        assertFalse(documentGenerationService.validateTemplate("Partner B", "contract"));
    }
    
    @Test
    void testMultipleDocumentGeneration() throws DocumentGenerationException {
        // Generate multiple documents to ensure the service is properly stateless
        for (int i = 0; i < 5; i++) {
            DocumentRequest request = DocumentRequest.builder()
                    .templateName("test-doc")
                    .addData("index", i)
                    .build();
            
            SignableDocument document = documentGenerationService.generateDocument("Partner A", request);
            assertNotNull(document);
            assertNotNull(document.getDocumentId());
        }
    }
}
