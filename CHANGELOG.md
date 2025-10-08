# Changelog

All notable changes to Luma Script will be documented in this file.

## [1.0.0] - 2025-01-08

### Added
- ✨ Initial release of Luma Script library
- 🏗️ **ScriptBuilder** - Fluent builder pattern for easy script creation
- 📦 Core script execution engine with step management
- ⏱️ Time-based and tick-based delay steps
- 🔀 Conditional and branching steps (ConditionalStep, BranchStep)
- ⚡ Parallel and sequential execution (ParallelStep, SequenceStep)
- 🔁 Loop strategies (FiniteLoopStrategy, InfiniteLoopStrategy)
- 🎯 Advanced steps (TimeoutStep, CancelableStep, ChainStep, CompositeStep)
- 🔄 RepeatStep for iteration control
- ⏳ WaitUntilStep for condition waiting
- 🎮 ScriptManager for centralized script lifecycle management
- 📝 Comprehensive Javadoc documentation
- 🧪 Full unit test coverage with JUnit 5
- 📖 Detailed README with examples and API documentation

### Fixed
- 🐛 **CRITICAL**: Fixed ScriptTime.finished() logic (was `millis - delay >= start`, now `millis >= start + delay`)
- 🛡️ Added null-safety checks to all constructors with Objects.requireNonNull()
- 🔒 Added validation for negative delays and tick counts
- ⚠️ Added empty list validation for ParallelStep and SequenceStep
- 🔧 Fixed TimedStep to use internal timer instead of modifying shared ScriptTime
- 🧹 Removed auto-cleanup from isFinished() - now explicit with needsCleanup()

### Enhanced
- 💪 Added Script.reset() method for reusing scripts
- 🎨 Improved error messages for all validation exceptions
- 📚 Added CONTRIBUTING.md with development guidelines
- 🔐 Added proper null-safety throughout the codebase
- 🏷️ Better type safety and parameter validation

### Documentation
- 📖 Complete README with 9 comprehensive examples
- 📝 Javadoc for all public APIs
- 🤝 Contributing guidelines
- ⚖️ MIT License
- 🔧 GitHub Actions CI/CD workflows
- 🔒 CodeQL security analysis

## [Unreleased]

### Planned Features
- Async step execution support
- Event-based triggers
- Script debugging utilities
- Performance profiling tools
- Script serialization/deserialization
- Visual script editor integration

---

**Legend:**
- ✨ New features
- 🐛 Bug fixes  
- 🔒 Security
- 📝 Documentation
- ♻️ Refactoring
- ⚡ Performance
- 🎨 UI/UX

