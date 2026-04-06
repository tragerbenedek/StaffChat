package com.staffchat;

import com.staffchat.commands.StaffChatCommand;
import com.staffchat.commands.StaffListCommand;
import com.staffchat.commands.StaffMsgCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for StaffChat.
 * Initialises all managers, registers the chat listener, and binds commands.
 */
public class StaffChatPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private StaffChatManager staffChatManager;

    @Override
    public void onEnable() {
        // Save the default config.yml if it does not exist yet
        saveDefaultConfig();

        // Initialise helpers
        configManager = new ConfigManager(this);
        staffChatManager = new StaffChatManager(this);

        // Register the chat / quit listener
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        // Register commands
        getCommand("staffchat").setExecutor(new StaffChatCommand(this));
        getCommand("staffmsg").setExecutor(new StaffMsgCommand(this));
        getCommand("stafflist").setExecutor(new StaffListCommand(this));

        getLogger().info("StaffChat has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("StaffChat has been disabled.");
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /** Returns the ConfigManager instance. */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /** Returns the StaffChatManager instance. */
    public StaffChatManager getStaffChatManager() {
        return staffChatManager;
    }
}
