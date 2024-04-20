package net.zapp.coordinator.helper;

import net.zapp.coordinator.Coordinator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.zapp.coordinator.Coordinator.*;
import static net.zapp.coordinator.helper.GUIHelper.itemWithData;

public class GUISettingHandling {
    public static void syncGui(Player player, Inventory chestGui) {
        Map<String, Integer> config = Coordinator.playerConfig.get(player.getUniqueId());

        int visibility = config.get("visibility");
        int location_type = config.get("location_type");
        int location = config.get("location");
        int direction_type = config.get("direction_type");
        int direction = config.get("direction");
        int time_type = config.get("time_type");
        int time = config.get("time");

        ItemStack visibilityItem = itemWithData(OnOffItem(visibility),
                colorize((plugin.getConfig().getBoolean("globals.visibility.is_enabled") ? "" : translationManager.get("translations.gui.disabled")) +
                        translationManager.get("translations.gui.visibility.switch_label") + OnOff(visibility)),
                Arrays.asList(colorize(translationManager.get("translations.gui.visibility.switch_tooltip"))), true);

        ItemStack locationItem = itemWithData(LegacyItem(isLegacy, new ItemStack(Material.COMPASS, 1), new ItemStack(Material.LEGACY_COMPASS, 1)),
                colorize((plugin.getConfig().getBoolean("globals.location.is_enabled") ? "" : translationManager.get("translations.gui.disabled")) +
                        translationManager.get("translations.gui.location.switch_label") + OnOff(location)),
                Arrays.asList(colorize(translationManager.get("translations.gui.location.switch_tooltip"))), location == 1);

        ItemStack directionItem = itemWithData(LegacyItem(isLegacy, new ItemStack(Material.MAP, 1), new ItemStack(Material.LEGACY_EMPTY_MAP, 1)),
                colorize((plugin.getConfig().getBoolean("globals.direction.is_enabled") ? "" : translationManager.get("translations.gui.disabled")) +
                        translationManager.get("translations.gui.direction.switch_label") + OnOff(direction)),
                Arrays.asList(colorize(translationManager.get("translations.gui.direction.switch_tooltip"))), direction == 1);

        ItemStack timeItem = itemWithData(LegacyItem(isLegacy, new ItemStack(Material.CLOCK, 1), new ItemStack(Material.LEGACY_WATCH, 1)),
                colorize((plugin.getConfig().getBoolean("globals.time.is_enabled") ? "" : translationManager.get("translations.gui.disabled")) +
                        translationManager.get("translations.gui.time.switch_label") + OnOff(time)),
                Arrays.asList(colorize(translationManager.get("translations.gui.time.switch_tooltip"))), time == 1);

        ItemStack locationTypeItem = itemWithData(ThreeStateItem(location_type),
                colorize(translationManager.get("translations.gui.location.type_selector_label")),
                TwoStateLoreSelection(
                        translationManager.get("translations.gui.location.type_selection_1"),
                        translationManager.get("translations.gui.location.type_selection_2"),
                        translationManager.get("translations.gui.selected"),
                        translationManager.get("translations.gui.unselected"),
                        location_type), false);

        ItemStack directionTypeItem = itemWithData(ThreeStateItem(direction_type),
                colorize(translationManager.get("translations.gui.direction.type_selector_label")),
                ThreeStateLoreSelection(
                        translationManager.get("translations.gui.direction.type_selection_1"),
                        translationManager.get("translations.gui.direction.type_selection_2"),
                        translationManager.get("translations.gui.direction.type_selection_3"),
                        translationManager.get("translations.gui.selected"),
                        translationManager.get("translations.gui.unselected"),
                        direction_type), false);

        ItemStack timeTypeItem = itemWithData(ThreeStateItem(time_type),
                colorize(translationManager.get("translations.gui.time.type_selector_label")),
                TwoStateLoreSelection(
                        translationManager.get("translations.gui.time.type_selection_1"),
                        translationManager.get("translations.gui.time.type_selection_2"),
                        translationManager.get("translations.gui.selected"),
                        translationManager.get("translations.gui.unselected"),
                        time_type), false);

        chestGui.setItem(10, visibilityItem);
        chestGui.setItem(4, locationItem);
        chestGui.setItem(6, directionItem);
        chestGui.setItem(8, timeItem);

        chestGui.setItem(13, locationTypeItem);
        chestGui.setItem(15, directionTypeItem);
        chestGui.setItem(17, timeTypeItem);
    }

    public static String OnOff(int i) {
        if (i == 0) {
            return translationManager.get("translations.gui.off_val");
        } else {
            return translationManager.get("translations.gui.on_val");
        }
    }

    public static List<String> ThreeStateLoreSelection(String s1, String s2, String s3, String sel, String unsel, int i) {
        if (i == 0) {
            return Arrays.asList(colorize(sel + s1), colorize(unsel + s2), colorize(unsel + s3));
        } else if (i == 1) {
            return Arrays.asList(colorize(unsel + s1), colorize(sel + s2), colorize(unsel + s3));
        } else {
            return Arrays.asList(colorize(unsel + s1), colorize(unsel + s2), colorize(sel + s3));
        }
    }

    public static List<String> TwoStateLoreSelection(String s1, String s2, String sel, String unsel, int i) {
        if (i == 0) {
            return Arrays.asList(colorize(sel + s1), colorize(unsel + s2));
        }  else {
            return Arrays.asList(colorize(unsel + s1), colorize(sel + s2));
        }
    }

    public static ItemStack OnOffItem(int i) {
        ItemStack legacy;
        ItemStack current;
        if (i == 0) {
            legacy = new ItemStack(Material.LEGACY_LEVER, 1);
            current = new ItemStack(Material.LEVER, 1);
        } else {
            legacy = new ItemStack(Material.LEGACY_REDSTONE_TORCH_ON, 1);
            current = new ItemStack(Material.REDSTONE_TORCH, 1);
        }
        return LegacyItem(isLegacy, current, legacy);
    }


    public static ItemStack ThreeStateItem(int i) {
        ItemStack legacy;
        ItemStack current;
        if (i == 0) {
            legacy = new ItemStack(Material.LEGACY_INK_SACK, 1, (short) 8);
            current = new ItemStack(Material.PINK_DYE, 1);
        } else if (i == 1) {
            legacy = new ItemStack(Material.LEGACY_INK_SACK, 1, (short) 13);
            current = new ItemStack(Material.MAGENTA_DYE, 1);
        } else {
            legacy = new ItemStack(Material.LEGACY_INK_SACK, 1, (short) 5);
            current = new ItemStack(Material.PURPLE_DYE, 1);
        }
        return LegacyItem(isLegacy, current, legacy);
    }

    public static ItemStack LegacyItem(boolean isLegacy, ItemStack current, ItemStack legacy) {
        if (isLegacy) return legacy;
        return current;
    }
}