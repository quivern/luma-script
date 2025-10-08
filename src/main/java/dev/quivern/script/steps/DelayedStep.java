package dev.quivern.script.steps;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptAction;
import dev.quivern.script.api.ScriptTime;

/**
 * Waits for a time-based delay, then executes an action.
 */
public class DelayedStep implements IScriptStep {
    private final long delay;
    private final ScriptAction action;
    private boolean finished = false;

    /**
     * Creates a delayed step.
     *
     * @param delay the delay in milliseconds
     * @param action the action to execute after delay
     * @throws NullPointerException if action is null
     * @throws IllegalArgumentException if delay is negative
     */
    public DelayedStep(long delay, ScriptAction action) {
        if (delay < 0) {
            throw new IllegalArgumentException("Delay cannot be negative");
        }
        this.delay = delay;
        this.action = java.util.Objects.requireNonNull(action, "Action cannot be null");
    }

    @Override
    public void perform(ScriptTime time) {
        if (finished) return;
        if (time.finished(delay)) {
            action.perform();
            finished = true;
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void reset() {
        finished = false;
    }
} 