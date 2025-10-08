package dev.quivern.script.steps.advanced;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptTime;

import java.util.Objects;
import java.util.function.BooleanSupplier;

/**
 * Executes one of two steps based on a condition (if-else logic).
 */
public class BranchStep implements IScriptStep {
    private final BooleanSupplier condition;
    private final IScriptStep ifStep;
    private final IScriptStep elseStep;

    /**
     * Creates a branch step.
     *
     * @param condition the condition to evaluate
     * @param ifStep the step to execute when condition is true
     * @param elseStep the step to execute when condition is false
     * @throws NullPointerException if any parameter is null
     */
    public BranchStep(BooleanSupplier condition, IScriptStep ifStep, IScriptStep elseStep) {
        this.condition = Objects.requireNonNull(condition, "Condition cannot be null");
        this.ifStep = Objects.requireNonNull(ifStep, "If step cannot be null");
        this.elseStep = Objects.requireNonNull(elseStep, "Else step cannot be null");
    }

    @Override
    public void perform(ScriptTime time) {
        if (condition.getAsBoolean()) {
            ifStep.perform(time);
        } else {
            elseStep.perform(time);
        }
    }

    @Override
    public boolean isFinished() {
        return condition.getAsBoolean() ? ifStep.isFinished() : elseStep.isFinished();
    }

    @Override
    public void reset() {
        ifStep.reset();
        elseStep.reset();
    }
}