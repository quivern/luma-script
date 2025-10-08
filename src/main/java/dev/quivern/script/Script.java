package dev.quivern.script;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.LoopStrategy;
import dev.quivern.script.api.ScriptAction;
import dev.quivern.script.api.ScriptTime;
import dev.quivern.script.steps.DelayedStep;
import dev.quivern.script.steps.DelayedTickStep;
import dev.quivern.script.strategy.FiniteLoopStrategy;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;


/**
 * Manages a sequence of script steps with support for loops and time tracking.
 * Steps are executed sequentially and can be configured to repeat.
 */
@Getter
@Setter
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Script {
    /**
     * Empty action that does nothing.
     */
    public static final ScriptAction EMPTY = () -> {
    };
    
    private final ScriptTime time = new ScriptTime();
    private final List<IScriptStep> steps = new LinkedList<>();
    private int currentStepIndex = 0;
    private boolean interrupt;
    private LoopStrategy loopStrategy = new FiniteLoopStrategy(0);


    /**
     * Creates a new empty script with default configuration.
     */
    public Script() {
        cleanup();
    }

    /**
     * Adds a delayed wait step (time-based).
     *
     * @param delay the delay in milliseconds
     * @return this script for chaining
     */
    public Script waitDelayedStep(long delay) {
        steps.add(new DelayedStep(delay, EMPTY));
        return this;
    }

    /**
     * Adds a tick-based wait step.
     *
     * @param delay the number of ticks to wait
     * @return this script for chaining
     */
    public Script waitTickStep(int delay) {
        steps.add(new DelayedTickStep(delay, EMPTY));
        return this;
    }

    /**
     * Adds a custom step to the script.
     *
     * @param step the step to add
     * @return this script for chaining
     * @throws NullPointerException if step is null
     */
    public Script addStep(IScriptStep step) {
        steps.add(java.util.Objects.requireNonNull(step, "Step cannot be null"));
        return this;
    }

    /**
     * Resets the internal time tracker.
     */
    public void resetTime() {
        time.reset();
    }

    /**
     * Resets the current step index to the beginning.
     */
    public void resetStepIndex() {
        currentStepIndex = 0;
    }

    /**
     * Clears all steps and resets the script state.
     *
     * @return this script for chaining
     */
    public Script cleanup() {
        steps.clear();
        currentStepIndex = 0;
        time.reset();
        return this;
    }

    /**
     * Updates the script by executing the current step.
     * Called each frame/tick by the script manager.
     */
    public void update() {
        if (steps.isEmpty() || interrupt) return;
        if (currentStepIndex >= steps.size()) {
            if (loopStrategy.shouldLoop(currentStepIndex, steps.size())) {
                currentStepIndex = 0;
                loopStrategy.onLoop();
                steps.forEach(IScriptStep::reset);
            } else {
                return;
            }
        }
        IScriptStep current = steps.get(currentStepIndex);
        current.perform(time);
        if (current.isFinished()) {
            ++currentStepIndex;
            time.reset();
        }
    }

    /**
     * Sets the loop strategy for this script.
     *
     * @param strategy the loop strategy
     * @return this script for chaining
     * @throws NullPointerException if strategy is null
     */
    public Script setLoopStrategy(LoopStrategy strategy) {
        this.loopStrategy = java.util.Objects.requireNonNull(strategy, "Loop strategy cannot be null");
        return this;
    }

    /**
     * Checks if the script has finished execution.
     * Note: Auto-cleanup behavior can be disabled by calling setAutoCleanup(false).
     *
     * @return true if finished, false otherwise
     */
    public boolean isFinished() {
        return currentStepIndex >= steps.size() && !interrupt && loopStrategy.isFinished();
    }
    
    /**
     * Checks if script is finished and needs cleanup.
     * 
     * @return true if finished and has steps to clean
     */
    public boolean needsCleanup() {
        return isFinished() && !steps.isEmpty();
    }
    
    /**
     * Resets the script to its initial state without clearing steps.
     * Useful for rerunning the same script sequence.
     */
    public void reset() {
        currentStepIndex = 0;
        interrupt = false;
        time.reset();
        steps.forEach(IScriptStep::reset);
        loopStrategy.reset();
    }
}
