package dev.quivern.script;

import dev.quivern.script.steps.advanced.CallbackStep;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ScriptBuilderTest {

    @Test
    void testBuilderBasicChaining() {
        AtomicInteger counter = new AtomicInteger(0);

        Script script = new ScriptBuilder()
                .then(() -> counter.incrementAndGet())
                .waitTicks(2)
                .then(() -> counter.incrementAndGet())
                .build();

        assertFalse(script.isFinished());
        
        script.update();
        assertEquals(1, counter.get());
        
        script.update();
        script.update();
        script.update();
        
        assertEquals(2, counter.get());
        assertTrue(script.isFinished());
    }

    @Test
    void testBuilderWithCondition() {
        AtomicBoolean flag = new AtomicBoolean(false);
        AtomicInteger counter = new AtomicInteger(0);

        Script script = new ScriptBuilder()
                .when(() -> flag.get(), new CallbackStep(counter::incrementAndGet))
                .build();

        script.update();
        assertEquals(0, counter.get());

        flag.set(true);
        script.reset();
        script.update();
        assertEquals(1, counter.get());
    }

    @Test
    void testBuilderWithLoop() {
        AtomicInteger counter = new AtomicInteger(0);

        Script script = new ScriptBuilder()
                .then(counter::incrementAndGet)
                .loop(3)
                .build();

        while (!script.isFinished()) {
            script.update();
        }

        assertEquals(3, counter.get());
    }

    @Test
    void testWaitUntil() {
        AtomicBoolean condition = new AtomicBoolean(false);
        AtomicInteger counter = new AtomicInteger(0);

        Script script = new ScriptBuilder()
                .waitUntil(condition::get)
                .then(counter::incrementAndGet)
                .build();

        script.update();
        assertEquals(0, counter.get());

        condition.set(true);
        script.update();
        script.update();
        
        assertEquals(1, counter.get());
    }
}

