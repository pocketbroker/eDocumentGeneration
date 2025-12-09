package com.pocketbroker.edocument.service;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;
import com.docusign.esign.model.*;
import com.pocketbroker.edocument.config.DocuSignConfig;
import com.pocketbroker.edocument.model.Document;
import com.pocketbroker.edocument.model.EnvelopeConfig;
import com.pocketbroker.edocument.model.Signer;
import com.pocketbroker.edocument.util.DocumentUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * Service class for integrating with DocuSign API.
 * This class handles authentication and envelope creation for electronic signatures.
 */
public class DocuSignIntegration {
    
    private final DocuSignConfig config;
    private ApiClient apiClient;
    
    private static final int ANCHOR_OFFSET_Y = 10;
    private static final int ANCHOR_OFFSET_X = 20;
    private static final List<String> SCOPES = Collections.singletonList(OAuth.Scope_SIGNATURE);

    public DocuSignIntegration(DocuSignConfig config) {
        this.config = config;
        this.config.validate();
    }

    /**
     * Authenticates with DocuSign using JWT authentication.
     *
     * @throws ApiException if authentication fails
     * @throws IOException if reading the private key fails
     */
    public void authenticate() throws ApiException, IOException {
        apiClient = new ApiClient(config.getBasePath());
        
        // Read private key file
        byte[] privateKeyBytes = Files.readAllBytes(Paths.get(config.getPrivateKeyPath()));
        
        // Request JWT token
        OAuth.OAuthToken oAuthToken = apiClient.requestJWTUserToken(
                config.getIntegrationKey(),
                config.getUserId(),
                SCOPES,
                privateKeyBytes,
                3600
        );
        
        // Set the access token
        apiClient.setAccessToken(oAuthToken.getAccessToken(), oAuthToken.getExpiresIn());
        
        // Get user info
        OAuth.UserInfo userInfo = apiClient.getUserInfo(oAuthToken.getAccessToken());
        
        // Update base path if needed
        if (userInfo.getAccounts() != null && !userInfo.getAccounts().isEmpty()) {
            String baseUri = userInfo.getAccounts().get(0).getBaseUri();
            apiClient.setBasePath(baseUri + "/restapi");
        }
    }

    /**
     * Creates and sends an envelope for signature via email.
     *
     * @param envelopeConfig the envelope configuration containing documents and signers
     * @return the envelope ID
     * @throws ApiException if the API call fails
     */
    public String sendEnvelopeForSignature(EnvelopeConfig envelopeConfig) throws ApiException {
        if (apiClient == null) {
            throw new IllegalStateException("Must authenticate before sending envelopes");
        }

        EnvelopeDefinition envelope = createEnvelopeDefinition(envelopeConfig);
        
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(config.getAccountId(), envelope);
        
        return envelopeSummary.getEnvelopeId();
    }

    /**
     * Creates an envelope for embedded signing and returns the signing URL.
     *
     * @param envelopeConfig the envelope configuration
     * @param returnUrl the URL where the signer will be redirected after signing
     * @return the URL for embedded signing
     * @throws ApiException if the API call fails
     */
    public String createEmbeddedSigningUrl(EnvelopeConfig envelopeConfig, String returnUrl) 
            throws ApiException {
        if (apiClient == null) {
            throw new IllegalStateException("Must authenticate before creating embedded signing URL");
        }
        
        if (envelopeConfig.getSigners().isEmpty()) {
            throw new IllegalArgumentException("Envelope must have at least one signer");
        }

        // Create envelope
        EnvelopeDefinition envelope = createEnvelopeDefinition(envelopeConfig);
        
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(config.getAccountId(), envelope);
        
        // Create recipient view request for embedded signing
        Signer firstSigner = envelopeConfig.getSigners().get(0);
        RecipientViewRequest viewRequest = new RecipientViewRequest();
        viewRequest.setReturnUrl(returnUrl);
        viewRequest.setAuthenticationMethod("none");
        viewRequest.setEmail(firstSigner.getEmail());
        viewRequest.setUserName(firstSigner.getName());
        viewRequest.setClientUserId(firstSigner.getClientUserId());
        
        ViewUrl viewUrl = envelopesApi.createRecipientView(
                config.getAccountId(), 
                envelopeSummary.getEnvelopeId(), 
                viewRequest
        );
        
        return viewUrl.getUrl();
    }

