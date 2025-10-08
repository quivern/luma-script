package dev.quivern.script;

import dev.quivern.script.steps.advanced.CallbackStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ScriptManagerTest {

    private ScriptManager manager;

    @BeforeEach
    void setUp() {
        manager = new ScriptManager();
    }

    @Test
    void testGetOrCreateScript() {
        var script = manager.getScript("test");
        assertTrue(script.isPresent());
        assertSame(script.get(), manager.getScript("test").get());
    }

    @Test
    void testAddScript() {
        Script customScript = new Script();
        manager.addScript("custom", customScript);

        assertTrue(manager.containsScript("custom"));
        assertSame(customScript, manager.getScript("custom").get());
    }

    @Test
    void testAddScriptValidation() {
        assertThrows(IllegalArgumentException.class, () -> manager.addScript(null, new Script()));
        assertThrows(IllegalArgumentException.class, () -> manager.addScript("", new Script()));
        assertThrows(IllegalArgumentException.class, () -> manager.addScript("test", null));
    }

    @Test
    void testContainsScript() {
        assertFalse(manager.containsScript("test"));
        manager.getScript("test");
        assertTrue(manager.containsScript("test"));
    }

    @Test
    void testRemoveScript() {
        manager.getScript("test");
        assertTrue(manager.containsScript("test"));

        manager.removeScript("test");
        assertFalse(manager.containsScript("test"));
    }

    @Test
    void testUpdateScript() {
        AtomicInteger counter = new AtomicInteger(0);
        manager.getScript("test").ifPresent(script ->
                script.addStep(new CallbackStep(counter::incrementAndGet))
        );

        manager.updateScript("test");
        assertEquals(1, counter.get());
    }

    @Test
    void testUpdateScriptWithCondition() {
        AtomicInteger counter = new AtomicInteger(0);
        manager.getScript("test").ifPresent(script ->
                script.addStep(new CallbackStep(counter::incrementAndGet))
        );

        manager.updateScript("test", () -> false);
        assertEquals(0, counter.get());

        manager.updateScript("test", () -> true);
        assertEquals(1, counter.get());
    }

    @Test
    void testUpdateAll() {
        AtomicInteger counter1 = new AtomicInteger(0);
        AtomicInteger counter2 = new AtomicInteger(0);

        manager.getScript("script1").ifPresent(s -> s.addStep(new CallbackStep(counter1::incrementAndGet)));
        manager.getScript("script2").ifPresent(s -> s.addStep(new CallbackStep(counter2::incrementAndGet)));

        manager.updateAll();
        assertEquals(1, counter1.get());
        assertEquals(1, counter2.get());
    }

    @Test
    void testCleanupScript() {
        manager.getScript("test").ifPresent(script -> script.addStep(new CallbackStep(() -> {})));

        manager.cleanupScript("test");
        assertTrue(manager.getScript("test").get().isFinished());
    }

    @Test
    void testCleanupAll() {
        manager.getScript("s1").ifPresent(s -> s.addStep(new CallbackStep(() -> {})));
        manager.getScript("s2").ifPresent(s -> s.addStep(new CallbackStep(() -> {})));

        manager.cleanupAll();
        assertTrue(manager.finished("s1"));
        assertTrue(manager.finished("s2"));
    }

    @Test
    void testClearAll() {
        manager.getScript("test");
        assertTrue(manager.containsScript("test"));

        manager.clearAll();
        assertFalse(manager.containsScript("test"));
    }

    @Test
    void testGetAllScriptNames() {
        manager.getScript("s1");
        manager.getScript("s2");

        var names = manager.getAllScriptNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("s1"));
        assertTrue(names.contains("s2"));
    }
}

