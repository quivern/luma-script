package dev.quivern.script.steps.advanced;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptTime;

import java.util.function.BooleanSupplier;

/**
 * Executes a step only when a condition is true.
 * Finishes immediately if condition is false.
 */
public class ConditionalStep implements IScriptStep {
    private final BooleanSupplier condition;
    private final IScriptStep step;

    /**
     * Creates a conditional step.
     *
     * @param condition the condition to check
     * @param step the step to execute when condition is true
     * @throws NullPointerException if any parameter is null
     */
    public ConditionalStep(BooleanSupplier condition, IScriptStep step) {
        this.condition = java.util.Objects.requireNonNull(condition, "Condition cannot be null");
        this.step = java.util.Objects.requireNonNull(step, "Step cannot be null");
    }

    @Override
    public void perform(ScriptTime time) {
        if (condition.getAsBoolean()) {
            step.perform(time);
        }
    }

    @Override
    public boolean isFinished() {
        return !condition.getAsBoolean() || step.isFinished();
    }

    @Override
    public void reset() {
        step.reset();
    }
}