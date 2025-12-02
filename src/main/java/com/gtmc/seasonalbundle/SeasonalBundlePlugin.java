package com.gtmc.seasonalbundle;

import org.bukkit.plugin.java.JavaPlugin;
import com.gtmc.seasonalbundle.listeners.PlayerJoinListener;
import com.gtmc.seasonalbundle.listeners.ItemValidationListener;
import com.gtmc.seasonalbundle.commands.SeasonCommand;
import com.gtmc.seasonalbundle.data.SeasonDataManager;

public class SeasonalBundlePlugin extends JavaPlugin {

    private SeasonDataManager seasonDataManager;

    @Override
    public void onEnable() {
        getLogger().info("SeasonalBundle plugin enabling...");

        // Save default config if it doesn't exist
        saveDefaultConfig();

        // Initialize data manager
        seasonDataManager = new SeasonDataManager(this);
        seasonDataManager.load();

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemValidationListener(), this);

        // Register commands
        new SeasonCommand(this);

        getLogger().info("SeasonalBundle plugin enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (seasonDataManager != null) {
            seasonDataManager.save();
        }
        getLogger().info("SeasonalBundle plugin disabled.");
    }

    public SeasonDataManager getSeasonDataManager() {
        return seasonDataManager;
    }
}
