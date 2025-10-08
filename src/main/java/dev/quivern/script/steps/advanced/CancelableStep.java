package dev.quivern.script.steps.advanced;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptTime;

import java.util.function.BooleanSupplier;

/**
 * Wraps a step with a cancellation condition.
 * The step is skipped if the condition returns true.
 */
public class CancelableStep implements IScriptStep {
    private final IScriptStep step;
    private final BooleanSupplier cancelCondition;

    /**
     * Creates a cancelable step.
     *
     * @param step the step to wrap
     * @param cancelCondition the condition that triggers cancellation
     */
    public CancelableStep(IScriptStep step, BooleanSupplier cancelCondition) {
        this.step = step;
        this.cancelCondition = cancelCondition;
    }

    @Override
    public void perform(ScriptTime time) {
        if (!cancelCondition.getAsBoolean()) {
            step.perform(time);
        }
    }

    @Override
    public boolean isFinished() {
        return cancelCondition.getAsBoolean() || step.isFinished();
    }

    @Override
    public void reset() {
        step.reset();
    }
}