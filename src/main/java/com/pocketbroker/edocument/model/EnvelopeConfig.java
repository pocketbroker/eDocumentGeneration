package com.pocketbroker.edocument.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for an envelope that contains documents to be signed.
 */
public class EnvelopeConfig {
    private String emailSubject;
    private String emailBody;
    private List<Document> documents;
    private List<Signer> signers;
    private String status;

    public EnvelopeConfig() {
        this.documents = new ArrayList<>();
        this.signers = new ArrayList<>();
        this.status = "sent";
        this.emailSubject = "Please sign this document";
    }

    public EnvelopeConfig(String emailSubject) {
        this();
        this.emailSubject = emailSubject;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void addDocument(Document document) {
        this.documents.add(document);
    }

    public List<Signer> getSigners() {
        return signers;
    }

    public void setSigners(List<Signer> signers) {
        this.signers = signers;
    }

    public void addSigner(Signer signer) {
        this.signers.add(signer);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EnvelopeConfig{" +
                "emailSubject='" + emailSubject + '\'' +
                ", emailBody='" + emailBody + '\'' +
                ", documents=" + documents.size() +
                ", signers=" + signers.size() +
                ", status='" + status + '\'' +
                '}';
    }
}