    /**
     * Gets the status of an envelope.
     *
     * @param envelopeId the envelope ID
     * @return the envelope status
     * @throws ApiException if the API call fails
     */
    public String getEnvelopeStatus(String envelopeId) throws ApiException {
        if (apiClient == null) {
            throw new IllegalStateException("Must authenticate before getting envelope status");
        }
        
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        Envelope envelope = envelopesApi.getEnvelope(config.getAccountId(), envelopeId);
        
        return envelope.getStatus();
    }

    /**
     * Creates a DocuSign EnvelopeDefinition from our EnvelopeConfig.
     *
     * @param envelopeConfig the envelope configuration
     * @return DocuSign EnvelopeDefinition
     */
    private EnvelopeDefinition createEnvelopeDefinition(EnvelopeConfig envelopeConfig) {
        if (envelopeConfig.getDocuments().isEmpty()) {
            throw new IllegalArgumentException("Envelope must have at least one document");
        }
        if (envelopeConfig.getSigners().isEmpty()) {
            throw new IllegalArgumentException("Envelope must have at least one signer");
        }

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject(envelopeConfig.getEmailSubject());
        if (envelopeConfig.getEmailBody() != null) {
            envelope.setEmailBlurb(envelopeConfig.getEmailBody());
        }
        envelope.setStatus(envelopeConfig.getStatus());

        // Add documents
        List<com.docusign.esign.model.Document> docuSignDocuments = new ArrayList<>();
        int docId = 1;
        for (Document doc : envelopeConfig.getDocuments()) {
            com.docusign.esign.model.Document dsDoc = new com.docusign.esign.model.Document();
            dsDoc.setDocumentBase64(DocumentUtil.encodeToBase64(doc.getContent()));
            dsDoc.setName(doc.getName());
            dsDoc.setFileExtension(doc.getFileExtension());
            dsDoc.setDocumentId(String.valueOf(docId++));
            doc.setDocumentId(dsDoc.getDocumentId());
            docuSignDocuments.add(dsDoc);
        }
        envelope.setDocuments(docuSignDocuments);

        // Add signers
        Recipients recipients = new Recipients();
        List<com.docusign.esign.model.Signer> dsSigners = new ArrayList<>();
        int recipientId = 1;
        for (Signer signer : envelopeConfig.getSigners()) {
            com.docusign.esign.model.Signer dsSigner = new com.docusign.esign.model.Signer();
            dsSigner.setEmail(signer.getEmail());
            dsSigner.setName(signer.getName());
            dsSigner.setRecipientId(String.valueOf(recipientId++));
            dsSigner.setRoutingOrder(String.valueOf(signer.getRoutingOrder()));
            
            if (signer.getClientUserId() != null) {
                dsSigner.setClientUserId(signer.getClientUserId());
            }
            
            // Add signature tabs
            Tabs tabs = createSignatureTabs();
            dsSigner.setTabs(tabs);
            
            dsSigners.add(dsSigner);
        }
        recipients.setSigners(dsSigners);
        envelope.setRecipients(recipients);

        return envelope;
    }

    /**
     * Creates signature tabs for documents.
     * Uses anchor strings to position signature fields.
     *
     * @return Tabs object with signature fields
     */
    private Tabs createSignatureTabs() {
        Tabs tabs = new Tabs();
        List<SignHere> signHereTabs = new ArrayList<>();
        
        // Common signature anchor strings
        SignHere signHere1 = new SignHere();
        signHere1.setAnchorString("/sn1/");
        signHere1.setAnchorUnits("pixels");
        signHere1.setAnchorYOffset(String.valueOf(ANCHOR_OFFSET_Y));
        signHere1.setAnchorXOffset(String.valueOf(ANCHOR_OFFSET_X));
        signHereTabs.add(signHere1);
        
        // Alternative anchor string
        SignHere signHere2 = new SignHere();
        signHere2.setAnchorString("**signature_1**");
        signHere2.setAnchorUnits("pixels");
        signHere2.setAnchorYOffset(String.valueOf(ANCHOR_OFFSET_Y));
        signHere2.setAnchorXOffset(String.valueOf(ANCHOR_OFFSET_X));
        signHereTabs.add(signHere2);
        
        tabs.setSignHereTabs(signHereTabs);
        return tabs;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }
}
