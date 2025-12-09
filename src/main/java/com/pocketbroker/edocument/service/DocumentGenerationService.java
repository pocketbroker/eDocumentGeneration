package com.pocketbroker.edocument.service;

import com.pocketbroker.edocument.api.DocumentGenerationException;
import com.pocketbroker.edocument.api.DocumentGenerator;
import com.pocketbroker.edocument.api.DocumentRequest;
import com.pocketbroker.edocument.api.SignableDocument;
import com.pocketbroker.edocument.config.DocumentGeneratorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for generating documents using partner-specific implementations.
 */
@Service
public class DocumentGenerationService {
    
    private final DocumentGeneratorFactory generatorFactory;
    
    @Autowired
    public DocumentGenerationService(DocumentGeneratorFactory generatorFactory) {
        this.generatorFactory = generatorFactory;
    }
    
    /**
     * Generates a signable document for the specified partner.
     * 
     * @param partnerName the name of the partner
     * @param request the document request
     * @return the generated SignableDocument
     * @throws DocumentGenerationException if document generation fails
     */
    public SignableDocument generateDocument(String partnerName, DocumentRequest request)
            throws DocumentGenerationException {
        
        DocumentGenerator generator = generatorFactory.getGenerator(partnerName);
        
        return generator.createDocument(
                request.getTemplateName(),
                request.getData(),
                request.getMetadata()
        );
    }
    
    /**
     * Validates a template for the specified partner.
     * 
     * @param partnerName the name of the partner
     * @param templateName the name of the template to validate
     * @return true if the template is valid, false otherwise
     */
    public boolean validateTemplate(String partnerName, String templateName) {
        if (!generatorFactory.hasGenerator(partnerName)) {
            return false;
        }
        
        DocumentGenerator generator = generatorFactory.getGenerator(partnerName);
        return generator.validateTemplate(templateName);
    }
}
