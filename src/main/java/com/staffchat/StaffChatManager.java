package com.staffchat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Manages the staff-chat toggle state and provides utility methods for
 * detecting staff members and resolving their rank.
 */
public class StaffChatManager {

    /**
     * Role definitions ordered from highest to lowest priority.
     * The first matching permission wins when determining a player's label.
     */
    private static final String[] ROLE_PERMISSIONS = {
            "staffchat.owner",
            "staffchat.admin",
            "staffchat.dev",
            "staffchat.mod",
            "staffchat.helper",
            "staffchat.staff"
    };

    /** Maps each permission node to its human-readable role label. */
    private static final Map<String, String> PERMISSION_TO_ROLE;

    static {
        PERMISSION_TO_ROLE = new LinkedHashMap<>();
        PERMISSION_TO_ROLE.put("staffchat.owner",  "OWNER");
        PERMISSION_TO_ROLE.put("staffchat.admin",  "ADMIN");
        PERMISSION_TO_ROLE.put("staffchat.dev",    "DEV");
        PERMISSION_TO_ROLE.put("staffchat.mod",    "MOD");
        PERMISSION_TO_ROLE.put("staffchat.helper", "HELPER");
        PERMISSION_TO_ROLE.put("staffchat.staff",  "STAFF");
    }

    /** UUIDs of players who currently have staff-chat mode toggled ON. */
    private final Set<UUID> toggledPlayers = new HashSet<>();

    @SuppressWarnings("unused")
    private final StaffChatPlugin plugin;

    public StaffChatManager(StaffChatPlugin plugin) {
        this.plugin = plugin;
    }

    // -------------------------------------------------------------------------
    // Toggle state
    // -------------------------------------------------------------------------

    /**
     * Toggles staff-chat mode for the given UUID.
     *
     * @param uuid the player's UUID
     * @return {@code true} if staff-chat is now ON, {@code false} if now OFF
     */
    public boolean toggle(UUID uuid) {
        if (toggledPlayers.contains(uuid)) {
            toggledPlayers.remove(uuid);
            return false;
        }
        toggledPlayers.add(uuid);
        return true;
    }

    /** Returns whether the given UUID has staff-chat mode currently ON. */
    public boolean isToggled(UUID uuid) {
        return toggledPlayers.contains(uuid);
    }

    /** Removes the player from the toggle set (called on quit). */
    public void removePlayer(UUID uuid) {
        toggledPlayers.remove(uuid);
    }

    // -------------------------------------------------------------------------
    // Staff detection & role resolution
    // -------------------------------------------------------------------------

    /**
     * Returns {@code true} if the player holds at least one staff role
     * permission.
     */
    public boolean isStaff(Player player) {
        for (String perm : ROLE_PERMISSIONS) {
            if (player.hasPermission(perm)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the highest role label for the player (e.g. "OWNER", "MOD").
     * Returns {@code "STAFF"} as the fallback if no specific role is found
     * (though {@link #isStaff} should be checked first).
     */
    public String getRole(Player player) {
        for (String perm : ROLE_PERMISSIONS) {
            if (player.hasPermission(perm)) {
                return PERMISSION_TO_ROLE.get(perm);
            }
        }
        return "STAFF";
    }

    /**
     * Returns all currently online staff members, sorted by rank from
     * highest to lowest.
     */
    public List<Player> getOnlineStaff() {
        List<Player> staff = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isStaff(player)) {
                staff.add(player);
            }
        }
        // Sort by role index (lower index = higher rank)
        staff.sort(Comparator.comparingInt(this::getRoleIndex));
        return staff;
    }

    /**
     * Returns the index of the player's highest role in {@link #ROLE_PERMISSIONS}.
     * Lower index means higher rank.
     */
    private int getRoleIndex(Player player) {
        for (int i = 0; i < ROLE_PERMISSIONS.length; i++) {
            if (player.hasPermission(ROLE_PERMISSIONS[i])) {
                return i;
            }
        }
        return ROLE_PERMISSIONS.length; // non-staff fallback (should not happen)
    }
}
