# SeasonalBundle - Development Guide

This guide helps developers understand and extend the SeasonalBundle plugin.

## Project Architecture

### Core Components

#### 1. SeasonalBundlePlugin (Main Class)
- Entry point for the plugin
- Initializes all managers and listeners
- Handles plugin lifecycle (enable/disable)

#### 2. SeasonDataManager
**Location**: `src/main/java/com/gtmc/seasonalbundle/data/SeasonDataManager.java`

Responsible for:
- Managing season state
- Persisting player seasonal items to YAML
- Loading seasonal data on startup
- Coordinating season transitions

Key Methods:
- `load()` - Load season data from file
- `save()` - Save season data to file
- `savePlayerItem()` - Store a player's item
- `getPlayerItem()` - Retrieve a player's saved item
- `endSeason()` - Mark season as ended
- `startNewSeason()` - Increment season and prepare for next

#### 3. BundleUtil
**Location**: `src/main/java/com/gtmc/seasonalbundle/util/BundleUtil.java`

Provides utility functions for:
- Creating new seasonal bundles with proper metadata
- Creating rare relics from previous season items
- Validating items before storing
- Checking bundle capacity

Key Methods:
- `createBundle()` - Create empty seasonal bundle
- `createRareBundle()` - Create bundle with previous season item
- `isValidBundleItem()` - Validate item eligibility
- `isBundleAtCapacity()` - Check if bundle is full

#### 4. PlayerJoinListener
**Location**: `src/main/java/com/gtmc/seasonalbundle/listeners/PlayerJoinListener.java`

Handles player join events:
- Checks if player has saved item from previous season
- Gives rare bundle with saved item OR empty bundle
- Sends welcome messages to players

#### 5. ItemValidationListener
**Location**: `src/main/java/com/gtmc/seasonalbundle/listeners/ItemValidationListener.java`

Validates inventory interactions:
- Prevents invalid items from entering bundles
- Enforces one-item limit
- Provides feedback to players

#### 6. SeasonCommand
**Location**: `src/main/java/com/gtmc/seasonalbundle/commands/SeasonCommand.java`

Implements admin commands:
- `/seasonend` - End season, save player items
- `/seasonstart` - Start new season
- `/bundlereload` - Reload configuration

## Data Persistence Format

### seasons.yml Structure

```yaml
current-season: 1
seasons:
  1:
    player-items:
      'uuid-here': !!org.bukkit.inventory.ItemStack {...}
  2:
    player-items:
      'uuid-here': !!org.bukkit.inventory.ItemStack {...}
```

## Extending the Plugin

### Adding New Item Restrictions

1. Edit `BundleUtil.isValidBundleItem()`:
```java
public static boolean isValidBundleItem(Material material) {
    // Add new restrictions here
    if (material == Material.YOUR_ITEM) {
        return false;
    }
    return true;
}
```

2. Consider adding to config.yml blacklist for easy admin management

### Adding New Commands

1. Create new command method in `SeasonCommand.java`
2. Add case statement to `onCommand()` method
3. Register command in `plugin.yml`
4. Test with proper permission handling

Example:
```java
private boolean handleMyCommand(CommandSender sender) {
    if (!sender.hasPermission("seasonalbundle.my-command")) {
        sender.sendMessage("No permission!");
        return true;
    }
    // Command logic here
    return true;
}
```

### Adding New Events

1. Create new listener class in `listeners/` package
2. Implement `Listener` interface
3. Add event handler methods with `@EventHandler`
4. Register in `SeasonalBundlePlugin.onEnable()`

Example:
```java
public class MyListener implements Listener {
    @EventHandler
    public void onCustomEvent(CustomEvent event) {
        // Handle event
    }
}
```

## Testing

### Manual Testing Checklist

- [ ] Player joins and receives bundle
- [ ] Player places item in bundle
- [ ] Player tries to place invalid item (rejected)
- [ ] Player tries to place stack (rejected)
- [ ] Admin runs `/seasonend` (items saved)
- [ ] Admin runs `/seasonstart` (new season created)
- [ ] Players rejoin and get relic bundles
- [ ] Relic displays correct season number
- [ ] Bundle shows correct enchantments

### Testing with Paper Server

1. Download Paper JAR: https://papermc.io/
2. Place JAR in server directory
3. Accept EULA
4. Build plugin: `mvnw clean package`
5. Copy JAR to `server/plugins/`
6. Start server and test

## Commit Message Guidelines

Follow conventional commits:

```
feat: add bundle customization options
fix: prevent elytra storage in bundles
docs: update configuration documentation
refactor: simplify item validation logic
test: add bundle creation tests
```

Format: `<type>(<scope>): <description>`

Types:
- `feat` - New feature
- `fix` - Bug fix
- `refactor` - Code restructure without feature change
- `docs` - Documentation only
- `test` - Tests only
- `chore` - Build/dependency updates

## Common Issues

### Bundle Not Appearing

**Cause**: Inventory is full
**Solution**: Players need at least 1 free slot, or bundle will be dropped

### Items Not Saving

**Cause**: `/seasonend` not called before season change
**Solution**: Always call `/seasonend` before `/seasonstart`

### Plugin Won't Load

**Cause**: Compilation errors or missing dependencies
**Solution**: Check server logs, run `mvnw clean compile`

## Code Style

- Use clear, descriptive variable names
- Add comments for complex logic
- Keep methods focused on single responsibility
- Use appropriate access modifiers (private/public)
- Follow Java naming conventions (camelCase)

## Dependencies

- Paper API 1.21.1 - Provided by server
- No external dependencies required

## Performance Considerations

- Data is only saved/loaded on demand
- Listeners are event-driven (minimal overhead)
- Configuration is cached in memory
- Batch operations on player join/leave

## Future Enhancements

Possible improvements:
- [ ] Per-player bundle customization
- [ ] Multi-item bundle (with capacity system)
- [ ] Bundle upgrades (enchantment levels)
- [ ] Item preview/viewing
- [ ] Database support for larger servers
- [ ] Web UI for item management
- [ ] Seasonal rewards system
