# DimensionManager

**DimensionManager** is a lightweight, feature-packed Paper/Spigot plugin designed to give server administrators full control over Minecraft dimensions. It allows you to dynamically open or close the Overworld, Nether, and The End in real time, complete with customizable broadcast alerts, sound effects, live tablist status tracking, and portal protection.

---

## 🌟 Features

- **Dynamic Dimension Control**: Easily open or close individual dimensions (`overworld`, `nether`, `end`) on the fly without server restarts.
- **Silent Portal Protection**: Prevents players, mobs, and entities from traveling through Nether portals, End portals, or gateways into closed worlds.
- **Broadcast Alerts & Sounds**: Notify all online players with configurable chat messages and sounds when a dimension opens or closes.
- **Live Tablist Integration**: Displays real-time dimension availability in the player list footer with full color code support (`&` and `§`).
- **In-Game Config Overview**: Run `/dm config` to see a formatted, color-coded overview of your entire configuration directly in chat.
- **Full Tab-Completion**: Fast, permission-aware tab completion for all commands and arguments.

---

## 📥 Installation

1. Download the latest `DimensionManager.jar`.
2. Place the jar into your server's `plugins/` folder.
3. Start or reload the server.
4. Modify `plugins/DimensionManager/config.yml` to your liking and reload using `/dm config reload`.

---

## 💻 Commands

| Command | Permission | Description |
|---|---|---|
| `/dm config` | `dimensionmanager.command` | Displays a formatted overview of the current configuration in chat. |
| `/dm config reload` | `dimensionmanager.config.reload` | Reloads all settings from `config.yml`. |
| `/dm <overworld\|nether\|end> open` | `dimensionmanager.dimension.manage` | Opens the specified dimension. |
| `/dm <overworld\|nether\|end> close` | `dimensionmanager.dimension.manage` | Closes the specified dimension and blocks portal entry. |
| `/dm <overworld\|nether\|end> info` | `dimensionmanager.dimension.manage` | Checks whether the specified dimension is currently open or closed. |

> *Note: `/dimensionmanager` can be used interchangeably with `/dm`.*

---

## 🔑 Permissions

| Permission | Description | Default |
|---|---|---|
| `dimensionmanager.admin` | Full administrative access to all commands and features. | `op` |
| `dimensionmanager.command` | Allows basic access to `/dm` and `/dm config`. | `op` |
| `dimensionmanager.config.reload` | Allows reloading the plugin configuration. | `op` |
| `dimensionmanager.dimension.manage` | Allows opening, closing, and querying dimension statuses. | `op` |
| `dimensionmanager.bypass` | Allows administrators to bypass dimension restrictions when using teleport commands (`/tp`). | `op` |

---

## ⚙️ Example `config.yml`

```yaml
dimensions:
  nether:
    is-open: true
  overworld:
    is-open: true
  end:
    is-open: true


tablist:
  enabled: true
  format: "\n Overworld: §a$overworld | Nether: §a$nether | End: §a$end"
  nether:
    open: "§aOpen"
    closed: "§cClosed"
  overworld:
    open: "§aOpen"
    closed: "§cClosed"
  end:
    open: "§aOpen"
    closed: "§cClosed"

alerts:
  enabled: true
  format:
    prefix: §7[§bDimensionManager§7] §r
    end-open: §aThe End is now open!
    end-close: §cThe End is now closed!
    nether-open: §aThe Nether is now open!
    nether-close: §cThe Nether is now closed!
    overworld-open: §aThe Overworld is now open!
    overworld-close: §cThe Overworld is now closed!

sound:
  enabled: true
  volume: 0.1
  format:
    end-open: ENTITY_ENDER_DRAGON_GROWL
    end-close: ENTITY_ENDER_DRAGON_DEATH
    nether-open: ENTITY_WITHER_SPAWN
    nether-close: ENTITY_WITHER_DEATH
    overworld-open: ENTITY_PLAYER_LEVELUP
    overworld-close: ENTITY_PLAYER_HURT
```

### Configuration Breakdown

- **`dimensions`**: Defines whether a dimension is currently open (`true`) or closed (`false`).
- **`tablist`**:
  - `enabled`: Toggles the tablist footer display.
  - `format`: Template for the footer text. Supports `$overworld`, `$nether`, and `$end` placeholders, as well as color codes (`&` / `§`).
  - `<dimension>.open` / `<dimension>.closed`: The display text replacing the respective placeholder depending on dimension state.
- **`alerts`**:
  - `enabled`: Toggles server-wide chat broadcasts when dimension states change.
  - `prefix`: Prefix added before broadcast messages.
  - `format.<dimension>-<open|close>`: The message broadcast when a dimension is opened or closed.
- **`sound`**:
  - `enabled`: Toggles sound effects played to all players on state changes.
  - `volume`: Playback volume for all broadcast sounds (e.g. `0.1` for subtle alerts).
  - `format.<dimension>-<open|close>`: The Bukkit `Sound` enum name (e.g., `ENTITY_WITHER_SPAWN`).

---

## 🛠️ Building from Source

This project uses Gradle with Kotlin DSL. To compile the plugin:

```bash
./gradlew build
```

The compiled jar file will be located in `build/libs/`.

---

## 👤 Author

Developed by **melonengolemm**.
https://github.com/ldrnico
