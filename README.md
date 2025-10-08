# 🎬 Luma Script

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Maven Central](https://img.shields.io/badge/maven--central-1.0.0-brightgreen.svg)](https://search.maven.org/)

A flexible and powerful Java library for creating and managing script step sequences. Perfect for game development, animations, automation workflows, and any scenario requiring sequential or conditional task execution.

## ✨ Features

- **🔄 Sequential Execution** - Execute steps one after another with automatic progression
- **⏰ Time-Based Delays** - Support for millisecond-based and tick-based delays
- **🔀 Conditional Logic** - Branch, loop, and conditional step execution
- **⚡ Parallel Execution** - Run multiple steps simultaneously
- **🔁 Loop Strategies** - Finite and infinite looping with customizable behavior
- **🎯 Script Management** - Thread-safe centralized script lifecycle management
- **🧩 Composable Steps** - Build complex behaviors from simple building blocks
- **🧪 Well-Tested** - Comprehensive unit test coverage

## 📦 Installation

### Maven

```xml
<dependency>
    <groupId>dev.quivern</groupId>
    <artifactId>luma-script</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'dev.quivern:luma-script:1.0.0'
```

## 🚀 Quick Start

### Basic Script

```java
import dev.quivern.script.Script;
import dev.quivern.script.ScriptBuilder;
import dev.quivern.script.steps.advanced.CallbackStep;

// Traditional way
Script script = new Script()
    .addStep(new CallbackStep(() -> System.out.println("Step 1")))
    .waitDelayedStep(1000) // Wait 1 second
    .addStep(new CallbackStep(() -> System.out.println("Step 2")))
    .waitTickStep(5) // Wait 5 ticks
    .addStep(new CallbackStep(() -> System.out.println("Step 3")));

// Builder pattern (recommended)
Script builderScript = new ScriptBuilder()
    .then(() -> System.out.println("Step 1"))
    .waitMillis(1000)
    .then(() -> System.out.println("Step 2"))
    .waitTicks(5)
    .then(() -> System.out.println("Step 3"))
    .build();

// Game loop or update cycle
while (!script.isFinished()) {
    script.update();
    Thread.sleep(100); // Simulate frame time
}
```

### Script Manager

```java
import dev.quivern.script.ScriptManager;

ScriptManager manager = new ScriptManager();

// Create and manage multiple scripts
manager.getScript("player-intro").ifPresent(script -> 
    script.addStep(new CallbackStep(() -> System.out.println("Player spawned!")))
          .waitDelayedStep(2000)
          .addStep(new CallbackStep(() -> System.out.println("Welcome!")))
);

// Update all scripts each frame
manager.updateAll();
```

### Advanced Steps

#### Conditional Execution

```java
import dev.quivern.script.steps.advanced.ConditionalStep;

ConditionalStep step = new ConditionalStep(
    () -> player.getHealth() > 50,
    new CallbackStep(() -> System.out.println("Player is healthy!"))
);
```

#### Branching (If-Else)

```java
import dev.quivern.script.steps.advanced.BranchStep;

BranchStep branch = new BranchStep(
    () -> player.hasItem("key"),
    new CallbackStep(() -> openDoor()),
    new CallbackStep(() -> showLockedMessage())
);
```

#### Parallel Execution

```java
import dev.quivern.script.steps.advanced.ParallelStep;

ParallelStep parallel = new ParallelStep(Arrays.asList(
    new CallbackStep(() -> playSound("explosion")),
    new CallbackStep(() -> spawnParticles()),
    new CallbackStep(() -> applyDamage())
));
```

#### Sequential Steps

```java
import dev.quivern.script.steps.advanced.SequenceStep;

SequenceStep sequence = new SequenceStep(Arrays.asList(
    new DelayedStep(500, () -> System.out.println("Phase 1")),
    new DelayedStep(500, () -> System.out.println("Phase 2")),
    new CallbackStep(() -> System.out.println("Complete!"))
));
```

### Loop Strategies

#### Finite Loop

```java
import dev.quivern.script.strategy.FiniteLoopStrategy;

Script script = new Script()
    .addStep(new CallbackStep(() -> System.out.println("Loop iteration")))
    .setLoopStrategy(new FiniteLoopStrategy(5)); // Repeat 5 times
```

#### Infinite Loop

```java
import dev.quivern.script.strategy.InfiniteLoopStrategy;

Script script = new Script()
    .addStep(new TimedTickStep(10, () -> checkForInput()))
    .setLoopStrategy(new InfiniteLoopStrategy()); // Loop forever

// Stop with interrupt
script.setInterrupt(true);
```

## 🏗️ ScriptBuilder

The `ScriptBuilder` provides a fluent, readable API for creating scripts:

```java
Script script = new ScriptBuilder()
    .then(() -> System.out.println("Start"))
    .waitMillis(500)
    .when(() -> player.isAlive(), 
         new CallbackStep(() -> heal(player)))
    .waitUntil(() -> enemy.isDead())
    .then(() -> victory())
    .loop(3)  // Repeat 3 times
    .build();
```

### Builder Methods

| Method | Description |
|--------|-------------|
| `then(Runnable)` | Execute callback once |
| `waitMillis(long)` | Time-based delay |
| `waitTicks(int)` | Tick-based delay |
| `when(condition, step)` | Conditional execution |
| `branch(condition, ifStep, elseStep)` | If-else logic |
| `waitUntil(condition)` | Wait for condition |
| `loop(int)` | Finite loop |
| `loopForever()` | Infinite loop |
| `addStep(step)` | Add custom step |
| `build()` | Get the script |

## 📚 Core Components

### IScriptStep

Base interface for all steps:
- `void perform(ScriptTime time)` - Execute step logic
- `boolean isFinished()` - Check if step is complete
- `void reset()` - Reset step to initial state

### Built-in Steps

| Step | Description |
|------|-------------|
| `CallbackStep` | Execute a simple callback once |
| `DelayedStep` | Wait for time delay, then execute |
| `DelayedTickStep` | Wait for tick count, then execute |
| `TimedStep` | Execute action repeatedly for duration |
| `TimedTickStep` | Execute action for N ticks |
| `ConditionalStep` | Execute step only when condition is true |
| `BranchStep` | If-else logic with two steps |
| `ParallelStep` | Execute multiple steps simultaneously |
| `SequenceStep` | Execute steps one after another |
| `RepeatStep` | Repeat a step N times |
| `WaitUntilStep` | Wait until condition becomes true |
| `CancelableStep` | Step that can be cancelled by condition |
| `TimeoutStep` | Wrap step with timeout |
| `MultiConditionStep` | Execute when all conditions are true |
| `ChainStep` | Dynamically add steps from supplier |
| `CompositeStep` | Custom completion logic for multiple steps |

### Script Manager

Thread-safe manager for multiple scripts:

```java
ScriptManager manager = new ScriptManager();

// Get or create script
Optional<Script> script = manager.getScript("my-script");

// Add custom script
manager.addScript("custom", new Script());

// Update specific script
manager.updateScript("my-script");

// Update with condition
manager.updateScript("my-script", () -> player.isAlive());

// Update all scripts
manager.updateAll();

// Cleanup
manager.cleanupScript("my-script");
manager.cleanupAll();
manager.clearAll();
```

## 🛠️ Development

### Prerequisites

- Java 21+
- Maven 3.8+

### Build

```bash
mvn clean install
```

### Run Tests

```bash
mvn test
```

### Run Example

```bash
mvn exec:java -Dexec.mainClass="dev.quivern.script.example.Example"
```

### Generate Javadoc

```bash
mvn javadoc:javadoc
```

## 📊 Project Structure

```
luma-script/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── dev/quivern/script/
│   │   │       ├── api/              # Core interfaces
│   │   │       ├── steps/            # Basic steps
│   │   │       │   └── advanced/     # Advanced steps
│   │   │       ├── strategy/         # Loop strategies
│   │   │       ├── Script.java       # Main script class
│   │   │       └── ScriptManager.java
│   │   └── resources/
│   └── test/
│       └── java/                     # JUnit 5 tests
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## 🧪 Testing

The library includes comprehensive test coverage:

- `ScriptTest` - Core script functionality
- `ScriptManagerTest` - Manager operations
- `StepsTest` - All step implementations

Run with:

```bash
mvn test
```

## 🚨 Known Limitations

1. **Thread Safety**: While `ScriptManager` is thread-safe, individual `Script` instances are not. Don't share scripts across threads without external synchronization.
2. **Time Precision**: Time-based delays use `System.currentTimeMillis()`, which has ~15ms precision on some systems. For high-precision timing, consider using `System.nanoTime()` with custom steps.
3. **Memory Management**: 
   - Infinite loops don't auto-cleanup. Use `script.needsCleanup()` and `cleanup()` when appropriate.
   - ChainStep with unbounded suppliers can grow indefinitely. Implement bounds in your supplier logic.
4. **Recursion**: Deep step nesting (>1000 levels) may cause stack overflow. Keep hierarchies reasonable or use iterative alternatives.
5. **Validation**: 
   - Negative delays/ticks throw `IllegalArgumentException`
   - Null parameters throw `NullPointerException`
   - Empty step lists throw `IllegalArgumentException`

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Inspired by Unity's coroutines and animation systems
- Built with modern Java 21 features
- Powered by Project Lombok for cleaner code

## 📬 Contact

- **Author**: Quivern
- **GitHub**: [@quivern](https://github.com/quivern)
- **Issues**: [GitHub Issues](https://github.com/quivern/luma-script/issues)

---

**⭐ If you find this library useful, please consider giving it a star!**

