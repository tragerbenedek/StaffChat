package com.staffchat.commands;

import com.staffchat.ConfigManager;
import com.staffchat.StaffChatManager;
import com.staffchat.StaffChatPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Handles {@code /stafflist} (alias {@code /sl}).
 *
 * <p>Displays all currently online staff members sorted by rank
 * (highest first).</p>
 */
public class StaffListCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final StaffChatManager staffChatManager;

    public StaffListCommand(StaffChatPlugin plugin) {
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

        List<Player> onlineStaff = staffChatManager.getOnlineStaff();

        if (onlineStaff.isEmpty()) {
            player.sendMessage(configManager.colorize(
                    "&cNo staff members are currently online."));
            return true;
        }

        // Header
        player.sendMessage(configManager.colorize("&b--- Online Staff ---"));

        // One line per staff member
        for (Player staff : onlineStaff) {
            String role = staffChatManager.getRole(staff);
            String roleColor = configManager.getRoleColor(role);
            player.sendMessage(configManager.colorize(
                    roleColor + "[" + role + "] &f" + staff.getName()));
        }

        // Footer
        player.sendMessage(configManager.colorize("&b--------------------"));

        return true;
    }
}
