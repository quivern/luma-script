package dev.quivern.script;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.LoopStrategy;
import dev.quivern.script.steps.DelayedStep;
import dev.quivern.script.steps.DelayedTickStep;
import dev.quivern.script.steps.advanced.*;
import dev.quivern.script.strategy.FiniteLoopStrategy;
import dev.quivern.script.strategy.InfiniteLoopStrategy;

import java.util.function.BooleanSupplier;

/**
 * Fluent builder for creating scripts with convenient helper methods.
 * Provides a more readable way to construct complex script sequences.
 */
public class ScriptBuilder {
    private final Script script;

    /**
     * Creates a new script builder.
     */
    public ScriptBuilder() {
        this.script = new Script();
    }

    /**
     * Creates a builder from an existing script.
     *
     * @param script the script to build upon
     */
    public ScriptBuilder(Script script) {
        this.script = script;
    }

    /**
     * Adds a callback step that executes once.
     *
     * @param callback the callback to execute
     * @return this builder for chaining
     */
    public ScriptBuilder then(Runnable callback) {
        script.addStep(new CallbackStep(callback));
        return this;
    }

    /**
     * Adds a time-based delay.
     *
     * @param milliseconds delay in milliseconds
     * @return this builder for chaining
     */
    public ScriptBuilder waitMillis(long milliseconds) {
        script.waitDelayedStep(milliseconds);
        return this;
    }

    /**
     * Adds a tick-based delay.
     *
     * @param ticks number of ticks to wait
     * @return this builder for chaining
     */
    public ScriptBuilder waitTicks(int ticks) {
        script.waitTickStep(ticks);
        return this;
    }

    /**
     * Adds a conditional step.
     *
     * @param condition the condition to check
     * @param step the step to execute when true
     * @return this builder for chaining
     */
    public ScriptBuilder when(BooleanSupplier condition, IScriptStep step) {
        script.addStep(new ConditionalStep(condition, step));
        return this;
    }

    /**
     * Adds a branch (if-else) step.
     *
     * @param condition the condition
     * @param ifStep step when true
     * @param elseStep step when false
     * @return this builder for chaining
     */
    public ScriptBuilder branch(BooleanSupplier condition, IScriptStep ifStep, IScriptStep elseStep) {
        script.addStep(new BranchStep(condition, ifStep, elseStep));
        return this;
    }

    /**
     * Waits until a condition becomes true.
     *
     * @param condition the condition to wait for
     * @return this builder for chaining
     */
    public ScriptBuilder waitUntil(BooleanSupplier condition) {
        script.addStep(new WaitUntilStep(condition));
        return this;
    }

    /**
     * Sets the script to loop a specific number of times.
     *
     * @param count number of loops
     * @return this builder for chaining
     */
    public ScriptBuilder loop(int count) {
        script.setLoopStrategy(new FiniteLoopStrategy(count));
        return this;
    }

    /**
     * Sets the script to loop indefinitely.
     *
     * @return this builder for chaining
     */
    public ScriptBuilder loopForever() {
        script.setLoopStrategy(new InfiniteLoopStrategy());
        return this;
    }

    /**
     * Adds a custom step.
     *
     * @param step the step to add
     * @return this builder for chaining
     */
    public ScriptBuilder addStep(IScriptStep step) {
        script.addStep(step);
        return this;
    }

    /**
     * Builds and returns the script.
     *
     * @return the constructed script
     */
    public Script build() {
        return script;
    }

    /**
     * Builds the script and starts it immediately.
     *
     * @return the constructed and started script
     */
    public Script buildAndStart() {
        return script;
    }
}

