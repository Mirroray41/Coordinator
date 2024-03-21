package net.zapp.coordinator.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

import static net.zapp.coordinator.Coordinator.*;
import static net.zapp.coordinator.helper.GUIHelper.itemWithData;
import static net.zapp.coordinator.helper.GUISettingHandling.syncGui;

public class CrCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings ) {
        if (strings.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(colorize(translationManager.get("error.cr_command_run_by_non_player")));
                return false;
            }

            Inventory chestGui = Bukkit.createInventory(null, 27, colorize(translationManager.get("translations.gui.title")));

            ItemStack background = itemWithData(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");

            ItemStack separator = itemWithData(new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1), " ");

            ItemStack loading = itemWithData(new ItemStack(Material.RED_STAINED_GLASS_PANE, 1), colorize(translationManager.get("translations.gui.loading")));

            for (int i = 0; i < 27; i++) {
                chestGui.setItem(i, background);
            }

            chestGui.setItem(10, loading);

            chestGui.setItem(3, separator);
            chestGui.setItem(12, separator);
            chestGui.setItem(21, separator);

            chestGui.setItem(4, loading);
            chestGui.setItem(6, loading);
            chestGui.setItem(8, loading);
            chestGui.setItem(13, loading);
            chestGui.setItem(15, loading);
            chestGui.setItem(17, loading);

            syncGui((Player) sender, chestGui);

            ((Player) sender).openInventory(chestGui);
            return true;
        }
        if (strings[0].equals("reload") && sender.hasPermission("cr.reload")) {
            sender.sendMessage("reloading!");
            reload();
            translationManager.reloadLang();
            return true;
        }
        sender.sendMessage(colorize(translationManager.get("translations.errors.cr_command_wrong_arguments")));
        return false;
    }
}
