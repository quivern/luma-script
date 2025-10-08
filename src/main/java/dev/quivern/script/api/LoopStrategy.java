package dev.quivern.script.api;

/**
 * Defines looping behavior for script execution.
 * Determines when and how a script should repeat.
 */
public interface LoopStrategy {

    /**
     * Determines if the script should loop.
     *
     * @param currentStepIndex the current step index
     * @param totalSteps the total number of steps
     * @return true if the script should loop, false otherwise
     */
    boolean shouldLoop(int currentStepIndex, int totalSteps);

    /**
     * Called when a loop iteration begins.
     */
    void onLoop();

    /**
     * Checks if the loop strategy has completed.
     *
     * @return true if the strategy is finished, false otherwise
     */
    boolean isFinished();
    
    /**
     * Resets the loop strategy to its initial state.
     */
    default void reset() {
        // Default: no-op for strategies that don't need reset
    }
}