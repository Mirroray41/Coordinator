package net.zapp.coordinator.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static net.zapp.coordinator.Coordinator.*;
import static net.zapp.coordinator.helper.GUIHelper.itemWithData;
import static net.zapp.coordinator.helper.GUISettingHandling.syncGui;

public class CrCommand implements CommandExecutor, TabCompleter {
    private static final String[] FIRST = {"menu", "set", "reload"};
    private static final String[] SET_1 = {"defaults"};
    private static final String[] SET_2 = {"default"};

    @Override
    public boolean onCommand( CommandSender sender,  Command command,  String s,  String[] strings ) {
        if (strings.length == 0 && sender.hasPermission("cr.use")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(colorize(translationManager.get("error.cr_command_run_by_non_player")));
                return false;
            }

            prepMenu((Player)sender);
            return true;
        }
        if (strings.length != 0) {
            if (strings[0].equals("reload") && sender.hasPermission("cr.reload")) {
                sender.sendMessage("reloading!");
                reload();
                translationManager.reloadLang();
                return true;
            }
        }
        sender.sendMessage(colorize(translationManager.get("translations.errors.cr_command_wrong_arguments")));
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> completions = new ArrayList<>();

        List<String> playerNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerNames.add(player.getName());
        }

        if (strings.length == 1) {
            completions = Arrays.asList(FIRST);
        } else if (strings.length == 2 && strings[0].equals("set")) {
            completions = Arrays.asList(SET_1);
            completions = concat(completions, playerNames);
        } else if (strings.length == 2 && strings[0].equals("menu")) {
            completions = playerNames;
        } else if (strings.length == 3 && strings[0].equals("set") && !strings[1].equals("defaults")) {
            completions = Arrays.asList(SET_2);
        } else if (strings.length == 4 && strings[0].equals("set") && !strings[1].equals("defaults")) {
            completions = playerNames;
        }


        return completions;
    }

    private static List<String> concat(List<String> list1, List<String> list2) {
        return Arrays.asList(Stream.concat(list1.stream(), list2.stream()).toArray(String[]::new));
    }

    private static void prepMenu(Player sender) {
        Inventory chestGui = Bukkit.createInventory(null, 27, colorize(translationManager.get("translations.gui.title")));

        ItemStack background;
        ItemStack separator;
        ItemStack loading;

        if (isLegacy) {
            background = itemWithData(new ItemStack(Material.LEGACY_STAINED_GLASS_PANE, 1, (short) 8), " ");

            separator = itemWithData(new ItemStack(Material.LEGACY_STAINED_GLASS_PANE, 1, (short) 7), " ");

            loading = itemWithData(new ItemStack(Material.LEGACY_STAINED_GLASS_PANE, 1, (short) 14), colorize(translationManager.get("translations.gui.loading")));
        } else {
            background = itemWithData(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1), " ");

            separator = itemWithData(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), " ");

            loading = itemWithData(new ItemStack(Material.RED_STAINED_GLASS_PANE, 1), colorize(translationManager.get("translations.gui.loading")));
        }



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
    }
}