package dev.quivern.script.steps.advanced;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptTime;

/**
 * Repeats a step a specified number of times.
 * Each time the step completes, it is reset and executed again.
 */
public class RepeatStep implements IScriptStep {
    private final IScriptStep step;
    private final int repeatCount;
    private int current = 0;

    /**
     * Creates a repeat step.
     *
     * @param step the step to repeat
     * @param repeatCount the number of times to repeat
     */
    public RepeatStep(IScriptStep step, int repeatCount) {
        this.step = step;
        this.repeatCount = repeatCount;
    }

    @Override
    public void perform(ScriptTime time) {
        if (!step.isFinished()) {
            step.perform(time);
            if (step.isFinished()) {
                current++;
                if (current < repeatCount) {
                    step.reset();
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        return current >= repeatCount;
    }

    @Override
    public void reset() {
        step.reset();
        current = 0;
    }
}