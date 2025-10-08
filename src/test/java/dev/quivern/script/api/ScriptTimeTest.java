package dev.quivern.script.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScriptTimeTest {

    @Test
    void testFinishedWithDelay() throws InterruptedException {
        ScriptTime time = new ScriptTime();
        
        assertFalse(time.finished(100));
        
        Thread.sleep(150);
        
        assertTrue(time.finished(100));
    }

    @Test
    void testReset() throws InterruptedException {
        ScriptTime time = new ScriptTime();
        
        Thread.sleep(100);
        assertTrue(time.finished(50));
        
        time.reset();
        assertFalse(time.finished(50));
    }

    @Test
    void testResetWithDelay() {
        ScriptTime time = new ScriptTime();
        time.reset(1000);
        
        // Should not be finished yet since we added 1000ms offset
        assertFalse(time.finished(500));
    }

    @Test
    void testElapsedTime() throws InterruptedException {
        ScriptTime time = new ScriptTime();
        
        Thread.sleep(100);
        
        long elapsed = time.elapsedTime();
        assertTrue(elapsed >= 100, "Elapsed time should be at least 100ms, was: " + elapsed);
        assertTrue(elapsed < 200, "Elapsed time should be less than 200ms, was: " + elapsed);
    }

    @Test
    void testNow() {
        long before = System.currentTimeMillis();
        long now = ScriptTime.now();
        long after = System.currentTimeMillis();
        
        assertTrue(now >= before && now <= after);
    }
}

