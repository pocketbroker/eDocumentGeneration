# eDocument Generation

A Java library for generating electronically signable documents with partner-specific implementations wired through dependency injection.

## Overview

This project provides a flexible framework for creating signable electronic documents. It exposes clean interfaces for document generation while supporting multiple partner-specific implementations through Spring Framework dependency injection.

## Architecture

### Core Components

- **API Layer** (`com.pocketbroker.edocument.api`): Public interfaces and models
  - `DocumentGenerator`: Interface for generating documents
  - `SignableDocument`: Interface representing a signable document
  - `DocumentRequest`: Builder-pattern request object
  - `DocumentGenerationException`: Exception for generation failures

- **Partner Implementation Layer** (`com.pocketbroker.edocument.partner`): Partner-specific implementations
  - `PartnerADocumentGenerator`: Implementation for Partner A
  - `PartnerBDocumentGenerator`: Implementation for Partner B
  - `DefaultSignableDocument`: Standard implementation of SignableDocument

- **Configuration Layer** (`com.pocketbroker.edocument.config`): Dependency injection setup
  - `DocumentGenerationConfig`: Spring configuration with component scanning
  - `DocumentGeneratorFactory`: Factory for retrieving partner-specific generators

- **Service Layer** (`com.pocketbroker.edocument.service`): Business logic
  - `DocumentGenerationService`: Service for generating documents using the factory

## Usage

### Basic Document Generation

```java
// Initialize Spring context
ApplicationContext context = new AnnotationConfigApplicationContext(DocumentGenerationConfig.class);
DocumentGenerationService service = context.getBean(DocumentGenerationService.class);

// Create a document request
DocumentRequest request = DocumentRequest.builder()
    .templateName("loan-agreement")
    .addData("customerName", "John Doe")
    .addData("amount", 10000.00)
    .addMetadata("documentType", "loan")
    .build();

// Generate document for specific partner
SignableDocument document = service.generateDocument("Partner A", request);

// Use the document
String documentId = document.getDocumentId();
byte[] content = document.getContent();
String type = document.getDocumentType();
boolean ready = document.isReadyForSigning();
```

### Template Validation

```java
// Validate template for specific partner
boolean isValid = service.validateTemplate("Partner A", "loan-agreement");
```

### Adding New Partner Implementations

1. Create a new class implementing `DocumentGenerator`
2. Annotate with `@Component`
3. Implement the required methods:
   - `createDocument()`: Generate the document
   - `validateTemplate()`: Validate template names
   - `getPartnerName()`: Return unique partner identifier

```java
@Component
public class PartnerCDocumentGenerator implements DocumentGenerator {
    
    @Override
    public SignableDocument createDocument(String templateName, 
                                          Map<String, Object> data,
                                          Map<String, String> metadata) {
        // Partner C specific implementation
    }
    
    @Override
    public boolean validateTemplate(String templateName) {
        // Partner C specific validation
    }
    
    @Override
    public String getPartnerName() {
        return "Partner C";
    }
}
```

## Building and Testing

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Build

```bash
mvn clean compile
```

### Run Tests

```bash
mvn test
```

### Package

```bash
mvn package
```

## Dependency Injection

The project uses Spring Framework for dependency injection:

- **Component Scanning**: Automatically discovers all `DocumentGenerator` implementations
- **Factory Pattern**: `DocumentGeneratorFactory` provides access to generators by partner name
- **Loose Coupling**: Business logic depends only on interfaces, not concrete implementations

## Partner Implementations

### Partner A

- **Template Format**: Alphanumeric with hyphens and underscores (e.g., `loan-agreement`)
- **Document ID Format**: Standard UUID
- **Document Type**: PDF

### Partner B

- **Template Format**: Must start with `PB_` prefix (e.g., `PB_contract`)
- **Document ID Format**: `PB-` prefix with UUID
- **Document Type**: PDF

## Testing

The project includes comprehensive tests:

- **Unit Tests**: Individual component testing
- **Integration Tests**: Full Spring context with dependency injection
- **Test Coverage**: 34 tests covering all major components

## License

Copyright © 2025 PocketBroker
