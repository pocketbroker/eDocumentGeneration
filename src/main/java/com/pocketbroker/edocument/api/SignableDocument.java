package com.pocketbroker.edocument.api;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents an electronically signable document.
 */
public interface SignableDocument {
    
    /**
     * Gets the unique identifier for this document.
     * 
     * @return the document ID
     */
    String getDocumentId();
    
    /**
     * Gets the document content in the specified format.
     * 
     * @return the document content as a byte array
     */
    byte[] getContent();
    
    /**
     * Gets the document type (e.g., "PDF", "DOCX").
     * 
     * @return the document type
     */
    String getDocumentType();
    
    /**
     * Gets the creation timestamp of the document.
     * 
     * @return the creation timestamp
     */
    LocalDateTime getCreatedAt();
    
    /**
     * Gets document metadata.
     * 
     * @return map of metadata key-value pairs
     */
    Map<String, String> getMetadata();
    
    /**
     * Checks if the document is ready for signing.
     * 
     * @return true if the document is ready for signing, false otherwise
     */
    boolean isReadyForSigning();
}
