package net.zapp.coordinator.commands;

import net.zapp.coordinator.Coordinator;
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


import java.util.*;
import java.util.stream.Stream;

import static net.zapp.coordinator.Coordinator.*;
import static net.zapp.coordinator.helper.GUIHelper.itemWithData;
import static net.zapp.coordinator.helper.GUISettingHandling.syncGUI;

public class CrCommand implements CommandExecutor, TabCompleter {
    private static final String[] FIRST = {"menu", "set", "get", "reload"};
    private static final String[] SET_1 = {"defaults"};
    private static final String[] SET_2 = {"default"};
    private static final String[] INT_OPTIONS = {"0", "1", "2"};

    private static final String[] OPTIONS = {"visibility", "location", "direction", "time", "location_type", "direction_type", "time_type"};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings ) {
        if ((strings.length == 0 || strings[0].equals("menu"))&& sender.hasPermission("cr.use")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(colorize(translationManager.get("errors.cr_command_run_by_non_player")));
                return false;
            }

            prepMenu((Player)sender);
            return true;
        }
        if (strings.length != 0) {
            if (strings[0].equals("reload") && sender.hasPermission("cr.reload")) {
                sender.sendMessage(colorize(translationManager.get("translations.info.cr_command_reload")));
                reload();
                translationManager.reloadLang();
                structureManager.reloadStructure();
                return true;
            } else if (strings[0].equals("set") && sender.hasPermission("cr.set")) {
                Map<String, Integer> config;
                Player player = getPlayerFromCommand(strings, sender);
                if (player == null) {
                    sender.sendMessage(colorize(translationManager.get("translations.errors.cr_command_missing_player")));
                    return false;
                }
                config = Coordinator.playerConfig.get((player.getUniqueId()));
                if(strings[1].equals("defaults") || strings[2].equals("defaults")) {
                    config = defaultConfig;
                } else {
                    if (Arrays.asList(OPTIONS).contains(strings[2])) {
                        if (strings[3].equals("default")){
                            config.replace(strings[2], defaultConfig.get(strings[2]));
                        } else {
                            config.replace(strings[2], Integer.valueOf(strings[3]));
                        }
                    } else {
                        sender.sendMessage(colorize(translationManager.get("translations.errors.cr_command_incorrect_parameter")));
                    }
                }
                Coordinator.playerConfig.replace(player.getUniqueId(), config);
                return true;
            } else if (strings[0].equals("get") && sender.hasPermission("cr.get")) {
                if (strings.length == 1) {
                    sender.sendMessage(colorize(translationManager.get("translations.errors.cr_command_wrong_arguments")));
                    return false;
                }
                Map<String, Integer> config;
                Player player = getPlayerFromCommand(strings, sender);
                if (player == null) {
                    sender.sendMessage(colorize(translationManager.get("translations.errors.cr_command_missing_player")));
                    return false;
                }
                config = Coordinator.playerConfig.get((player.getUniqueId()));
                if (strings.length == 2) {
                    sender.sendMessage(config.toString()
                            .replace("{", "")
                            .replace("}", "")
                            .replace("=", " = ")
                            .replace(", ", "\n"));
                    return true;
                } else if (Arrays.asList(OPTIONS).contains(strings[2])) {
                    sender.sendMessage(strings[2] + " = " + config.get(strings[2]));
                    return true;
                }
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
        } else if (strings.length == 2 && strings[0].equals("get")) {
            completions = playerNames;
        } else if (strings.length == 3 && (strings[0].equals("set") && !strings[1].equals("defaults"))) {
            completions = Arrays.asList(OPTIONS);
            completions = concat(completions, Arrays.asList(SET_1));
        } else if (strings.length == 3 && strings[0].equals("get")) {
            completions = Arrays.asList(OPTIONS);
        } else if (strings.length == 4 && strings[0].equals("set") && !strings[1].equals("defaults") && !strings[2].equals("defaults")) {
            completions = Arrays.asList(SET_2);
            completions = concat(completions, Arrays.asList(INT_OPTIONS));
        }


        return completions;
    }

    private static List<String> concat(List<String> list1, List<String> list2) {
        return Arrays.asList(Stream.concat(list1.stream(), list2.stream()).toArray(String[]::new));
    }

    private static void prepMenu(Player sender) {
        Inventory chestGui = Bukkit.createInventory(null, 27, colorize(translationManager.get("translations.gui.title")));



        for (int i = 0; i < 27; i++) {
            chestGui.setItem(i, itemWithData(new ItemStack(Material.valueOf(structureManager.get("l.material")), 1), colorize(translationManager.get(structureManager.get("l.name")))));
        }

        syncGUI(sender, chestGui);

        sender.openInventory(chestGui);
    }

    private Player getPlayerFromCommand(String[] strings, CommandSender sender) {
        Player player;
        if (!strings[1].equals("defaults")) {
            player = Bukkit.getPlayer(strings[1]);
            if (player != null) {
                if (!Coordinator.playerConfig.containsKey((player.getUniqueId()))) {
                    return null;
                }
            }
        } else {
            if (!(sender instanceof Player)) return null;
            player = (Player) sender;
        }
        return player;
    }
}