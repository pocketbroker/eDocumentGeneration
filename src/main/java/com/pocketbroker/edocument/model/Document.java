package com.pocketbroker.edocument.model;

/**
 * Represents a document to be signed electronically.
 */
public class Document {
    private String name;
    private String filePath;
    private byte[] content;
    private String fileExtension;
    private String documentId;

    public Document() {
    }

    public Document(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

    public Document(String name, byte[] content, String fileExtension) {
        this.name = name;
        this.content = content;
        this.fileExtension = fileExtension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Override
    public String toString() {
        return "Document{" +
                "name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", documentId='" + documentId + '\'' +
                '}';
    }
}
