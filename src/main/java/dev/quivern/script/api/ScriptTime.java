package dev.quivern.script.api;

/**
 * Tracks time for script step execution.
 * Provides utilities for time-based delays and checks.
 */
public final class ScriptTime {
    private long startTime;

    /**
     * Creates a new script time tracker initialized to current time.
     */
    public ScriptTime() {
        reset();
    }

    /**
     * Checks if a delay has elapsed.
     *
     * @param delay the delay in milliseconds
     * @return true if the delay has passed, false otherwise
     */
    public boolean finished(final double delay) {
        return System.currentTimeMillis() >= startTime + delay;
    }

    /**
     * Resets the timer to current time.
     */
    public void reset() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Resets the timer with an offset.
     *
     * @param delay the offset in milliseconds
     */
    public void reset(long delay) {
        this.startTime = System.currentTimeMillis() + delay;
    }

    /**
     * Gets elapsed time since last reset.
     *
     * @return elapsed time in milliseconds
     */
    public long elapsedTime() {
        return System.currentTimeMillis() - this.startTime;
    }

    /**
     * Gets current system time.
     *
     * @return current time in milliseconds
     */
    public static long now() {
        return System.currentTimeMillis();
    }
}