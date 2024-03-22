package net.zapp.coordinator.helper;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GUIHelper {
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

    public static ItemStack itemWithData(ItemStack stack, String name) {
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
        }
        stack.setItemMeta(meta);
        return stack;
    }

    public static int rollChoices(int previous, int rollLimit) {
        if (previous >= rollLimit) {
            return 0;
        }
        return previous+1;

    }
}
