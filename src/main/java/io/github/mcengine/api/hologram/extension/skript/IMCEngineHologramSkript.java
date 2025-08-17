package io.github.mcengine.api.hologram.extension.skript;

import org.bukkit.plugin.Plugin;

/**
 * Represents a Hologram-based Skript module that can be dynamically loaded into the MCEngine.
 * <p>
 * Implement this interface to integrate scripted hologram content into the system.
 */
public interface IMCEngineHologramSkript {
    /**
     * Called when the Hologram Skript module is loaded by the engine.
     *
     * @param plugin The plugin instance providing context.
     */
    void onLoad(Plugin plugin);

    /**
     * Called when the Hologram Skript module is unloaded or disabled by the engine.
     *
     * @param plugin The plugin instance providing context.
     */
    void onDisload(Plugin plugin);

    /**
     * Sets a unique ID for this Hologram Skript instance.
     *
     * @param id The unique ID assigned by the engine.
     */
    void setId(String id);
}
