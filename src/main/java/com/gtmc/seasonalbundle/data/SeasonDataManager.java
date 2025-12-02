package com.gtmc.seasonalbundle.data;

import com.gtmc.seasonalbundle.SeasonalBundlePlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SeasonDataManager {

    private final SeasonalBundlePlugin plugin;
    private final File dataFolder;
    private final File seasonFile;
    private FileConfiguration seasonConfig;
    private int currentSeason = 1;
    private final Map<UUID, ItemStack> playerSeasonItems = new HashMap<>();

    public SeasonDataManager(SeasonalBundlePlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        this.seasonFile = new File(dataFolder, "seasons.yml");
    }

    public void load() {
        if (!seasonFile.exists()) {
            createDefaultConfig();
        }

        seasonConfig = YamlConfiguration.loadConfiguration(seasonFile);
        currentSeason = seasonConfig.getInt("current-season", 1);

        // Load player items from current season
        if (seasonConfig.contains("seasons." + currentSeason + ".player-items")) {
            for (String uuidStr : seasonConfig.getConfigurationSection("seasons." + currentSeason + ".player-items").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    ItemStack item = seasonConfig.getItemStack("seasons." + currentSeason + ".player-items." + uuidStr);
                    if (item != null) {
                        playerSeasonItems.put(uuid, item);
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in seasons.yml: " + uuidStr);
                }
            }
        }

        plugin.getLogger().info("Loaded season data - Current Season: " + currentSeason);
    }

    public void save() {
        try {
            seasonConfig.set("current-season", currentSeason);
            seasonConfig.set("seasons." + currentSeason + ".player-items", null);

            for (Map.Entry<UUID, ItemStack> entry : playerSeasonItems.entrySet()) {
                seasonConfig.set("seasons." + currentSeason + ".player-items." + entry.getKey(), entry.getValue());
            }

            seasonConfig.save(seasonFile);
            plugin.getLogger().info("Season data saved successfully!");
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save season data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void endSeason() {
        save();
        plugin.getLogger().info("Season " + currentSeason + " ended. Items saved for " + playerSeasonItems.size() + " players.");
    }

    public void startNewSeason() {
        currentSeason++;
        playerSeasonItems.clear();
        save();
        plugin.getLogger().info("Season " + currentSeason + " started!");

        // Notify all online players
        Bukkit.broadcastMessage("§6§lSeason " + (currentSeason - 1) + " has ended! Season " + currentSeason + " begins!");
    }

    public void savePlayerItem(UUID playerUUID, ItemStack item) {
        playerSeasonItems.put(playerUUID, item);
    }

    public ItemStack getPlayerItem(UUID playerUUID) {
        return playerSeasonItems.get(playerUUID);
    }

    public int getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(int season) {
        this.currentSeason = season;
    }

    private void createDefaultConfig() {
        try {
            seasonFile.createNewFile();
            seasonConfig = YamlConfiguration.loadConfiguration(seasonFile);
            seasonConfig.set("current-season", 1);
            seasonConfig.save(seasonFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to create seasons.yml: " + e.getMessage());
        }
    }
}
