package com.pocketbroker.edocument.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Request object for document generation.
 */
public class DocumentRequest {
    
    private final String templateName;
    private final Map<String, Object> data;
    private final Map<String, String> metadata;
    
    private DocumentRequest(Builder builder) {
        this.templateName = builder.templateName;
        this.data = builder.data;
        this.metadata = builder.metadata;
    }
    
    public String getTemplateName() {
        return templateName;
    }
    
    public Map<String, Object> getData() {
        return new HashMap<>(data);
    }
    
    public Map<String, String> getMetadata() {
        return new HashMap<>(metadata);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String templateName;
        private Map<String, Object> data = new HashMap<>();
        private Map<String, String> metadata = new HashMap<>();
        
        public Builder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }
        
        public Builder data(Map<String, Object> data) {
            this.data = new HashMap<>(data);
            return this;
        }
        
        public Builder addData(String key, Object value) {
            this.data.put(key, value);
            return this;
        }
        
        public Builder metadata(Map<String, String> metadata) {
            this.metadata = new HashMap<>(metadata);
            return this;
        }
        
        public Builder addMetadata(String key, String value) {
            this.metadata.put(key, value);
            return this;
        }
        
        public DocumentRequest build() {
            if (templateName == null || templateName.isEmpty()) {
                throw new IllegalArgumentException("Template name is required");
            }
            return new DocumentRequest(this);
        }
    }
}
