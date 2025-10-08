package dev.quivern.script.steps.advanced;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptTime;

import java.util.function.BooleanSupplier;

/**
 * Waits until a condition becomes true.
 * Does nothing until the condition is met.
 */
public class WaitUntilStep implements IScriptStep {
    private final BooleanSupplier condition;
    private boolean finished = false;

    /**
     * Creates a wait until step.
     *
     * @param condition the condition to wait for
     */
    public WaitUntilStep(BooleanSupplier condition) {
        this.condition = condition;
    }

    @Override
    public void perform(ScriptTime time) {
        if (condition.getAsBoolean()) {
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