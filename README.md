# StaffChat

A Paper/Spigot (1.20+) plugin that provides staff-only chat, direct staff messaging, and an online staff list.

---

## Features

- **Staff Chat Toggle** (`/sc`) — route all your messages to staff-only chat.
- **Staff Direct Messages** (`/sm`) — privately message another staff member.
- **Staff List** (`/sl`) — view all online staff sorted by rank.
- **Permission-based roles** — OWNER › ADMIN › DEV › MOD › HELPER › STAFF.
- **Configurable prefixes, role colours, and behaviour** via `config.yml`.
- **Console logging** of staff chat messages (optional).
- **Hide normal chat** from staff members who have toggled staff-chat on (optional).

---

## Commands

| Command | Alias | Description | Permission |
|---------|-------|-------------|------------|
| `/staffchat` | `/sc` | Toggle staff chat mode on/off | `staffchat.use` |
| `/staffmsg <player> <message>` | `/sm` | Send a private message to a staff member | `staffchat.msg` |
| `/stafflist` | `/sl` | List all online staff members | `staffchat.use` |

---

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `staffchat.use` | Use `/staffchat` and `/stafflist` | op |
| `staffchat.msg` | Use `/staffmsg` | op |
| `staffchat.staff` | Staff role | false |
| `staffchat.helper` | Helper role | false |
| `staffchat.mod` | Moderator role | false |
| `staffchat.admin` | Admin role | false |
| `staffchat.dev` | Developer role | false |
| `staffchat.owner` | Owner role | false |

---

## Configuration (`config.yml`)

```yaml
# Prefix shown before staff chat messages
staff-chat-prefix: "&8[&bStaffChat&8]"

# Prefix shown before staff direct messages
staff-msg-prefix: "&8[&dStaffMsg&8]"

# Color codes for each role label
role-colors:
  OWNER: "&4"
  ADMIN: "&c"
  DEV: "&b"
  MOD: "&2"
  HELPER: "&e"
  STAFF: "&7"

# Whether players in staff chat mode should hide normal public chat
hide-normal-chat: true

# Whether staff chat messages should be logged to the console
log-to-console: true
```

---

## Example Output

**Staff Chat message:**
```
[StaffChat] [DEV] trager » Hello team!
```

**Staff Direct Message:**
```
[StaffMsg] [DEV] trager → [ADMIN] Alex: Can you check player X?
```

**Staff List:**
```
--- Online Staff ---
[OWNER] Notch
[ADMIN] Alex
[MOD] Steve
[DEV] trager
--------------------
```

---

## Building

Requires Java 17 and Maven.

```bash
mvn clean package
```

The compiled jar will be placed in `target/StaffChat-1.0.0.jar`.

---

## Installation

1. Copy `target/StaffChat-1.0.0.jar` into your server's `plugins/` folder.
2. Restart (or reload) the server.
3. A `plugins/StaffChat/config.yml` will be generated automatically.
4. Assign staff-role permissions to players/groups via your permissions plugin (e.g. LuckPerms).

---

## Author

tragerbenedek