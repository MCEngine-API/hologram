package io.github.mcengine.api.hologram.extension.library;

import org.bukkit.plugin.Plugin;

/**
 * Represents a Hologram-based Library module that can be dynamically loaded into the MCEngine.
 * <p>
 * Implement this interface to provide hologram-related library support to the system.
 */
public interface IMCEngineHologramLibrary {
    /**
     * Called when the Hologram Library is loaded by the engine.
     *
     * @param plugin The plugin instance providing context.
     */
    void onLoad(Plugin plugin);

    /**
     * Called when the Hologram Library is unloaded or disabled by the engine.
     *
     * @param plugin The plugin instance providing context.
     */
    void onDisload(Plugin plugin);

    /**
     * Sets a unique ID for this Hologram Library instance.
     *
     * @param id The unique ID assigned by the engine.
     */
    void setId(String id);
}
