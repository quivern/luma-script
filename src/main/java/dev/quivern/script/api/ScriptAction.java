package dev.quivern.script.api;

/**
 * Functional interface for executing a simple action.
 * Used in steps that perform operations without parameters.
 */
@FunctionalInterface
public interface ScriptAction {

    /**
     * Performs the action.
     */
    void perform();
}
