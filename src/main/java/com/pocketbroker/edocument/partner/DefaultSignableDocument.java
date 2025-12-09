package com.pocketbroker.edocument.partner;

import com.pocketbroker.edocument.api.SignableDocument;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of SignableDocument.
 */
public class DefaultSignableDocument implements SignableDocument {
    
    private final String documentId;
    private final byte[] content;
    private final String documentType;
    private final LocalDateTime createdAt;
    private final Map<String, String> metadata;
    private final boolean readyForSigning;
    
    public DefaultSignableDocument(String documentId, byte[] content, String documentType,
                                   LocalDateTime createdAt, Map<String, String> metadata,
                                   boolean readyForSigning) {
        this.documentId = documentId;
        this.content = content;
        this.documentType = documentType;
        this.createdAt = createdAt;
        this.metadata = new HashMap<>(metadata);
        this.readyForSigning = readyForSigning;
    }
    
    @Override
    public String getDocumentId() {
        return documentId;
    }
    
    @Override
    public byte[] getContent() {
        return content;
    }
    
    @Override
    public String getDocumentType() {
        return documentType;
    }
    
    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    @Override
    public Map<String, String> getMetadata() {
        return new HashMap<>(metadata);
    }
    
    @Override
    public boolean isReadyForSigning() {
        return readyForSigning;
    }
}
