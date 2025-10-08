package dev.quivern.script.steps;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptAction;
import dev.quivern.script.api.ScriptTime;

/**
 * Executes an action repeatedly for a specified duration.
 * Uses its own internal timer to avoid interfering with other steps.
 */
public class TimedStep implements IScriptStep {
    private final long duration;
    private final ScriptAction action;
    private final ScriptTime internalTimer = new ScriptTime();
    private boolean started = false;
    private boolean finished = false;

    /**
     * Creates a timed step.
     *
     * @param duration the duration in milliseconds
     * @param action the action to perform during the duration
     * @throws NullPointerException if action is null
     * @throws IllegalArgumentException if duration is negative
     */
    public TimedStep(long duration, ScriptAction action) {
        if (duration < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
        this.duration = duration;
        this.action = java.util.Objects.requireNonNull(action, "Action cannot be null");
    }

    @Override
    public void perform(ScriptTime time) {
        if (!started) {
            internalTimer.reset();
            started = true;
        }
        if (finished) return;
        action.perform();
        if (internalTimer.finished(duration)) {
            finished = true;
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void reset() {
        started = false;
        finished = false;
    }
} 