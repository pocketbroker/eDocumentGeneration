package com.pocketbroker.edocument.service;

import com.pocketbroker.edocument.model.Document;
import com.pocketbroker.edocument.model.EnvelopeConfig;
import com.pocketbroker.edocument.model.Signer;
import com.pocketbroker.edocument.util.DocumentUtil;

import java.io.IOException;

/**
 * Service class for generating electronic documents.
 * This class provides methods to create and configure documents for electronic signing.
 */
public class DocumentGenerator {

    /**
     * Creates a document from a file path.
     *
     * @param filePath the path to the document file
     * @param documentName the name of the document
     * @return a Document object
     * @throws IOException if an error occurs reading the file
     */
    public Document createDocumentFromFile(String filePath, String documentName) throws IOException {
        DocumentUtil.validateFile(filePath);
        
        byte[] content = DocumentUtil.readFileToBytes(filePath);
        String extension = DocumentUtil.getFileExtension(filePath);
        
        Document document = new Document();
        document.setName(documentName);
        document.setFilePath(filePath);
        document.setContent(content);
        document.setFileExtension(extension);
        
        return document;
    }

    /**
     * Creates a document from byte array content.
     *
     * @param content the document content as byte array
     * @param documentName the name of the document
     * @param fileExtension the file extension (e.g., "pdf", "docx")
     * @return a Document object
     */
    public Document createDocumentFromBytes(byte[] content, String documentName, String fileExtension) {
        if (content == null || content.length == 0) {
            throw new IllegalArgumentException("Document content cannot be null or empty");
        }
        
        Document document = new Document();
        document.setName(documentName);
        document.setContent(content);
        document.setFileExtension(fileExtension);
        
        return document;
    }

    /**
     * Creates an envelope configuration for sending documents for signature.
     *
     * @param emailSubject the subject of the email sent to signers
     * @return an EnvelopeConfig object
     */
    public EnvelopeConfig createEnvelopeConfig(String emailSubject) {
        return new EnvelopeConfig(emailSubject);
    }

    /**
     * Creates a signer object.
     *
     * @param email the signer's email address
     * @param name the signer's full name
     * @return a Signer object
     */
    public Signer createSigner(String email, String name) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Signer email cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Signer name cannot be null or empty");
        }
        
        return new Signer(email, name);
    }

    /**
     * Creates a signer object for embedded signing.
     *
     * @param email the signer's email address
     * @param name the signer's full name
     * @param clientUserId a unique identifier for the signer in your system
     * @return a Signer object configured for embedded signing
     */
    public Signer createEmbeddedSigner(String email, String name, String clientUserId) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Signer email cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Signer name cannot be null or empty");
        }
        if (clientUserId == null || clientUserId.isEmpty()) {
            throw new IllegalArgumentException("Client User ID cannot be null or empty");
        }
        
        return new Signer(email, name, clientUserId);
    }
}
