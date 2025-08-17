package io.github.mcengine.api.hologram.extension.agent;

import org.bukkit.plugin.Plugin;

/**
 * Represents a Hologram-based Agent module that can be dynamically loaded into the MCEngine.
 * <p>
 * Implement this interface to integrate hologram-related agents into the system.
 */
public interface IMCEngineHologramAgent {
    /**
     * Called when the Hologram Agent is loaded by the engine.
     *
     * @param plugin The plugin instance providing context.
     */
    void onLoad(Plugin plugin);

    /**
     * Called when the Hologram Agent is unloaded or disabled by the engine.
     *
     * @param plugin The plugin instance providing context.
     */
    void onDisload(Plugin plugin);

    /**
     * Sets a unique ID for this Hologram Agent instance.
     *
     * @param id The unique ID assigned by the engine.
     */
    void setId(String id);
}
