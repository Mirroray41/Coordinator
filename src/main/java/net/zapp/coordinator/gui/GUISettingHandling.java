package net.zapp.coordinator.gui;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static net.zapp.coordinator.Coordinator.*;

public class GUISettingHandling {
    public static void syncGUI(Player sender, Inventory chestGUI) {
        String struct = structureManager.getString("structure").replaceFirst(" ","");
        String[] keys = struct.replace("{", "").replace("}", "").split(" ");

        int state = 0;

        for (int i = 0; i < 27; i++) {
            String key = keys[i];
            String parentKey = key;
            String type = structureManager.getString(key + ".type");
            if (type.equals("two_state_switch") || type.equals("two_state_roll")) {
                state = playerSettingDatabase.getIntStatement(sender, structureManager.getString(key + ".change"));
                if (state == 0) {
                    key = formatKey(structureManager.getString(key + ".sub_1"));
                } else {
                    key = formatKey(structureManager.getString(key + ".sub_2"));
                }
            }
            if (type.equals("three_state_roll")) {
                state = playerSettingDatabase.getIntStatement(sender, structureManager.getString(key + ".change"));
                if (state == 0) {
                    key = formatKey(structureManager.getString(key + ".sub_1"));
                } else if (state == 1) {
                    key = formatKey(structureManager.getString(key + ".sub_2"));
                } else {
                    key = formatKey(structureManager.getString(key + ".sub_3"));
                }
            }
            typeToGUIItem(chestGUI, key, parentKey, i, state);
        }
    }

    protected static void typeToGUIItem(Inventory chestGUI, String childKey, String parentKey, int slot, int state) {
        String type = structureManager.getString(childKey + ".type");
        Material material = Material.valueOf(structureManager.getString(childKey + ".material"));

        String name;
        String[] loreKeys = structureManager.getString(childKey + ".lore").split("\\|");
        List<String> lore = new java.util.ArrayList<>();
        if (!structureManager.getString(childKey + ".lore").isEmpty()) {
            for (String loreKey: loreKeys) {
                lore.add(translationManager.getStringWithReplace(loreKey));
            }
        }

        switch (type) {
            case "static":
                name = structureManager.getString(childKey + ".name");
                break;
            case "static_translatable":
                name = translationManager.getStringWithReplace(structureManager.getString(childKey + ".name"));
                break;
            default:
                name = " ";
                break;
        }
        if (name.contains("|")) {
            String[] parts = name.split("\\|");
            if (state == 0) {
                name = parts[0] + parts[1];
            } else {
                name = parts[0] + parts[2];
            }
        }
        if (name.contains("/")) {
            String[] parts = name.split("/");
            if (state == 0) {
                name = parts[2] + parts[3];
            } else {
                name = parts[1] + parts[3];
            }
        }

        for (int i = 0; i < lore.size(); i++) {
            if (lore.get(i).contains("|")) {
                String[] parts = lore.get(i).split("\\|");
                if (state == i) {
                    lore.set(i, parts[0] + parts[1]);
                } else {
                    lore.set(i, parts[0] + parts[2]);
                }
            }
            if (lore.get(i).contains("/")) {
                String[] parts = lore.get(i).split("/");

                if (state == i) {
                    lore.set(i, parts[2] + parts[3]);
                } else {
                    lore.set(i, parts[1] + parts[3]);
                }
            }
            lore.set(i, colorize(lore.get(i)));
        }

        if (structureManager.has(parentKey + ".change")) {
            if (structureManager.getString(parentKey + ".change").equals("visibility")) {
                name = (config.getBoolean("globals.visibility.is_enabled") ? "" : translationManager.getStringWithReplace("translations.gui.disabled")) + name;
            } else if (structureManager.getString(parentKey + ".change").equals("direction")) {
                name = (config.getBoolean("globals.direction.is_enabled") ? "" : translationManager.getStringWithReplace("translations.gui.disabled")) + name;
            } else if (structureManager.getString(parentKey + ".change").equals("location")) {
                name = (config.getBoolean("globals.location.is_enabled") ? "" : translationManager.getStringWithReplace("translations.gui.disabled")) + name;
            } else if (structureManager.getString(parentKey + ".change").equals("time")) {
                name = (config.getBoolean("globals.time.is_enabled") ? "" : translationManager.getStringWithReplace("translations.gui.disabled")) + name;
            }
        }

        name = colorize(name);
        if (isLegacy) {
            chestGUI.setItem(slot, itemWithData(new ItemStack(material, 1, Short.parseShort(structureManager.getString(childKey + ".legacy_state"))), name, lore, structureManager.getString(childKey+".enchanted").equals("true")));
        } else {
            chestGUI.setItem(slot, itemWithData(new ItemStack(material, 1), name, lore, structureManager.getString(childKey+".enchanted").equals("true")));
        }

    }

    protected static String formatKey(String key) {
        return key.replace("{", "").replace("}", "");
    }

    public static ItemStack itemWithData(ItemStack stack, String name, List<String> lore, boolean isEnchanted) {
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        stack.setItemMeta(meta);
        if (isEnchanted) {
            stack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }
        return stack;
    }
    
}