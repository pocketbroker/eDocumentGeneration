package com.pocketbroker.edocument.config;

/**
 * Configuration class for DocuSign API credentials and settings.
 */
public class DocuSignConfig {
    private String integrationKey;
    private String userId;
    private String accountId;
    private String privateKeyPath;
    private String basePath;
    private String oAuthBasePath;

    public DocuSignConfig() {
        // Default to DocuSign demo environment
        this.basePath = "https://demo.docusign.net/restapi";
        this.oAuthBasePath = "account-d.docusign.com";
    }

    public DocuSignConfig(String integrationKey, String userId, String accountId, String privateKeyPath) {
        this();
        this.integrationKey = integrationKey;
        this.userId = userId;
        this.accountId = accountId;
        this.privateKeyPath = privateKeyPath;
    }

    public String getIntegrationKey() {
        return integrationKey;
    }

    public void setIntegrationKey(String integrationKey) {
        this.integrationKey = integrationKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getOAuthBasePath() {
        return oAuthBasePath;
    }

    public void setOAuthBasePath(String oAuthBasePath) {
        this.oAuthBasePath = oAuthBasePath;
    }

    /**
     * Validates that all required configuration is present.
     * @throws IllegalStateException if required configuration is missing
     */
    public void validate() {
        if (integrationKey == null || integrationKey.isEmpty()) {
            throw new IllegalStateException("Integration Key is required");
        }
        if (userId == null || userId.isEmpty()) {
            throw new IllegalStateException("User ID is required");
        }
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalStateException("Account ID is required");
        }
        if (privateKeyPath == null || privateKeyPath.isEmpty()) {
            throw new IllegalStateException("Private Key Path is required");
        }
    }

    @Override
    public String toString() {
        return "DocuSignConfig{" +
                "integrationKey='" + (integrationKey != null ? "***" : "null") + '\'' +
                ", userId='" + userId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", basePath='" + basePath + '\'' +
                ", oAuthBasePath='" + oAuthBasePath + '\'' +
                '}';
    }
}
