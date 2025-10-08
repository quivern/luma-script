package dev.quivern.script.steps.advanced;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptTime;

/**
 * Wraps a step with a timeout.
 * The step is finished when either it completes or the timeout is reached.
 */
public class TimeoutStep implements IScriptStep {
    private final IScriptStep step;
    private final long timeout;
    private final ScriptTime timer = new ScriptTime();
    private boolean started = false;

    /**
     * Creates a timeout step.
     *
     * @param step the step to wrap
     * @param timeout the timeout duration in milliseconds
     */
    public TimeoutStep(IScriptStep step, long timeout) {
        this.step = step;
        this.timeout = timeout;
    }

    @Override
    public void perform(ScriptTime time) {
        if (!started) {
            timer.reset();
            started = true;
        }
        if (!step.isFinished() && !timer.finished(timeout)) {
            step.perform(time);
        }
    }

    @Override
    public boolean isFinished() {
        return step.isFinished() || timer.finished(timeout);
    }

    @Override
    public void reset() {
        step.reset();
        timer.reset();
        started = false;
    }
}