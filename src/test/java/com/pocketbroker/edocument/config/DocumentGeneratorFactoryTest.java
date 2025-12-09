package com.pocketbroker.edocument.config;

import com.pocketbroker.edocument.api.DocumentGenerator;
import com.pocketbroker.edocument.partner.PartnerADocumentGenerator;
import com.pocketbroker.edocument.partner.PartnerBDocumentGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocumentGeneratorFactoryTest {
    
    private DocumentGeneratorFactory factory;
    
    @BeforeEach
    void setUp() {
        List<DocumentGenerator> generators = Arrays.asList(
                new PartnerADocumentGenerator(),
                new PartnerBDocumentGenerator()
        );
        factory = new DocumentGeneratorFactory(generators);
    }
    
    @Test
    void testGetGenerator_PartnerA() {
        DocumentGenerator generator = factory.getGenerator("Partner A");
        assertNotNull(generator);
        assertEquals("Partner A", generator.getPartnerName());
    }
    
    @Test
    void testGetGenerator_PartnerB() {
        DocumentGenerator generator = factory.getGenerator("Partner B");
        assertNotNull(generator);
        assertEquals("Partner B", generator.getPartnerName());
    }
    
    @Test
    void testGetGenerator_NonExistent() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.getGenerator("Partner C");
        });
    }
    
    @Test
    void testHasGenerator() {
        assertTrue(factory.hasGenerator("Partner A"));
        assertTrue(factory.hasGenerator("Partner B"));
        assertFalse(factory.hasGenerator("Partner C"));
    }
    
    @Test
    void testGetAvailablePartners() {
        List<String> partners = factory.getAvailablePartners();
        assertEquals(2, partners.size());
        assertTrue(partners.contains("Partner A"));
        assertTrue(partners.contains("Partner B"));
    }
}
