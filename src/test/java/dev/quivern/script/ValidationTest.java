package dev.quivern.script;

import dev.quivern.script.steps.DelayedStep;
import dev.quivern.script.steps.DelayedTickStep;
import dev.quivern.script.steps.TimedStep;
import dev.quivern.script.steps.advanced.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for parameter validation across the library.
 */
class ValidationTest {

    @Test
    void testNullCallbackThrows() {
        assertThrows(NullPointerException.class, 
            () -> new CallbackStep(null));
    }

    @Test
    void testNullStepInScriptThrows() {
        Script script = new Script();
        assertThrows(NullPointerException.class,
            () -> script.addStep(null));
    }

    @Test
    void testNullLoopStrategyThrows() {
        Script script = new Script();
        assertThrows(NullPointerException.class,
            () -> script.setLoopStrategy(null));
    }

    @Test
    void testNegativeDelayThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new DelayedStep(-1, () -> {}));
    }

    @Test
    void testNegativeTicksThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new DelayedTickStep(-1, () -> {}));
    }

    @Test
    void testNegativeDurationThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new TimedStep(-1, () -> {}));
    }

    @Test
    void testNullConditionInConditionalStepThrows() {
        assertThrows(NullPointerException.class,
            () -> new ConditionalStep(null, new CallbackStep(() -> {})));
    }

    @Test
    void testNullStepInConditionalStepThrows() {
        assertThrows(NullPointerException.class,
            () -> new ConditionalStep(() -> true, null));
    }

    @Test
    void testNullConditionInBranchStepThrows() {
        CallbackStep step = new CallbackStep(() -> {});
        assertThrows(NullPointerException.class,
            () -> new BranchStep(null, step, step));
    }

    @Test
    void testNullStepsInBranchStepThrows() {
        assertThrows(NullPointerException.class,
            () -> new BranchStep(() -> true, null, new CallbackStep(() -> {})));
        
        assertThrows(NullPointerException.class,
            () -> new BranchStep(() -> true, new CallbackStep(() -> {}), null));
    }

    @Test
    void testNullStepsListInParallelStepThrows() {
        assertThrows(NullPointerException.class,
            () -> new ParallelStep(null));
    }

    @Test
    void testEmptyStepsListInParallelStepThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new ParallelStep(Collections.emptyList()));
    }

    @Test
    void testNullStepsListInSequenceStepThrows() {
        assertThrows(NullPointerException.class,
            () -> new SequenceStep(null));
    }

    @Test
    void testEmptyStepsListInSequenceStepThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> new SequenceStep(Collections.emptyList()));
    }

    @Test
    void testValidParametersDoNotThrow() {
        assertDoesNotThrow(() -> {
            new CallbackStep(() -> {});
            new DelayedStep(0, () -> {});
            new DelayedTickStep(0, () -> {});
            new TimedStep(0, () -> {});
            new ConditionalStep(() -> true, new CallbackStep(() -> {}));
            new BranchStep(() -> true, 
                new CallbackStep(() -> {}), 
                new CallbackStep(() -> {}));
            new ParallelStep(List.of(new CallbackStep(() -> {})));
            new SequenceStep(List.of(new CallbackStep(() -> {})));
        });
    }
}

