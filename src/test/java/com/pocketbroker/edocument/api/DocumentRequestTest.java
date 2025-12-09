package com.pocketbroker.edocument.api;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DocumentRequestTest {
    
    @Test
    void testBuilder() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "John Doe");
        data.put("age", 30);
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("author", "system");
        
        DocumentRequest request = DocumentRequest.builder()
                .templateName("test-template")
                .data(data)
                .metadata(metadata)
                .build();
        
        assertEquals("test-template", request.getTemplateName());
        assertEquals(2, request.getData().size());
        assertEquals("John Doe", request.getData().get("name"));
        assertEquals(1, request.getMetadata().size());
        assertEquals("system", request.getMetadata().get("author"));
    }
    
    @Test
    void testBuilderWithAddMethods() {
        DocumentRequest request = DocumentRequest.builder()
                .templateName("test-template")
                .addData("key1", "value1")
                .addData("key2", "value2")
                .addMetadata("meta1", "metavalue1")
                .build();
        
        assertEquals("test-template", request.getTemplateName());
        assertEquals(2, request.getData().size());
        assertEquals("value1", request.getData().get("key1"));
        assertEquals(1, request.getMetadata().size());
    }
    
    @Test
    void testBuilderRequiresTemplateName() {
        assertThrows(IllegalArgumentException.class, () -> {
            DocumentRequest.builder().build();
        });
    }
    
    @Test
    void testBuilderRejectsEmptyTemplateName() {
        assertThrows(IllegalArgumentException.class, () -> {
            DocumentRequest.builder().templateName("").build();
        });
    }
    
    @Test
    void testDataImmutability() {
        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");
        
        DocumentRequest request = DocumentRequest.builder()
                .templateName("test")
                .data(data)
                .build();
        
        // Modify original map
        data.put("newKey", "newValue");
        
        // Request data should not be affected
        assertEquals(1, request.getData().size());
        assertFalse(request.getData().containsKey("newKey"));
    }
}
