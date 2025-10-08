package dev.quivern.script;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.steps.DelayedStep;
import dev.quivern.script.steps.advanced.CallbackStep;
import dev.quivern.script.strategy.FiniteLoopStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ScriptTest {

    private Script script;

    @BeforeEach
    void setUp() {
        script = new Script();
    }

    @Test
    void testScriptCreation() {
        assertNotNull(script);
        assertTrue(script.isFinished());
    }

    @Test
    void testAddStep() {
        AtomicInteger counter = new AtomicInteger(0);
        IScriptStep step = new CallbackStep(counter::incrementAndGet);

        script.addStep(step);
        assertFalse(script.isFinished());
        assertEquals(0, counter.get());

        script.update();
        assertTrue(script.isFinished());
        assertEquals(1, counter.get());
    }

    @Test
    void testSequentialExecution() {
        AtomicInteger order = new AtomicInteger(0);

        script.addStep(new CallbackStep(() -> assertEquals(0, order.getAndIncrement())))
                .addStep(new CallbackStep(() -> assertEquals(1, order.getAndIncrement())))
                .addStep(new CallbackStep(() -> assertEquals(2, order.getAndIncrement())));

        while (!script.isFinished()) {
            script.update();
        }

        assertEquals(3, order.get());
    }

    @Test
    void testWaitDelayedStep() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);

        script.waitDelayedStep(100)
                .addStep(new CallbackStep(counter::incrementAndGet));

        script.update();
        assertEquals(0, counter.get());

        Thread.sleep(150);
        script.update();
        script.update();
        assertEquals(1, counter.get());
    }

    @Test
    void testWaitTickStep() {
        AtomicInteger counter = new AtomicInteger(0);

        script.waitTickStep(3)
                .addStep(new CallbackStep(counter::incrementAndGet));

        for (int i = 0; i < 3; i++) {
            script.update();
            assertEquals(0, counter.get());
        }

        script.update();
        assertEquals(1, counter.get());
    }

    @Test
    void testLoopStrategy() {
        AtomicInteger counter = new AtomicInteger(0);

        script.addStep(new CallbackStep(counter::incrementAndGet))
                .setLoopStrategy(new FiniteLoopStrategy(3));

        while (!script.isFinished()) {
            script.update();
        }

        assertEquals(3, counter.get());
    }

    @Test
    void testCleanup() {
        script.addStep(new CallbackStep(() -> {}));
        assertFalse(script.isFinished());

        script.cleanup();
        assertTrue(script.isFinished());
    }

    @Test
    void testInterrupt() {
        script.addStep(new CallbackStep(() -> {}))
                .addStep(new CallbackStep(() -> {}));

        script.setInterrupt(true);
        script.update();

        assertFalse(script.isFinished());
    }
}

