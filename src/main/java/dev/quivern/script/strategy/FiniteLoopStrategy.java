package dev.quivern.script.strategy;

import dev.quivern.script.api.LoopStrategy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Loops a script for a fixed number of iterations.
 * The first execution counts as iteration 1.
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class FiniteLoopStrategy implements LoopStrategy {
    private final int loopCount;
    private int currentLoop = 1; // Start at 1 since first execution counts

    @Override
    public boolean shouldLoop(int currentStepIndex, int totalSteps) {
        return currentStepIndex >= totalSteps && currentLoop < loopCount;
    }

    @Override
    public void onLoop() {
        ++currentLoop;
    }

    @Override
    public boolean isFinished() {
        return currentLoop >= loopCount;
    }
    
    @Override
    public void reset() {
        currentLoop = 1;
    }
}