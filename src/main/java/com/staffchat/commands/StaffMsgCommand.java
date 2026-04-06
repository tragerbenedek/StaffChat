package com.staffchat.commands;

import com.staffchat.ConfigManager;
import com.staffchat.StaffChatManager;
import com.staffchat.StaffChatPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Handles {@code /staffmsg} (alias {@code /sm}).
 *
 * <p>Sends a private message from one staff member to another.  Both sender
 * and target must hold at least one staff-role permission.</p>
 */
public class StaffMsgCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final StaffChatManager staffChatManager;

    public StaffMsgCommand(StaffChatPlugin plugin) {
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
        if (!player.hasPermission("staffchat.msg")) {
            player.sendMessage(configManager.colorize(
                    "&cYou don't have permission to use this command."));
            return true;
        }

        // Staff-role check for sender
        if (!staffChatManager.isStaff(player)) {
            player.sendMessage(configManager.colorize(
                    "&cYou are not a staff member."));
            return true;
        }

        // Argument validation: need at least <player> and one word of message
        if (args.length < 2) {
            player.sendMessage(configManager.colorize(
                    "&cUsage: /staffmsg <player> <message>"));
            return true;
        }

        // Target lookup
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(configManager.colorize(
                    "&cThat player is not online."));
            return true;
        }

        // Target must be a staff member
        if (!staffChatManager.isStaff(target)) {
            player.sendMessage(configManager.colorize(
                    "&cThat player is not a staff member."));
            return true;
        }

        // Reconstruct message from remaining arguments (everything after the player name)
        String rawMessage = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        String senderRole = staffChatManager.getRole(player);
        String targetRole = staffChatManager.getRole(target);
        String senderRoleColor = configManager.getRoleColor(senderRole);
        String targetRoleColor = configManager.getRoleColor(targetRole);
        String prefix = configManager.getStaffMsgPrefix();

        // Format:  <prefix> <senderColor>[SENDER_ROLE]</> SenderName &7→ <targetColor>[TARGET_ROLE]</> TargetName&7: &fMessage
        String formatted = prefix
                + " " + senderRoleColor + "[" + senderRole + "] "
                + "&f" + player.getName()
                + " &7\u2192 "
                + targetRoleColor + "[" + targetRole + "] "
                + "&f" + target.getName()
                + "&7: &f" + rawMessage;

        Component component = configManager.colorize(formatted);

        // Deliver to both parties
        player.sendMessage(component);
        target.sendMessage(component);

        // Log to console if configured
        if (configManager.isLogToConsole()) {
            Bukkit.getConsoleSender().sendMessage(component);
        }

        return true;
    }
}
