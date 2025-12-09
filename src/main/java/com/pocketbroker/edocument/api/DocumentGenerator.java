package com.pocketbroker.edocument.api;

import java.util.Map;

/**
 * Interface for generating signable electronic documents.
 * Implementations should be partner-specific.
 */
public interface DocumentGenerator {
    
    /**
     * Creates a signable document with the provided content and metadata.
     * 
     * @param templateName the name of the document template to use
     * @param data the data to populate the document with
     * @param metadata additional metadata for the document
     * @return a SignableDocument instance
     * @throws DocumentGenerationException if document generation fails
     */
    SignableDocument createDocument(String templateName, Map<String, Object> data, Map<String, String> metadata)
            throws DocumentGenerationException;
    
    /**
     * Validates if a template exists and is usable.
     * 
     * @param templateName the name of the template to validate
     * @return true if the template is valid, false otherwise
     */
    boolean validateTemplate(String templateName);
    
    /**
     * Gets the partner name for this document generator implementation.
     * 
     * @return the partner name
     */
    String getPartnerName();
}
