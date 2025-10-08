# Contributing to Luma Script

Thank you for your interest in contributing to Luma Script! This document provides guidelines and instructions for contributing.

## 🤝 How to Contribute

### Reporting Bugs

Before creating bug reports, please check the existing issues to avoid duplicates. When you create a bug report, include as many details as possible:

- **Use a clear and descriptive title**
- **Describe the exact steps to reproduce the problem**
- **Provide specific examples** (code snippets, logs)
- **Describe the behavior you observed and what you expected**
- **Include your environment details** (Java version, OS, etc.)

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion:

- **Use a clear and descriptive title**
- **Provide a detailed description of the suggested enhancement**
- **Explain why this enhancement would be useful**
- **List some examples of how it would be used**

### Pull Requests

1. Fork the repository
2. Create a new branch from `develop`:
   ```bash
   git checkout -b feature/amazing-feature develop
   ```

3. Make your changes following the coding standards:
   - Add Javadoc for all public methods and classes
   - Follow Java naming conventions
   - Keep methods focused and concise
   - Add unit tests for new functionality

4. Ensure tests pass:
   ```bash
   mvn test
   ```

5. Commit your changes:
   ```bash
   git commit -m "Add amazing feature"
   ```

6. Push to your fork:
   ```bash
   git push origin feature/amazing-feature
   ```

7. Open a Pull Request to the `develop` branch

## 📝 Coding Standards

### Java Code Style

- **Java Version**: Java 21
- **Indentation**: 4 spaces (no tabs)
- **Line Length**: Maximum 120 characters
- **Naming**:
  - Classes: `PascalCase`
  - Methods/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`

### Javadoc

All public classes, interfaces, and methods must have Javadoc:

```java
/**
 * Brief description of what this does.
 * 
 * @param paramName description of parameter
 * @return description of return value
 * @throws ExceptionType when this exception occurs
 */
public ReturnType methodName(ParamType paramName) {
    // implementation
}
```

### Testing

- Write unit tests for all new features
- Maintain or improve code coverage
- Use JUnit 5 for testing
- Test names should be descriptive: `testMethodName_Scenario_ExpectedResult`

Example:
```java
@Test
void testAddStep_WithValidStep_AddsToStepList() {
    // Arrange
    Script script = new Script();
    IScriptStep step = new CallbackStep(() -> {});
    
    // Act
    script.addStep(step);
    
    // Assert
    assertFalse(script.isFinished());
}
```

## 🔄 Development Workflow

1. **develop** - Main development branch
2. **main** - Stable release branch
3. **feature/** - New features
4. **bugfix/** - Bug fixes
5. **hotfix/** - Urgent production fixes

## ✅ Checklist Before Submitting PR

- [ ] Code follows the project's style guidelines
- [ ] Self-review of code completed
- [ ] Code is commented, particularly in complex areas
- [ ] Javadoc added for public APIs
- [ ] Unit tests added/updated
- [ ] All tests pass locally
- [ ] No new warnings introduced
- [ ] Documentation updated (README, etc.)

## 📋 Commit Message Guidelines

Use clear and meaningful commit messages:

- `feat: add new feature X`
- `fix: resolve issue with Y`
- `docs: update README with Z`
- `test: add tests for component W`
- `refactor: improve code structure in V`
- `chore: update dependencies`

## 🐛 Debugging

For debugging contributions:

1. Enable verbose logging
2. Use the Example.java as a test bed
3. Add breakpoints in your IDE
4. Run with: `mvn exec:java -Dexec.mainClass="Example"`

## 📞 Questions?

- Open an issue with the `question` label
- Check existing issues and discussions
- Review the README and documentation

## 📜 License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

Thank you for contributing to Luma Script! 🎉

