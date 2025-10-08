package dev.quivern.script.api;

/**
 * Represents a single executable step in a script sequence.
 * Steps can be simple actions, delays, or complex compositions.
 */
public interface IScriptStep {

    /**
     * Executes the step logic for the current frame/tick.
     *
     * @param time the script time tracker
     */
    void perform(ScriptTime time);

    /**
     * Checks if the step has completed its execution.
     *
     * @return true if the step is finished, false otherwise
     */
    boolean isFinished();

    /**
     * Resets the step to its initial state for reuse.
     */
    void reset();
}