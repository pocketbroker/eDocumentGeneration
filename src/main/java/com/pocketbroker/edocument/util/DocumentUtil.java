package com.pocketbroker.edocument.util;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Utility class for document operations.
 */
public class DocumentUtil {

    private DocumentUtil() {
        // Utility class
    }

    /**
     * Reads a file from the filesystem and returns its content as a byte array.
     *
     * @param filePath the path to the file
     * @return byte array containing the file content
     * @throws IOException if an error occurs reading the file
     */
    public static byte[] readFileToBytes(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }

    /**
     * Encodes byte array content to Base64 string.
     *
     * @param content the byte array to encode
     * @return Base64 encoded string
     */
    public static String encodeToBase64(byte[] content) {
        return Base64.getEncoder().encodeToString(content);
    }

    /**
     * Gets the file extension from a file path.
     *
     * @param filePath the file path
     * @return the file extension (e.g., "pdf", "docx")
     */
    public static String getFileExtension(String filePath) {
        return FilenameUtils.getExtension(filePath);
    }

    /**
     * Gets the file name without extension from a file path.
     *
     * @param filePath the file path
     * @return the file name without extension
     */
    public static String getFileNameWithoutExtension(String filePath) {
        return FilenameUtils.getBaseName(filePath);
    }

    /**
     * Validates that a file exists and is readable.
     *
     * @param filePath the path to the file
     * @throws IllegalArgumentException if the file doesn't exist or isn't readable
     */
    public static void validateFile(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File does not exist: " + filePath);
        }
        if (!Files.isReadable(path)) {
            throw new IllegalArgumentException("File is not readable: " + filePath);
        }
        if (Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path is a directory, not a file: " + filePath);
        }
    }
}
