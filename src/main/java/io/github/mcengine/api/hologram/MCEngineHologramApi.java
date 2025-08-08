package io.github.mcengine.api.hologram;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Simple hologram helper for showing command usage in front of a player and
 * providing a clickable pre-filled command prompt.
 */
public class MCEngineHologramApi {

    /**
     * Horizontal distance (in blocks) in front of the player's eyes where the hologram is spawned.
     */
    private static final double HOLOGRAM_FORWARD = 1.6D;

    /**
     * Vertical offset from the player's eye height for the base line of the hologram.
     * Negative values move the hologram down (closer to chest level).
     */
    private static final double HOLOGRAM_VERTICAL_OFFSET = -0.9D;

    /**
     * Vertical spacing (in blocks) between hologram lines (one {@link ArmorStand} per line).
     */
    private static final double HOLOGRAM_LINE_SPACING = 0.27D;

    /**
     * Lifespan of the hologram (in seconds) before it automatically despawns.
     */
    private static final int DEFAULT_LIFETIME_SECONDS = 10;

    /**
     * Creates and displays a multi-line usage hologram in front of the player. When the player
     * right-clicks any line of the hologram, a clickable chat component is sent that uses
     * {@link ClickEvent.Action#SUGGEST_COMMAND} to pre-fill the player's chat with the given command prefix.
     *
     * @param player            the target player who should see and interact with the hologram
     * @param command_pre_prompt the command prefix to pre-fill in the player's chat (e.g. {@code "/currency default "})
     * @param usages            lines of usage text; one entry per line (displayed under the header)
     */
    public void getUsageHologram(Player player, String command_pre_prompt, String[] usages) {
        if (player == null) return;

        // Resolve the hosting plugin from the providing JAR to schedule tasks & register listeners.
        final Plugin plugin = JavaPlugin.getProvidingPlugin(MCEngineHologramApi.class);

        // Compose visible lines: title + "Usage:" + provided lines + click hint.
        List<String> lines = new ArrayList<>();
        lines.add(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Invalid command");
        lines.add(ChatColor.GOLD + "Usage:");
        if (usages != null) {
            for (String u : usages) {
                if (u != null && !u.isEmpty()) {
                    // Keep the color flexible: if the caller already provided colors, keep them;
                    // otherwise make it gray for readability.
                    lines.add(u.startsWith("§") || u.startsWith(ChatColor.COLOR_CHAR + "")
                            ? u
                            : ChatColor.GRAY + u);
                }
            }
        }
        lines.add(ChatColor.YELLOW + "Right-click to get a command prompt");

        // Base location: in front of eyes, lowered vertically so the first line isn't too high.
        Location eye = player.getEyeLocation();
        Location base = eye.add(eye.getDirection().normalize().multiply(HOLOGRAM_FORWARD))
                           .add(0, HOLOGRAM_VERTICAL_OFFSET, 0);

        // Spawn one ArmorStand per line, stacked downward from the base.
        Set<UUID> standIds = new HashSet<>();
        List<ArmorStand> spawned = new ArrayList<>(lines.size());

        for (int i = 0; i < lines.size(); i++) {
            final String text = lines.get(i);
            Location lineLoc = base.clone().subtract(0, i * HOLOGRAM_LINE_SPACING, 0);

            // Keep a hitbox (marker=false) so PlayerInteractAtEntityEvent fires; use larger hitbox (small=false).
            ArmorStand stand = player.getWorld().spawn(lineLoc, ArmorStand.class, as -> {
                as.setInvisible(true);
                as.setMarker(false);
                as.setSmall(false);
                as.setGravity(false);
                as.setCollidable(false);
                as.setInvulnerable(true);
                as.setCustomNameVisible(true);
                as.setCustomName(text);
            });

            spawned.add(stand);
            standIds.add(stand.getUniqueId());
        }

        // Single-use listener for right-clicks on any of the spawned stands by this player.
        Listener listener = new Listener() {
            @EventHandler(ignoreCancelled = true)
            public void onInteract(PlayerInteractAtEntityEvent event) {
                if (!event.getPlayer().getUniqueId().equals(player.getUniqueId())) return;
                if (!standIds.contains(event.getRightClicked().getUniqueId())) return;

                event.setCancelled(true);

                TextComponent tip = new TextComponent(
                        ChatColor.YELLOW + "Click to prefill: " + ChatColor.WHITE + command_pre_prompt
                );
                tip.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command_pre_prompt));
                player.spigot().sendMessage(tip);
            }
        };

        Bukkit.getPluginManager().registerEvents(listener, plugin);

        // Cleanup all stands & listener after the default lifetime.
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (ArmorStand stand : spawned) {
                if (stand != null && !stand.isDead()) stand.remove();
            }
            HandlerList.unregisterAll(listener);
        }, DEFAULT_LIFETIME_SECONDS * 20L);
    }
}
