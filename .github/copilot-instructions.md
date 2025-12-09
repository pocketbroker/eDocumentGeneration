# GitHub Copilot Instructions for eDocumentGeneration

## Repository Overview
This repository handles the generation of electronically signable documents. When working on this codebase, prioritize security, document integrity, and compliance with electronic signature standards.

## Coding Standards

### General Guidelines
- Write clean, maintainable, and well-documented code
- Follow industry best practices for the language/framework being used
- Prioritize code readability and simplicity over clever solutions
- Add comments for complex logic or business rules

### Security Considerations
- **Never hardcode sensitive information** (API keys, credentials, certificates)
- Use environment variables or secure configuration management for sensitive data
- Validate and sanitize all inputs, especially document content and user data
- Follow secure coding practices for document generation and handling
- Be mindful of potential injection vulnerabilities when generating documents
- Ensure proper access controls for document generation and retrieval

### Document Generation
- Maintain document integrity and ensure generated documents are tamper-proof
- Follow relevant standards for electronic signatures (e.g., eIDAS, E-SIGN Act)
- Implement proper error handling for document generation failures
- Log document generation activities for audit trails
- Test document generation with various input scenarios

## Testing Guidelines
- Write unit tests for new functionality
- Include integration tests for document generation workflows
- Test edge cases and error conditions
- Verify document output format and integrity
- Test with various document types and content

## Code Review
- Ensure all changes maintain backward compatibility when possible
- Review security implications of document handling changes
- Verify compliance with electronic signature standards
- Check for proper error handling and logging

## Documentation
- Update README.md when adding new features or changing functionality
- Document API endpoints and their parameters
- Include examples for document generation usage
- Keep inline documentation current with code changes

## Commit Messages
- Use clear, descriptive commit messages
- Follow conventional commit format when applicable:
  - `feat:` for new features
  - `fix:` for bug fixes
  - `docs:` for documentation changes
  - `refactor:` for code refactoring
  - `test:` for test additions/changes
  - `chore:` for maintenance tasks
