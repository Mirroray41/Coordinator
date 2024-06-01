package net.zapp.coordinator.helper;

import net.zapp.coordinator.Coordinator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

import static net.zapp.coordinator.Coordinator.*;
import static net.zapp.coordinator.helper.GUIHelper.itemWithData;

public class GUISettingHandling {
    public static void syncGUI(Player sender, Inventory chestGUI) {
        String struct = structureManager.get("structure").replaceFirst(" ","");
        String[] keys = struct.replace("{", "").replace("}", "").split(" ");
        int state = 0;

        Map<String, Integer> config = Coordinator.playerConfig.get(sender.getUniqueId());

        for (int i = 0; i < 27; i++) {
            String key = keys[i];

            String type = structureManager.get(key + ".type");
            if (type.equals("two_state_switch") || type.equals("two_state_roll")) {
                state = config.get(structureManager.get(key + ".change"));
                if (state == 0) {
                    key = formatKey(structureManager.get(key + ".sub_1"));
                } else {
                    key = formatKey(structureManager.get(key + ".sub_2"));
                }
            }
            if (type.equals("three_state_roll")) {
                state = config.get(structureManager.get(key + ".change"));
                if (state == 0) {
                    key = formatKey(structureManager.get(key + ".sub_1"));
                } else if (state == 1) {
                    key = formatKey(structureManager.get(key + ".sub_2"));
                } else {
                    key = formatKey(structureManager.get(key + ".sub_3"));
                }
            }
            typeToGUIItem(chestGUI, key, i, state);
        }
    }

    protected static void typeToGUIItem(Inventory chestGUI, String key, int slot, int state) {
        String type = structureManager.get(key + ".type");
        Material material = Material.valueOf(structureManager.get(key + ".material"));

        String name;
        String[] loreKeys = structureManager.get(key + ".lore").split("\\|");
        List<String> lore = new java.util.ArrayList<>();
        if (!structureManager.get(key + ".lore").isEmpty()) {
            for (String loreKey: loreKeys) {
                lore.add(translationManager.get(loreKey));
            }
        }

        switch (type) {
            case "static":
                name = structureManager.get(key + ".name");
                break;
            case "static_translatable":
                name = translationManager.get(structureManager.get(key + ".name"));
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
        name = colorize(name);
        if (isLegacy) {
            chestGUI.setItem(slot, itemWithData(new ItemStack(material, 1, Short.parseShort(structureManager.get(key + ".legacy_state"))), name, lore, structureManager.get(key+".enchanted").equals("true")));
        } else {
            chestGUI.setItem(slot, itemWithData(new ItemStack(material, 1), name, lore, structureManager.get(key+".enchanted").equals("true")));
        }

    }

    protected static String formatKey(String key) {
        return key.replace("{", "").replace("}", "");
    }
}