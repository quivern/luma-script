package dev.quivern.script.steps.advanced;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptTime;

import java.util.List;

/**
 * Executes steps sequentially, one after another.
 * Finishes when all steps are complete.
 */
public class SequenceStep implements IScriptStep {
    private final List<IScriptStep> steps;
    private int index = 0;

    /**
     * Creates a sequence step.
     *
     * @param steps the list of steps to execute in sequence
     * @throws NullPointerException if steps is null
     * @throws IllegalArgumentException if steps is empty
     */
    public SequenceStep(List<IScriptStep> steps) {
        this.steps = java.util.Objects.requireNonNull(steps, "Steps list cannot be null");
        if (steps.isEmpty()) {
            throw new IllegalArgumentException("Steps list cannot be empty");
        }
    }

    @Override
    public void perform(ScriptTime time) {
        if (index < steps.size()) {
            IScriptStep current = steps.get(index);
            current.perform(time);
            if (current.isFinished()) {
                index++;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return index >= steps.size();
    }

    @Override
    public void reset() {
        index = 0;
        steps.forEach(IScriptStep::reset);
    }
}