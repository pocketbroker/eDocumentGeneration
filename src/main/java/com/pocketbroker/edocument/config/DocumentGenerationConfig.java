package com.pocketbroker.edocument.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for document generation.
 * Enables component scanning to discover partner-specific implementations.
 */
@Configuration
@ComponentScan(basePackages = "com.pocketbroker.edocument")
public class DocumentGenerationConfig {
    // Component scanning will automatically discover and register
    // DocumentGenerator implementations annotated with @Component
}
