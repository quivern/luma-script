package dev.quivern.script.steps;

import dev.quivern.script.api.ScriptTime;
import dev.quivern.script.steps.advanced.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class StepsTest {

    @Test
    void testCallbackStep() {
        AtomicBoolean executed = new AtomicBoolean(false);
        var step = new CallbackStep(() -> executed.set(true));

        assertFalse(step.isFinished());
        step.perform(new ScriptTime());
        assertTrue(step.isFinished());
        assertTrue(executed.get());

        step.reset();
        assertFalse(step.isFinished());
    }

    @Test
    void testDelayedStep() throws InterruptedException {
        AtomicBoolean executed = new AtomicBoolean(false);
        var step = new DelayedStep(100, () -> executed.set(true));
        var time = new ScriptTime();

        step.perform(time);
        assertFalse(step.isFinished());
        assertFalse(executed.get());

        Thread.sleep(150);
        step.perform(time);
        assertTrue(step.isFinished());
        assertTrue(executed.get());
    }

    @Test
    void testDelayedTickStep() {
        AtomicBoolean executed = new AtomicBoolean(false);
        var step = new DelayedTickStep(3, () -> executed.set(true));
        var time = new ScriptTime();

        step.perform(time);
        assertFalse(step.isFinished());
        assertFalse(executed.get());

        step.perform(time);
        assertFalse(step.isFinished());
        assertFalse(executed.get());

        step.perform(time);
        assertTrue(step.isFinished());
        assertTrue(executed.get());
    }

    @Test
    void testConditionalStep() {
        AtomicBoolean condition = new AtomicBoolean(true);
        AtomicInteger counter = new AtomicInteger(0);

        var step = new ConditionalStep(
                condition::get,
                new CallbackStep(counter::incrementAndGet)
        );

        step.perform(new ScriptTime());
        assertTrue(step.isFinished());
        assertEquals(1, counter.get());

        step.reset();
        condition.set(false);
        step.perform(new ScriptTime());
        assertTrue(step.isFinished());
        assertEquals(1, counter.get());
    }

    @Test
    void testBranchStep() {
        AtomicBoolean condition = new AtomicBoolean(true);
        AtomicInteger ifCounter = new AtomicInteger(0);
        AtomicInteger elseCounter = new AtomicInteger(0);

        var step = new BranchStep(
                condition::get,
                new CallbackStep(ifCounter::incrementAndGet),
                new CallbackStep(elseCounter::incrementAndGet)
        );

        step.perform(new ScriptTime());
        assertEquals(1, ifCounter.get());
        assertEquals(0, elseCounter.get());

        step.reset();
        condition.set(false);
        step.perform(new ScriptTime());
        assertEquals(1, ifCounter.get());
        assertEquals(1, elseCounter.get());
    }

    @Test
    void testParallelStep() {
        AtomicInteger counter1 = new AtomicInteger(0);
        AtomicInteger counter2 = new AtomicInteger(0);

        var step = new ParallelStep(Arrays.asList(
                new CallbackStep(counter1::incrementAndGet),
                new CallbackStep(counter2::incrementAndGet)
        ));

        step.perform(new ScriptTime());
        assertTrue(step.isFinished());
        assertEquals(1, counter1.get());
        assertEquals(1, counter2.get());
    }

    @Test
    void testSequenceStep() {
        AtomicInteger order = new AtomicInteger(0);

        var step = new SequenceStep(Arrays.asList(
                new CallbackStep(() -> assertEquals(0, order.getAndIncrement())),
                new CallbackStep(() -> assertEquals(1, order.getAndIncrement())),
                new CallbackStep(() -> assertEquals(2, order.getAndIncrement()))
        ));

        var time = new ScriptTime();
        assertFalse(step.isFinished());

        step.perform(time);
        assertFalse(step.isFinished());

        step.perform(time);
        assertFalse(step.isFinished());

        step.perform(time);
        assertTrue(step.isFinished());
        assertEquals(3, order.get());
    }

    @Test
    void testRepeatStep() {
        AtomicInteger counter = new AtomicInteger(0);
        var innerStep = new CallbackStep(counter::incrementAndGet);
        var step = new RepeatStep(innerStep, 3);

        var time = new ScriptTime();
        for (int i = 0; i < 3; i++) {
            assertFalse(step.isFinished());
            step.perform(time);
        }

        assertTrue(step.isFinished());
        assertEquals(3, counter.get());
    }

    @Test
    void testWaitUntilStep() {
        AtomicBoolean condition = new AtomicBoolean(false);
        var step = new WaitUntilStep(condition::get);

        step.perform(new ScriptTime());
        assertFalse(step.isFinished());

        condition.set(true);
        step.perform(new ScriptTime());
        assertTrue(step.isFinished());
    }

    @Test
    void testCancelableStep() {
        AtomicBoolean cancel = new AtomicBoolean(false);
        AtomicInteger counter = new AtomicInteger(0);

        var step = new CancelableStep(
                new CallbackStep(counter::incrementAndGet),
                cancel::get
        );

        step.perform(new ScriptTime());
        assertTrue(step.isFinished());
        assertEquals(1, counter.get());

        step.reset();
        cancel.set(true);
        step.perform(new ScriptTime());
        assertTrue(step.isFinished());
        assertEquals(1, counter.get());
    }
}

