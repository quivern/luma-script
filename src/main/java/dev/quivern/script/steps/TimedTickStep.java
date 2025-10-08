package dev.quivern.script.steps;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptAction;
import dev.quivern.script.api.ScriptTime;

/**
 * Executes an action for a specified number of ticks.
 */
public class TimedTickStep implements IScriptStep {
    private final int ticks;
    private final ScriptAction action;
    private int ticksLeft;
    private boolean finished = false;

    /**
     * Creates a timed tick step.
     *
     * @param ticks the number of ticks to execute
     * @param action the action to perform each tick
     */
    public TimedTickStep(int ticks, ScriptAction action) {
        this.ticks = ticks;
        this.action = action;
        this.ticksLeft = ticks;
    }

    @Override
    public void perform(ScriptTime time) {
        if (ticksLeft > 0) {
            action.perform();
            ticksLeft--;
            if (ticksLeft == 0) {
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