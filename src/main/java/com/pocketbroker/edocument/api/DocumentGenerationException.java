package com.pocketbroker.edocument.api;

/**
 * Exception thrown when document generation fails.
 */
public class DocumentGenerationException extends Exception {
    
    public DocumentGenerationException(String message) {
        super(message);
    }
    
    public DocumentGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
