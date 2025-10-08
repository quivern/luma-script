package dev.quivern.script;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


/**
 * Manages multiple named scripts with thread-safe operations.
 * Provides centralized control for script lifecycle and execution.
 */
public class ScriptManager {
    private final Map<String, Script> scripts = new ConcurrentHashMap<>();

    /**
     * Gets a script by name, creating it if it doesn't exist.
     *
     * @param name the script name
     * @return optional containing the script, empty if name is invalid
     */
    public Optional<Script> getScript(String name) {
        return isNullOrEmpty(name) ? Optional.empty() : Optional.of(scripts.computeIfAbsent(name, k -> new Script()));
    }

    /**
     * Adds or replaces a script with the given name.
     *
     * @param name the script name
     * @param script the script instance
     * @return the previous script if any, null otherwise
     * @throws IllegalArgumentException if name or script is null/empty
     */
    public Script addScript(String name, Script script) {
        if (isNullOrEmpty(name) || script == null) {
            throw new IllegalArgumentException("Script name or instance cannot be null or empty");
        }
        return scripts.put(name, script);
    }

    /**
     * Checks if a script exists.
     *
     * @param name the script name
     * @return true if the script exists, false otherwise
     */
    public boolean containsScript(String name) {
        return !isNullOrEmpty(name) && scripts.containsKey(name);
    }

    /**
     * Checks if a script has finished execution.
     *
     * @param name the script name
     * @return true if the script exists and is finished
     */
    public boolean finished(String name) {
        return !isNullOrEmpty(name) && getScript(name).map(Script::isFinished).orElse(false);
    }

    /**
     * Removes a script from the manager.
     *
     * @param name the script name
     */
    public void removeScript(String name) {
        if (!isNullOrEmpty(name)) {
            scripts.remove(name);
        }
    }

    /**
     * Cleans up a specific script without removing it.
     *
     * @param name the script name
     */
    public void cleanupScript(String name) {
        if (!isNullOrEmpty(name)) {
            scripts.computeIfPresent(name, (k, script) -> {
                script.cleanup();
                return script;
            });
        }
    }

    /**
     * Cleans up all scripts without removing them.
     */
    public void cleanupAll() {
        scripts.forEach((k, script) -> script.cleanup());
    }

    /**
     * Removes all scripts from the manager.
     */
    public void clearAll() {
        scripts.clear();
    }

    /**
     * Updates a script unconditionally.
     *
     * @param name the script name
     */
    public void updateScript(String name) {
        updateScript(name, () -> true);
    }

    /**
     * Updates a script if the condition is met.
     *
     * @param name the script name
     * @param condition the condition to check before updating
     */
    public void updateScript(String name, Supplier<Boolean> condition) {
        if (condition.get() && !isNullOrEmpty(name)) {
            scripts.computeIfPresent(name, (k, script) -> {
                script.update();
                return script;
            });
        }
    }

    /**
     * Updates all managed scripts.
     */
    public void updateAll() {
        scripts.values().forEach(Script::update);
    }

    /**
     * Gets all script names.
     *
     * @return unmodifiable set of script names
     */
    public Set<String> getAllScriptNames() {
        return Collections.unmodifiableSet(scripts.keySet());
    }

    /**
     * Gets all scripts.
     *
     * @return unmodifiable map of all scripts
     */
    public Map<String, Script> getAllScripts() {
        return Collections.unmodifiableMap(scripts);
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
