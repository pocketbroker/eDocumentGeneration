package com.pocketbroker.edocument.partner;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultSignableDocumentTest {
    
    @Test
    void testContentImmutability_Constructor() {
        byte[] originalContent = "Test content".getBytes();
        byte[] contentCopy = originalContent.clone();
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key", "value");
        
        DefaultSignableDocument document = new DefaultSignableDocument(
                "doc-123",
                originalContent,
                "PDF",
                LocalDateTime.now(),
                metadata,
                true
        );
        
        // Modify the original array
        originalContent[0] = 'X';
        
        // Document content should not be affected
        assertArrayEquals(contentCopy, document.getContent());
        assertNotEquals(originalContent[0], document.getContent()[0]);
    }
    
    @Test
    void testContentImmutability_Getter() {
        byte[] content = "Test content".getBytes();
        Map<String, String> metadata = new HashMap<>();
        
        DefaultSignableDocument document = new DefaultSignableDocument(
                "doc-123",
                content,
                "PDF",
                LocalDateTime.now(),
                metadata,
                true
        );
        
        byte[] retrievedContent = document.getContent();
        byte[] contentCopy = retrievedContent.clone();
        
        // Modify the retrieved array
        retrievedContent[0] = 'X';
        
        // Document content should not be affected
        byte[] retrievedAgain = document.getContent();
        assertArrayEquals(contentCopy, retrievedAgain);
        assertNotEquals(retrievedContent[0], retrievedAgain[0]);
    }
    
    @Test
    void testMetadataImmutability() {
        Map<String, String> originalMetadata = new HashMap<>();
        originalMetadata.put("key", "value");
        
        DefaultSignableDocument document = new DefaultSignableDocument(
                "doc-123",
                "content".getBytes(),
                "PDF",
                LocalDateTime.now(),
                originalMetadata,
                true
        );
        
        // Modify original metadata
        originalMetadata.put("newKey", "newValue");
        
        // Document metadata should not be affected
        assertFalse(document.getMetadata().containsKey("newKey"));
        assertEquals(1, document.getMetadata().size());
    }
    
    @Test
    void testGetMetadataReturnsDefensiveCopy() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key", "value");
        
        DefaultSignableDocument document = new DefaultSignableDocument(
                "doc-123",
                "content".getBytes(),
                "PDF",
                LocalDateTime.now(),
                metadata,
                true
        );
        
        Map<String, String> retrievedMetadata = document.getMetadata();
        retrievedMetadata.put("newKey", "newValue");
        
        // Document metadata should not be affected
        assertFalse(document.getMetadata().containsKey("newKey"));
        assertEquals(1, document.getMetadata().size());
    }
    
    @Test
    void testAllFields() {
        String documentId = "doc-123";
        byte[] content = "Test content".getBytes();
        String documentType = "PDF";
        LocalDateTime createdAt = LocalDateTime.now();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("author", "system");
        boolean readyForSigning = true;
        
        DefaultSignableDocument document = new DefaultSignableDocument(
                documentId,
                content,
                documentType,
                createdAt,
                metadata,
                readyForSigning
        );
        
        assertEquals(documentId, document.getDocumentId());
        assertArrayEquals(content, document.getContent());
        assertEquals(documentType, document.getDocumentType());
        assertEquals(createdAt, document.getCreatedAt());
        assertEquals("system", document.getMetadata().get("author"));
        assertEquals(readyForSigning, document.isReadyForSigning());
    }
}
