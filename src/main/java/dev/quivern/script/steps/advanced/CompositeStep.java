package dev.quivern.script.steps.advanced;

import dev.quivern.script.api.IScriptStep;
import dev.quivern.script.api.ScriptTime;

import java.util.List;
import java.util.function.Predicate;

/**
 * Executes multiple steps and uses a custom predicate to determine completion.
 * Useful for complex completion logic based on step states.
 */
public class CompositeStep implements IScriptStep {
    private final List<IScriptStep> steps;
    private final Predicate<List<Boolean>> finishedPredicate;

    /**
     * Creates a composite step.
     *
     * @param steps             the list of steps to execute
     * @param finishedPredicate predicate that determines if the composite is finished
     */
    public CompositeStep(List<IScriptStep> steps, Predicate<List<Boolean>> finishedPredicate) {
        this.steps = steps;
        this.finishedPredicate = finishedPredicate;
    }

    @Override
    public void perform(ScriptTime time) {
        for (IScriptStep step : steps) {
            if (!step.isFinished()) {
                step.perform(time);
            }
        }
    }

    @Override
    public boolean isFinished() {
        List<Boolean> finishedList = steps.stream().map(IScriptStep::isFinished).toList();
        return finishedPredicate.test(finishedList);
    }

    @Override
    public void reset() {
        steps.forEach(IScriptStep::reset);
    }
} 