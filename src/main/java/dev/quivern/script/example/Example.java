package dev.quivern.script.example;

import dev.quivern.script.Script;
import dev.quivern.script.ScriptManager;
import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.steps.DelayedStep;
import dev.quivern.script.steps.DelayedTickStep;
import dev.quivern.script.steps.TimedStep;
import dev.quivern.script.steps.TimedTickStep;
import dev.quivern.script.steps.advanced.*;
import dev.quivern.script.strategy.FiniteLoopStrategy;
import dev.quivern.script.strategy.InfiniteLoopStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates the Luma Script library usage with comprehensive examples.
 */
@SuppressWarnings("BusyWait")
public class Example {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   Luma Script Library - Full Demo      ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        basicScriptExample();
        printDivider();

        scriptManagerExample();
        printDivider();

        advancedStepsExample();
        printDivider();

        loopingExample();
        printDivider();

        timeBasedExample();
        printDivider();

        compositeAndChainExample();
        printDivider();

        timeoutAndCancellationExample();
        printDivider();

        realWorldGameExample();
        printDivider();

        complexWorkflowExample();

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║      All Examples Completed! ✓         ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private static void printDivider() {
        System.out.println("\n" + "=".repeat(50) + "\n");
    }

    /**
     * Basic script with delays and actions.
     */
    private static void basicScriptExample() {
        System.out.println("1. Basic Script Example:");

        Script script = new Script()
                .addStep(new CallbackStep(() -> System.out.println("  Step 1: Starting...")))
                .waitDelayedStep(500)
                .addStep(new CallbackStep(() -> System.out.println("  Step 2: After 500ms delay")))
                .waitTickStep(3)
                .addStep(new CallbackStep(() -> System.out.println("  Step 3: After 3 ticks")));

        while (!script.isFinished()) {
            script.update();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("  ✓ Script completed");
    }

    /**
     * Using ScriptManager for multiple scripts.
     */
    private static void scriptManagerExample() {
        System.out.println("2. Script Manager Example:");
        ScriptManager manager = new ScriptManager();

        Script greetScript = new Script()
                .addStep(new CallbackStep(() -> System.out.println("  Hello from managed script!")));

        manager.addScript("greet", greetScript);

        manager.getScript("countdown").ifPresent(script ->
                script.addStep(new TimedTickStep(3, () -> System.out.print("  Tick... ")))
                        .addStep(new CallbackStep(() -> System.out.println("\n  Countdown done!")))
        );

        int ticks = 0;
        while (!manager.finished("greet") || !manager.finished("countdown")) {
            manager.updateAll();
            ticks++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("  ✓ All scripts completed in " + ticks + " ticks");
    }

    /**
     * Advanced steps: conditions, branches, parallel execution.
     */
    private static void advancedStepsExample() {
        System.out.println("3. Advanced Steps Example:");
        AtomicInteger value = new AtomicInteger(5);

        IScriptStep conditionalStep = new ConditionalStep(
                () -> value.get() > 3,
                new CallbackStep(() -> System.out.println("  Value > 3: " + value.get()))
        );

        IScriptStep branchStep = new BranchStep(
                () -> value.get() % 2 == 0,
                new CallbackStep(() -> System.out.println("  Value is even: " + value.get())),
                new CallbackStep(() -> System.out.println("  Value is odd: " + value.get()))
        );

        List<IScriptStep> parallelSteps = Arrays.asList(
                new CallbackStep(() -> System.out.println("  Parallel task 1")),
                new CallbackStep(() -> System.out.println("  Parallel task 2")),
                new CallbackStep(() -> System.out.println("  Parallel task 3"))
        );

        Script script = new Script()
                .addStep(conditionalStep)
                .addStep(branchStep)
                .addStep(new ParallelStep(parallelSteps))
                .addStep(new CallbackStep(() -> System.out.println("  ✓ Advanced demo complete")));

        while (!script.isFinished()) {
            script.update();
        }
    }

    /**
     * Looping scripts with different strategies.
     */
    private static void loopingExample() {
        System.out.println("4. Looping Example:");
        AtomicInteger loopCount = new AtomicInteger(0);

        Script finiteLoop = new Script()
                .addStep(new CallbackStep(() -> System.out.println("  Loop iteration: " + loopCount.incrementAndGet())))
                .setLoopStrategy(new FiniteLoopStrategy(3));

        while (!finiteLoop.isFinished()) {
            finiteLoop.update();
        }
        System.out.println("  ✓ Completed 3 iterations");

        loopCount.set(0);
        Script infiniteLoop = new Script();
        infiniteLoop.addStep(new CallbackStep(() -> {
                    int count = loopCount.incrementAndGet();
                    System.out.println("  Infinite loop: " + count);
                    if (count >= 2) infiniteLoop.setInterrupt(true);
                }))
                .setLoopStrategy(new InfiniteLoopStrategy());

        while (!infiniteLoop.isFinished() && !infiniteLoop.isInterrupt()) {
            infiniteLoop.update();
        }
        System.out.println("  ✓ Interrupted after 2 iterations");
    }

    /**
     * Time-based delays and timed execution.
     */
    private static void timeBasedExample() {
        System.out.println("5. Time-Based Steps Example:");

        long startTime = System.currentTimeMillis();
        AtomicInteger tickCount = new AtomicInteger(0);

        Script script = new Script()
                .addStep(new CallbackStep(() -> System.out.println("  Starting timed sequence...")))
                .addStep(new DelayedStep(200, () ->
                        System.out.println("  Action after 200ms delay")))
                .addStep(new TimedStep(300, () -> {
                    if (tickCount.incrementAndGet() % 2 == 0) {
                        System.out.println("  Executing during 300ms window... tick " + tickCount.get());
                    }
                }))
                .addStep(new CallbackStep(() -> {
                    long elapsed = System.currentTimeMillis() - startTime;
                    System.out.println("  ✓ Sequence completed in ~" + elapsed + "ms");
                }));

        while (!script.isFinished()) {
            script.update();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Composite steps and chain steps.
     */
    private static void compositeAndChainExample() {
        System.out.println("6. Composite & Chain Steps Example:");

        // Composite step - custom completion logic
        List<IScriptStep> tasks = Arrays.asList(
                new DelayedTickStep(2, () -> System.out.println("  Task 1 executing...")),
                new DelayedTickStep(3, () -> System.out.println("  Task 2 executing...")),
                new DelayedTickStep(1, () -> System.out.println("  Task 3 executing..."))
        );

        CompositeStep composite = new CompositeStep(
                tasks,
                finishedList -> finishedList.stream().filter(f -> f).count() >= 2 // Finish when 2 are done
        );

        System.out.println("  Composite: Waiting for at least 2 tasks to complete...");
        while (!composite.isFinished()) {
            composite.perform(null);
        }
        System.out.println("  ✓ Composite step completed");

        // Chain step - dynamic step generation
        AtomicInteger stepNumber = new AtomicInteger(0);
        ChainStep chain = new ChainStep(
                List.of(new CallbackStep(() -> System.out.println("  Chain: Initial step"))),
                () -> {
                    int num = stepNumber.incrementAndGet();
                    if (num <= 3) {
                        return new CallbackStep(() -> System.out.println("  Chain: Dynamic step " + num));
                    }
                    return null; // Stop chain
                }
        );

        while (!chain.isFinished()) {
            chain.perform(null);
        }
        System.out.println("  ✓ Chain completed with " + stepNumber.get() + " dynamic steps");
    }

    /**
     * Timeout and cancellation examples.
     */
    private static void timeoutAndCancellationExample() {
        System.out.println("7. Timeout & Cancellation Example:");

        // Timeout example
        AtomicBoolean taskCompleted = new AtomicBoolean(false);
        IScriptStep longTask = new WaitUntilStep(taskCompleted::get);
        TimeoutStep timeoutStep = new TimeoutStep(longTask, 200);

        System.out.println("  Starting task with 200ms timeout...");
        long start = System.currentTimeMillis();
        while (!timeoutStep.isFinished()) {
            timeoutStep.perform(null);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("  ✓ Task timed out after ~" + elapsed + "ms");

        // Cancellation example
        AtomicBoolean shouldCancel = new AtomicBoolean(false);
        AtomicInteger cancelCounter = new AtomicInteger(0);

        CancelableStep cancelable = new CancelableStep(
                new RepeatStep(
                        new CallbackStep(() -> System.out.println("  Working... " + cancelCounter.incrementAndGet())),
                        5
                ),
                shouldCancel::get
        );

        while (!cancelable.isFinished()) {
            cancelable.perform(null);
            if (cancelCounter.get() == 2) {
                shouldCancel.set(true);
                System.out.println("  ⚠ Cancellation triggered!");
            }
        }
        System.out.println("  ✓ Task cancelled after " + cancelCounter.get() + " iterations");
    }

    /**
     * Real-world game scenario example.
     */
    private static void realWorldGameExample() {
        System.out.println("8. Real-World Game Scenario:");
        System.out.println("  Simulating player respawn sequence...\n");

        AtomicInteger health = new AtomicInteger(0);
        AtomicBoolean invulnerable = new AtomicBoolean(false);

        Script respawnScript = new Script()
                .addStep(new CallbackStep(() -> {
                    System.out.println("  💀 Player died!");
                    health.set(0);
                }))
                .waitDelayedStep(1000)
                .addStep(new CallbackStep(() -> System.out.println("  🔄 Respawning...")))
                .waitDelayedStep(500)
                .addStep(new CallbackStep(() -> {
                    health.set(100);
                    invulnerable.set(true);
                    System.out.println("  ✨ Player respawned with " + health.get() + " HP");
                    System.out.println("  🛡 Invulnerability activated");
                }))
                .addStep(new RepeatStep(new DelayedStep(200, () -> {
                    // Blink effect during invulnerability
                    System.out.print("  ⚡");
                }), 5))
                .addStep(new CallbackStep(() -> {
                    invulnerable.set(false);
                    System.out.println("\n  🎯 Invulnerability ended - player vulnerable");
                }))
                .addStep(new CallbackStep(() -> System.out.println("  🛡 Invulnerability: " + invulnerable.get())));

        long startTime = System.currentTimeMillis();
        while (!respawnScript.isFinished()) {
            respawnScript.update();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("  ✓ Respawn sequence completed in " + totalTime + "ms");
    }

    /**
     * Complex workflow with multiple conditions and branches.
     */
    private static void complexWorkflowExample() {
        System.out.println("9. Complex Workflow Example:");
        System.out.println("  Simulating quest completion with multiple outcomes...\n");

        AtomicInteger questProgress = new AtomicInteger(0);
        AtomicBoolean hasKey = new AtomicBoolean(false);
        AtomicBoolean bossDefeated = new AtomicBoolean(false);

        // Multi-condition step
        MultiConditionStep questStart = new MultiConditionStep(
                Arrays.asList(
                        () -> questProgress.get() == 0,
                        () -> !bossDefeated.get()
                ),
                new CallbackStep(() -> {
                    questProgress.set(1);
                    System.out.println("  📜 Quest started: Find the key and defeat the boss");
                })
        );

        // Sequence of quest steps
        SequenceStep questSteps = new SequenceStep(Arrays.asList(
                new CallbackStep(() -> {
                    hasKey.set(true);
                    questProgress.set(2);
                    System.out.println("  🗝 Found the magical key!");
                }),
                new DelayedTickStep(2, () -> {
                }),
                new BranchStep(
                        hasKey::get,
                        new CallbackStep(() -> {
                            bossDefeated.set(true);
                            questProgress.set(3);
                            System.out.println("  ⚔ Used the key to unlock boss chamber");
                            System.out.println("  🐉 Boss defeated!");
                        }),
                        new CallbackStep(() -> System.out.println("  ❌ Cannot enter - need the key"))
                ),
                new DelayedTickStep(1, () -> {
                }),
                new ConditionalStep(
                        () -> bossDefeated.get() && hasKey.get(),
                        new CallbackStep(() -> {
                            questProgress.set(4);
                            System.out.println("  🏆 Quest completed successfully!");
                            System.out.println("  💰 Rewards: 1000 gold, Epic Sword");
                        })
                )
        ));

        Script questScript = new Script()
                .addStep(questStart)
                .addStep(questSteps);

        int tickCount = 0;
        while (!questScript.isFinished()) {
            questScript.update();
            tickCount++;
        }

        System.out.println("  ✓ Quest workflow completed in " + tickCount + " ticks");
        System.out.println("  📊 Final progress: " + questProgress.get() + "/4\n");
    }
}
