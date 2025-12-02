package com.gtmc.seasonalbundle.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;
import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;

public class BundleUtil {

    public static final int SEASON_1_SLOTS = 1;

    /**
     * Creates an enchanted bundle for a player at the start of a season
     */
    public static ItemStack createBundle(int season) {
        ItemStack bundle = new ItemStack(Material.BUNDLE);
        ItemMeta meta = bundle.getItemMeta();

        if (meta instanceof BundleMeta) {
            BundleMeta bundleMeta = (BundleMeta) meta;

            // Set display name
            bundleMeta.setDisplayName("§6§lSeasonal Bundle");

            // Set lore with season info
            bundleMeta.setLore(Arrays.asList(
                    "§7Season: §c" + season,
                    "§7Capacity: §b1 item",
                    "§8Store one item here"
            ));

            // Add enchantment for visual effect
            bundleMeta.addEnchant(Enchantment.UNBREAKING, 1, true);

            bundle.setItemMeta(bundleMeta);
        }

        return bundle;
    }

    /**
     * Creates a rare bundle containing the season item for next season
     */
    public static ItemStack createRareBundle(ItemStack seasonItem, int seasonNumber) {
        ItemStack bundle = new ItemStack(Material.BUNDLE);
        ItemMeta meta = bundle.getItemMeta();

        if (meta instanceof BundleMeta) {
            BundleMeta bundleMeta = (BundleMeta) meta;

            // Set display name
            bundleMeta.setDisplayName("§c§lSEASON " + seasonNumber + " RELIC");

            // Set lore
            bundleMeta.setLore(Arrays.asList(
                    "§c§lFrom Season " + seasonNumber,
                    "§7A rare item from",
                    "§7the previous season",
                    "§8Carry with pride"
            ));

            // Add enchantments for rarity
            bundleMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
            bundleMeta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);

            // Add the item to the bundle
            bundleMeta.addItem(seasonItem);

            bundle.setItemMeta(bundleMeta);
        }

        return bundle;
    }

    /**
     * Validates if an item can be placed in a bundle
     * Only allows 1 item, rejects: elytras, shulker boxes, bundles
     */
    public static boolean isValidBundleItem(Material material) {
        if (material == null) {
            return false;
        }

        String materialName = material.name();

        // Reject elytras
        if (material == Material.ELYTRA) {
            return false;
        }

        // Reject bundles (prevent nesting)
        if (material == Material.BUNDLE) {
            return false;
        }

        // Reject ALL shulker boxes (including all color variants)
        if (materialName.contains("SHULKER_BOX")) {
            return false;
        }

        return true;
    }

    /**
     * Checks if an ItemStack is a valid bundle item
     */
    public static boolean isValidBundleItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        return isValidBundleItem(item.getType());
    }

    /**
     * Gets the capacity in percentage (bundle can hold 64 items worth of space)
     */
    public static int getBundleCapacity(ItemStack bundle) {
        if (bundle == null || bundle.getType() != Material.BUNDLE) {
            return 0;
        }

        ItemMeta meta = bundle.getItemMeta();
        if (meta instanceof BundleMeta) {
            BundleMeta bundleMeta = (BundleMeta) meta;
            return bundleMeta.getItems().size();
        }

        return 0;
    }

    /**
     * Checks if bundle is at capacity (1 item max)
     */
    public static boolean isBundleAtCapacity(ItemStack bundle) {
        return getBundleCapacity(bundle) >= 1;
    }
}
