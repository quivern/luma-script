package dev.quivern.script.strategy;

import dev.quivern.script.api.LoopStrategy;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Loops a script indefinitely until manually stopped.
 */
@Getter
@Accessors(fluent = true)
public class InfiniteLoopStrategy implements LoopStrategy {
    private int currentLoop;

    @Override
    public boolean shouldLoop(int currentStepIndex, int totalSteps) {
        return currentStepIndex >= totalSteps;
    }

    @Override
    public void onLoop() {
        ++currentLoop;
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
    @Override
    public void reset() {
        currentLoop = 0;
    }
}