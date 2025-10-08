package dev.quivern.script.steps;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptAction;
import dev.quivern.script.api.ScriptTime;

/**
 * Waits for a number of ticks/updates, then executes an action.
 */
public class DelayedTickStep implements IScriptStep {
    private final int ticks;
    private final ScriptAction action;
    private int ticksLeft;
    private boolean finished = false;

    /**
     * Creates a delayed tick step.
     *
     * @param ticks the number of ticks to wait
     * @param action the action to execute after delay
     * @throws NullPointerException if action is null
     * @throws IllegalArgumentException if ticks is negative
     */
    public DelayedTickStep(int ticks, ScriptAction action) {
        if (ticks < 0) {
            throw new IllegalArgumentException("Ticks cannot be negative");
        }
        this.ticks = ticks;
        this.action = java.util.Objects.requireNonNull(action, "Action cannot be null");
        this.ticksLeft = ticks;
    }

    @Override
    public void perform(ScriptTime time) {
        if (finished) return;
        if (ticksLeft > 0) {
            ticksLeft--;
            if (ticksLeft == 0) {
                action.perform();
                finished = true;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void reset() {
        ticksLeft = ticks;
        finished = false;
    }
} 