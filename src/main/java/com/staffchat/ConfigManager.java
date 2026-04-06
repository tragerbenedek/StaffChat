package com.staffchat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Loads and exposes the values from {@code config.yml}.
 * Also provides a helper to translate legacy {@code &} colour codes into
 * Adventure {@link Component}s.
 */
public class ConfigManager {

    /** Serialiser that converts {@code &} colour codes to Adventure components. */
    private static final LegacyComponentSerializer LEGACY =
            LegacyComponentSerializer.legacyAmpersand();

    private final StaffChatPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(StaffChatPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    /** (Re)loads the configuration from disk. */
    public void reload() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    // -------------------------------------------------------------------------
    // Config accessors
    // -------------------------------------------------------------------------

    /** Returns the raw staff-chat prefix string (e.g. {@code "&8[&bStaffChat&8]"}). */
    public String getStaffChatPrefix() {
        return config.getString("staff-chat-prefix", "&8[&bStaffChat&8]");
    }

    /** Returns the raw staff-msg prefix string (e.g. {@code "&8[&dStaffMsg&8]"}). */
    public String getStaffMsgPrefix() {
        return config.getString("staff-msg-prefix", "&8[&dStaffMsg&8]");
    }

    /**
     * Returns a map from role label (e.g. "OWNER") to its colour code string
     * (e.g. "&4").  Falls back to sensible defaults when keys are absent.
     */
    public Map<String, String> getRoleColors() {
        Map<String, String> colors = new HashMap<>();
        colors.put("OWNER",  config.getString("role-colors.OWNER",  "&4"));
        colors.put("ADMIN",  config.getString("role-colors.ADMIN",  "&c"));
        colors.put("DEV",    config.getString("role-colors.DEV",    "&b"));
        colors.put("MOD",    config.getString("role-colors.MOD",    "&2"));
        colors.put("HELPER", config.getString("role-colors.HELPER", "&e"));
        colors.put("STAFF",  config.getString("role-colors.STAFF",  "&7"));
        return colors;
    }

    /**
     * Returns the colour code string for the given role label.
     * Defaults to {@code "&7"} if the role is unknown.
     */
    public String getRoleColor(String role) {
        return getRoleColors().getOrDefault(role, "&7");
    }

    /** Whether players in staff-chat mode should hide normal public chat. */
    public boolean isHideNormalChat() {
        return config.getBoolean("hide-normal-chat", true);
    }

    /** Whether staff-chat messages should be echoed to the server console. */
    public boolean isLogToConsole() {
        return config.getBoolean("log-to-console", true);
    }

    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------

    /**
     * Translates a string with legacy {@code &} colour codes into an Adventure
     * {@link Component}.
     *
     * @param text the string to colourize, e.g. {@code "&aHello!"}
     * @return the corresponding Adventure component
     */
    public Component colorize(String text) {
        return LEGACY.deserialize(text);
    }
}
