package io.github.mcengine.api.hologram.extension.api;

import org.bukkit.plugin.Plugin;

/**
 * Represents a Hologram-based API module that can be dynamically loaded into the MCEngine.
 * <p>
 * Implement this interface to provide hologram-related APIs to the system.
 */
public interface IMCEngineHologramAPI {
    /**
     * Called when the Hologram API is loaded by the engine.
     *
     * @param plugin The plugin instance providing context.
     */
    void onLoad(Plugin plugin);

    /**
     * Called when the Hologram API is unloaded or disabled by the engine.
     *
     * @param plugin The plugin instance providing context.
     */
    void onDisload(Plugin plugin);

    /**
     * Sets a unique ID for this Hologram API instance.
     *
     * @param id The unique ID assigned by the engine.
     */
    void setId(String id);
}
