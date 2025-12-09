package com.pocketbroker.edocument.config;

import com.pocketbroker.edocument.api.DocumentGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory for obtaining DocumentGenerator instances by partner name.
 * Uses dependency injection to discover all available implementations.
 */
@Component
public class DocumentGeneratorFactory {
    
    private final Map<String, DocumentGenerator> generators;
    
    @Autowired
    public DocumentGeneratorFactory(List<DocumentGenerator> generatorList) {
        this.generators = generatorList.stream()
                .collect(Collectors.toMap(
                        DocumentGenerator::getPartnerName,
                        Function.identity()
                ));
    }
    
    /**
     * Gets a DocumentGenerator for the specified partner.
     * 
     * @param partnerName the name of the partner
     * @return the DocumentGenerator instance for the partner
     * @throws IllegalArgumentException if no generator exists for the partner
     */
    public DocumentGenerator getGenerator(String partnerName) {
        DocumentGenerator generator = generators.get(partnerName);
        if (generator == null) {
            throw new IllegalArgumentException("No document generator found for partner: " + partnerName);
        }
        return generator;
    }
    
    /**
     * Checks if a generator exists for the specified partner.
     * 
     * @param partnerName the name of the partner
     * @return true if a generator exists, false otherwise
     */
    public boolean hasGenerator(String partnerName) {
        return generators.containsKey(partnerName);
    }
    
    /**
     * Gets all available partner names.
     * 
     * @return a list of all available partner names
     */
    public List<String> getAvailablePartners() {
        return List.copyOf(generators.keySet());
    }
}
