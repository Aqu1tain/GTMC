# SeasonalBundle - Paper Minecraft Plugin

A Paper Minecraft 1.21.10 plugin that gives players enchanted bundles to store items across seasons.

## Features

- **Seasonal Bundles**: Players receive an enchanted bundle when joining
- **One-Item Capacity**: Bundles can only hold 1 item (not a stack)
- **Item Validation**: Prevents storing elytras, shulker boxes, or other bundles
- **Season Persistence**: Items are saved at the end of a season and restored at the start of the next
- **Rare Relics**: Previous season items appear as rare, enchanted relics in season bundles
- **Admin Commands**: Easy season management with commands

## Plugin Structure

```
src/main/java/com/gtmc/seasonalbundle/
├── SeasonalBundlePlugin.java          # Main plugin class
├── commands/
│   └── SeasonCommand.java             # Season management commands
├── data/
│   └── SeasonDataManager.java         # Seasonal data persistence
├── listeners/
│   ├── PlayerJoinListener.java        # Player join event handling
│   └── ItemValidationListener.java    # Item validation for bundles
└── util/
    └── BundleUtil.java                # Bundle creation and utilities
```

## Setup & Build

### Prerequisites

- Java 21 or higher
- Git

### Building

#### Option 1: Using Maven Wrapper (Recommended)

```bash
# Windows
mvnw.cmd clean package

# Linux/Mac
./mvnw clean package
```

#### Option 2: Using Maven (if installed)

```bash
mvn clean package
```

The compiled plugin JAR will be in `target/seasonal-bundle-1.0.0.jar`

### Installation

1. Build the plugin (see above)
2. Copy the JAR file to your Paper server's `plugins/` directory
3. Restart or reload the server
4. The plugin will create configuration files automatically

## Commands

### Admin Commands (require `seasonalbundle.admin` permission)

- `/seasonend` - End the current season and save player items
- `/seasonstart` - Start a new season with saved items
- `/bundlereload` - Reload plugin configuration

## Configuration

### config.yml

```yaml
current-season: 1

bundle:
  max-slots: 1
  blacklist:
    - ELYTRA
    - SHULKER_BOX
    - # ... other shulker box variants
```

### seasons.yml (Auto-generated)

Stores player items for each season. Format:
```yaml
current-season: 1
seasons:
  1:
    player-items:
      [uuid]: [ItemStack YAML]
```

## How It Works

### Season Lifecycle

1. **Season Start**: Players join and receive an enchanted bundle
   - New players: Empty bundle
   - Returning players: Bundle with previous season's saved item

2. **Mid-Season**: Players can place 1 item in their bundle
   - Invalid items are blocked
   - Stack validation prevents storing multiple items

3. **Season End**: Admin runs `/seasonend`
   - All player bundles are searched
   - Items are extracted and saved to seasons.yml
   - Server displays notifications

4. **New Season**: Admin runs `/seasonstart`
   - Season number increments
   - Saved items are prepared for next season

### Item Restrictions

**Blocked Items:**
- Elytras
- Shulker Boxes (all variants)
- Bundles (prevent nesting)

**Allowed Items:**
- Any other item (diamonds, netherite gear, custom items, etc.)

## Development

### Adding Features

1. Create feature branch: `git checkout -b feat/feature-name`
2. Implement changes (make atomic commits)
3. Test thoroughly
4. Create commit with conventional message: `feat: description`
5. Push and create PR

### Commit Conventions

- `feat:` - New features
- `fix:` - Bug fixes
- `refactor:` - Code improvements
- `docs:` - Documentation
- `test:` - Testing

Example:
```bash
git commit -m "feat: add bundle enchantment customization"
```

## Troubleshooting

### Plugin won't load
- Check Java version is 21+
- Verify plugin.yml syntax
- Check server logs for errors

### Items not saving
- Verify `seasons.yml` exists in `plugins/SeasonalBundle/`
- Check file permissions
- Ensure `/seasonend` was called before season change

### Players not getting bundles
- Verify `SeasonalBundle` shows in `/plugins`
- Check player join logs
- Ensure players have space in inventory

## License

Developed for GTMC Server
