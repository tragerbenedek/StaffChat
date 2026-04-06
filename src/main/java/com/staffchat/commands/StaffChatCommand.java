package com.staffchat.commands;

import com.staffchat.ConfigManager;
import com.staffchat.StaffChatManager;
import com.staffchat.StaffChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Handles {@code /staffchat} (alias {@code /sc}).
 *
 * <p>Toggles staff-chat mode on/off for the executing player.  Requires the
 * {@code staffchat.use} permission <em>and</em> at least one staff role
 * permission.</p>
 */
public class StaffChatCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final StaffChatManager staffChatManager;

    public StaffChatCommand(StaffChatPlugin plugin) {
        this.configManager = plugin.getConfigManager();
        this.staffChatManager = plugin.getStaffChatManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {

        // Console check
        if (!(sender instanceof Player player)) {
            sender.sendMessage(configManager.colorize(
                    "&cThis command can only be used by players."));
            return true;
        }

        // Permission check
        if (!player.hasPermission("staffchat.use")) {
            player.sendMessage(configManager.colorize(
                    "&cYou don't have permission to use this command."));
            return true;
        }

        // Staff-role check
        if (!staffChatManager.isStaff(player)) {
            player.sendMessage(configManager.colorize(
                    "&cYou are not a staff member."));
            return true;
        }

        // Toggle and inform
        boolean nowEnabled = staffChatManager.toggle(player.getUniqueId());
        if (nowEnabled) {
            player.sendMessage(configManager.colorize("&aStaff chat mode enabled."));
        } else {
            player.sendMessage(configManager.colorize("&cStaff chat mode disabled."));
        }

        return true;
    }
}
