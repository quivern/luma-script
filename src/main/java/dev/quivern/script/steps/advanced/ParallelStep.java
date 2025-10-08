package dev.quivern.script.steps.advanced;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptTime;

import java.util.List;

/**
 * Executes multiple steps in parallel.
 * Finishes when all steps are complete.
 */
public class ParallelStep implements IScriptStep {
    private final List<IScriptStep> steps;

    /**
     * Creates a parallel step.
     *
     * @param steps the list of steps to execute in parallel
     * @throws NullPointerException if steps is null
     * @throws IllegalArgumentException if steps is empty
     */
    public ParallelStep(List<IScriptStep> steps) {
        this.steps = java.util.Objects.requireNonNull(steps, "Steps list cannot be null");
        if (steps.isEmpty()) {
            throw new IllegalArgumentException("Steps list cannot be empty");
        }
    }

    @Override
    public void perform(ScriptTime time) {
        for (IScriptStep step : steps) {
            if (!step.isFinished()) {
                step.perform(time);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return steps.stream().allMatch(IScriptStep::isFinished);
    }

    @Override
    public void reset() {
        steps.forEach(IScriptStep::reset);
    }
}