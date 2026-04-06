package com.staffchat;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Iterator;

/**
 * Listens to chat and quit events.
 *
 * <ul>
 *   <li>If the sender has staff-chat toggled ON the message is cancelled and
 *       re-sent only to online staff members.</li>
 *   <li>If the sender is in normal chat, staff members with
 *       {@code hide-normal-chat} active are removed from the viewer set so
 *       they do not see the public message.</li>
 *   <li>On quit, the player is removed from the toggle set.</li>
 * </ul>
 */
public class ChatListener implements Listener {

    private final StaffChatPlugin plugin;
    private final ConfigManager configManager;
    private final StaffChatManager staffChatManager;

    public ChatListener(StaffChatPlugin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.staffChatManager = plugin.getStaffChatManager();
    }

    // -------------------------------------------------------------------------
    // AsyncChatEvent
    // -------------------------------------------------------------------------

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event) {
        Player sender = event.getPlayer();

        if (staffChatManager.isToggled(sender.getUniqueId())) {
            // -----------------------------------------------------------------
            // Staff-chat mode ON → intercept and redirect
            // -----------------------------------------------------------------
            event.setCancelled(true);

            String role = staffChatManager.getRole(sender);
            String roleColor = configManager.getRoleColor(role);
            String prefix = configManager.getStaffChatPrefix();

            // Extract the plain text of the message the player typed
            String plainMessage = PlainTextComponentSerializer.plainText()
                    .serialize(event.message());

            // Build:  <prefix> <roleColor>[ROLE]</roleColor> <white>name</white> &7» <white>message</white>
            String formatted = prefix
                    + " " + roleColor + "[" + role + "] "
                    + "&f" + sender.getName()
                    + " &7\u00bb &f" + plainMessage;

            Component component = configManager.colorize(formatted);

            // Send to all online staff
            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staffChatManager.isStaff(staff)) {
                    staff.sendMessage(component);
                }
            }

            // Log to console if configured
            if (configManager.isLogToConsole()) {
                Bukkit.getConsoleSender().sendMessage(component);
            }

        } else {
            // -----------------------------------------------------------------
            // Normal chat → remove toggled-in staff from viewer set if configured
            // -----------------------------------------------------------------
            if (configManager.isHideNormalChat()) {
                Iterator<net.kyori.adventure.audience.Audience> viewers =
                        event.viewers().iterator();
                while (viewers.hasNext()) {
                    net.kyori.adventure.audience.Audience viewer = viewers.next();
                    if (viewer instanceof Player p
                            && staffChatManager.isToggled(p.getUniqueId())) {
                        viewers.remove();
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // PlayerQuitEvent
    // -------------------------------------------------------------------------

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        staffChatManager.removePlayer(event.getPlayer().getUniqueId());
    }
}
