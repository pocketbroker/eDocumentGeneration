package com.pocketbroker.edocument.partner;

import com.pocketbroker.edocument.api.DocumentGenerationException;
import com.pocketbroker.edocument.api.SignableDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PartnerADocumentGeneratorTest {
    
    private PartnerADocumentGenerator generator;
    
    @BeforeEach
    void setUp() {
        generator = new PartnerADocumentGenerator();
    }
    
    @Test
    void testGetPartnerName() {
        assertEquals("Partner A", generator.getPartnerName());
    }
    
    @Test
    void testValidateTemplate_Valid() {
        assertTrue(generator.validateTemplate("test-template"));
        assertTrue(generator.validateTemplate("template_123"));
        assertTrue(generator.validateTemplate("Template-Name"));
    }
    
    @Test
    void testValidateTemplate_Invalid() {
        assertFalse(generator.validateTemplate(null));
        assertFalse(generator.validateTemplate(""));
        assertFalse(generator.validateTemplate("template with spaces"));
        assertFalse(generator.validateTemplate("template@special"));
    }
    
    @Test
    void testCreateDocument() throws DocumentGenerationException {
        Map<String, Object> data = new HashMap<>();
        data.put("customerName", "John Doe");
        data.put("amount", 1000.00);
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("author", "system");
        
        SignableDocument document = generator.createDocument("test-template", data, metadata);
        
        assertNotNull(document);
        assertNotNull(document.getDocumentId());
        assertNotNull(document.getContent());
        assertEquals("PDF", document.getDocumentType());
        assertTrue(document.isReadyForSigning());
        assertNotNull(document.getCreatedAt());
        assertEquals("system", document.getMetadata().get("author"));
    }
    
    @Test
    void testCreateDocument_InvalidTemplate() {
        Map<String, Object> data = new HashMap<>();
        Map<String, String> metadata = new HashMap<>();
        
        assertThrows(DocumentGenerationException.class, () -> {
            generator.createDocument("invalid template", data, metadata);
        });
    }
    
    @Test
    void testCreateDocument_NullTemplate() {
        Map<String, Object> data = new HashMap<>();
        Map<String, String> metadata = new HashMap<>();
        
        assertThrows(DocumentGenerationException.class, () -> {
            generator.createDocument(null, data, metadata);
        });
    }
    
    @Test
    void testDocumentContentContainsData() throws DocumentGenerationException {
        Map<String, Object> data = new HashMap<>();
        data.put("testKey", "testValue");
        
        SignableDocument document = generator.createDocument("test-template", data, new HashMap<>());
        String content = new String(document.getContent());
        
        assertTrue(content.contains("Partner A Document"));
        assertTrue(content.contains("test-template"));
        assertTrue(content.contains("testKey"));
        assertTrue(content.contains("testValue"));
    }
}
