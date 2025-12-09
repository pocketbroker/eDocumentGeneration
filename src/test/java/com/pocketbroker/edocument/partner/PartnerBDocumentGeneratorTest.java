package com.pocketbroker.edocument.partner;

import com.pocketbroker.edocument.api.DocumentGenerationException;
import com.pocketbroker.edocument.api.SignableDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PartnerBDocumentGeneratorTest {
    
    private PartnerBDocumentGenerator generator;
    
    @BeforeEach
    void setUp() {
        generator = new PartnerBDocumentGenerator();
    }
    
    @Test
    void testGetPartnerName() {
        assertEquals("Partner B", generator.getPartnerName());
    }
    
    @Test
    void testValidateTemplate_Valid() {
        assertTrue(generator.validateTemplate("PB_template"));
        assertTrue(generator.validateTemplate("PB_test123"));
        assertTrue(generator.validateTemplate("PB_"));
    }
    
    @Test
    void testValidateTemplate_Invalid() {
        assertFalse(generator.validateTemplate(null));
        assertFalse(generator.validateTemplate(""));
        assertFalse(generator.validateTemplate("template"));
        assertFalse(generator.validateTemplate("PA_template"));
    }
    
    @Test
    void testCreateDocument() throws DocumentGenerationException {
        Map<String, Object> data = new HashMap<>();
        data.put("customerName", "Jane Smith");
        data.put("amount", 2000.00);
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("department", "sales");
        
        SignableDocument document = generator.createDocument("PB_test", data, metadata);
        
        assertNotNull(document);
        assertNotNull(document.getDocumentId());
        assertTrue(document.getDocumentId().startsWith("PB-"));
        assertNotNull(document.getContent());
        assertEquals("PDF", document.getDocumentType());
        assertTrue(document.isReadyForSigning());
        assertNotNull(document.getCreatedAt());
        assertEquals("sales", document.getMetadata().get("department"));
    }
    
    @Test
    void testCreateDocument_InvalidTemplate() {
        Map<String, Object> data = new HashMap<>();
        Map<String, String> metadata = new HashMap<>();
        
        assertThrows(DocumentGenerationException.class, () -> {
            generator.createDocument("invalid-template", data, metadata);
        });
    }
    
    @Test
    void testDocumentContentContainsData() throws DocumentGenerationException {
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "value1");
        
        SignableDocument document = generator.createDocument("PB_template", data, new HashMap<>());
        String content = new String(document.getContent());
        
        assertTrue(content.contains("PARTNER B DOCUMENT"));
        assertTrue(content.contains("PB_template"));
        assertTrue(content.contains("field1"));
        assertTrue(content.contains("value1"));
    }
}
