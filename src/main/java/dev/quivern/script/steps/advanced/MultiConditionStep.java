package dev.quivern.script.steps.advanced;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptTime;

import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Executes a step only when all conditions are true.
 * Finishes when any condition becomes false or the step completes.
 */
public class MultiConditionStep implements IScriptStep {
    private final List<BooleanSupplier> conditions;
    private final IScriptStep step;

    /**
     * Creates a multi-condition step.
     *
     * @param conditions list of conditions that must all be true
     * @param step the step to execute
     */
    public MultiConditionStep(List<BooleanSupplier> conditions, IScriptStep step) {
        this.conditions = conditions;
        this.step = step;
    }

    @Override
    public void perform(ScriptTime time) {
        if (conditions.stream().allMatch(BooleanSupplier::getAsBoolean)) {
            step.perform(time);
        }
    }

    @Override
    public boolean isFinished() {
        return conditions.stream().anyMatch(c -> !c.getAsBoolean()) || step.isFinished();
    }

    @Override
    public void reset() {
        step.reset();
    }
} 