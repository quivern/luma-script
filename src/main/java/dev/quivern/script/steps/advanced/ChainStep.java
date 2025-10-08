package dev.quivern.script.steps.advanced;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptTime;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Executes steps sequentially and dynamically adds new steps from a supplier.
 * Finishes when the supplier returns null.
 */
public class ChainStep implements IScriptStep {
    private final List<IScriptStep> steps = new LinkedList<>();
    private final Supplier<IScriptStep> nextStepSupplier;
    private int currentIndex = 0;
    private boolean finished = false;

    /**
     * Creates a chain step.
     *
     * @param initialSteps the initial list of steps
     * @param nextStepSupplier supplier that provides next steps, returns null when done
     */
    public ChainStep(List<IScriptStep> initialSteps, Supplier<IScriptStep> nextStepSupplier) {
        if (initialSteps != null) {
            steps.addAll(initialSteps);
        }
        this.nextStepSupplier = nextStepSupplier;
    }

    @Override
    public void perform(ScriptTime time) {
        if (finished) return;
        if (currentIndex < steps.size()) {
            IScriptStep current = steps.get(currentIndex);
            current.perform(time);
            if (current.isFinished()) {
                currentIndex++;
            }
        } else {
            IScriptStep next = nextStepSupplier.get();
            if (next != null) {
                steps.add(next);
            } else {
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
        steps.forEach(IScriptStep::reset);
        currentIndex = 0;
        finished = false;
    }
} 