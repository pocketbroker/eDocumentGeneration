package com.pocketbroker.edocument.service;

import com.pocketbroker.edocument.api.DocumentGenerationException;
import com.pocketbroker.edocument.api.DocumentRequest;
import com.pocketbroker.edocument.api.SignableDocument;
import com.pocketbroker.edocument.config.DocumentGeneratorFactory;
import com.pocketbroker.edocument.partner.PartnerADocumentGenerator;
import com.pocketbroker.edocument.partner.PartnerBDocumentGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DocumentGenerationServiceTest {
    
    private DocumentGenerationService service;
    
    @BeforeEach
    void setUp() {
        DocumentGeneratorFactory factory = new DocumentGeneratorFactory(Arrays.asList(
                new PartnerADocumentGenerator(),
                new PartnerBDocumentGenerator()
        ));
        service = new DocumentGenerationService(factory);
    }
    
    @Test
    void testGenerateDocument_PartnerA() throws DocumentGenerationException {
        Map<String, Object> data = new HashMap<>();
        data.put("field", "value");
        
        DocumentRequest request = DocumentRequest.builder()
                .templateName("test-template")
                .data(data)
                .addMetadata("key", "value")
                .build();
        
        SignableDocument document = service.generateDocument("Partner A", request);
        
        assertNotNull(document);
        assertEquals("PDF", document.getDocumentType());
        assertTrue(document.isReadyForSigning());
    }
    
    @Test
    void testGenerateDocument_PartnerB() throws DocumentGenerationException {
        Map<String, Object> data = new HashMap<>();
        data.put("field", "value");
        
        DocumentRequest request = DocumentRequest.builder()
                .templateName("PB_template")
                .data(data)
                .build();
        
        SignableDocument document = service.generateDocument("Partner B", request);
        
        assertNotNull(document);
        assertTrue(document.getDocumentId().startsWith("PB-"));
        assertEquals("PDF", document.getDocumentType());
    }
    
    @Test
    void testGenerateDocument_InvalidPartner() {
        DocumentRequest request = DocumentRequest.builder()
                .templateName("test")
                .build();
        
        assertThrows(IllegalArgumentException.class, () -> {
            service.generateDocument("Partner C", request);
        });
    }
    
    @Test
    void testValidateTemplate_PartnerA() {
        assertTrue(service.validateTemplate("Partner A", "test-template"));
        assertFalse(service.validateTemplate("Partner A", "invalid template"));
    }
    
    @Test
    void testValidateTemplate_PartnerB() {
        assertTrue(service.validateTemplate("Partner B", "PB_template"));
        assertFalse(service.validateTemplate("Partner B", "test-template"));
    }
    
    @Test
    void testValidateTemplate_InvalidPartner() {
        assertFalse(service.validateTemplate("Partner C", "template"));
    }
}
