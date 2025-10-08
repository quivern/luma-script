package dev.quivern.script.steps.advanced;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptTime;

import java.util.Objects;

/**
 * Executes a callback once and marks itself as finished.
 */
public class CallbackStep implements IScriptStep {
    private final Runnable callback;
    private boolean finished = false;

    /**
     * Creates a callback step.
     *
     * @param callback the callback to execute
     * @throws NullPointerException if callback is null
     */
    public CallbackStep(Runnable callback) {
        this.callback = Objects.requireNonNull(callback, "Callback cannot be null");
    }

    @Override
    public void perform(ScriptTime time) {
        if (!finished) {
            callback.run();
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