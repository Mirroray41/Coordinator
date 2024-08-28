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
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.*;
import java.util.stream.Stream;

import static net.zapp.coordinator.Coordinator.*;
import static net.zapp.coordinator.gui.GUISettingHandling.syncGUI;

public class CrCommand implements CommandExecutor, TabCompleter {
    private static final String[] FIRST = {"menu", "set", "get", "reload"};
    private static final String[] SET_1 = {"defaults"};
    private static final String[] SET_2 = {"default"};
    private static final String[] INT_SETTINGS = {"0", "1", "2"};
    private static final String[] BOOL_SETTINGS = {"true", "false"};

    private static final String[] OPTIONS = {"visibility", "location", "direction", "time", "location_type", "direction_type", "time_type"};
    private static final String[] BOOL_OPTIONS = {"visibility", "location", "direction", "time"};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings ) {
        if ((strings.length == 0 || strings[0].equals("menu"))&& sender.hasPermission("cr.use")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(colorize(translationManager.getString("errors.cr_command_run_by_non_player")));
                return false;
            }

            prepMenu((Player)sender);
            return true;
        }
        if (strings.length != 0) {
            if (strings[0].equals("reload") && sender.hasPermission("cr.reload")) {
                sender.sendMessage(colorize(translationManager.getString("translations.info.cr_command_reload")));
                reload();
                return true;
            } else if (strings[0].equals("set") && sender.hasPermission("cr.set")) {
                Player player = getPlayerFromCommand(strings, sender);
                if (player == null) {
                    sender.sendMessage(colorize(translationManager.getString("translations.errors.cr_command_missing_player")));
                    return false;
                }
                if(strings[1].equals("defaults") || strings[2].equals("defaults")) {
                    playerSettingDatabase.setDefault(player, "visibility");
                    playerSettingDatabase.setDefault(player, "location");
                    playerSettingDatabase.setDefault(player, "location_type");
                    playerSettingDatabase.setDefault(player, "direction");
                    playerSettingDatabase.setDefault(player, "direction_type");
                    playerSettingDatabase.setDefault(player, "time");
                    playerSettingDatabase.setDefault(player, "time_type");
                } else {
                    if (Arrays.asList(OPTIONS).contains(strings[2])) {
                        if (strings[3].equals("default")){
                            playerSettingDatabase.setDefault(player, strings[2]);
                        } else {
                            if (Arrays.asList(BOOL_OPTIONS).contains((strings[2]))){
                                playerSettingDatabase.setBoolStatement(player, strings[3].equals("true"), strings[2]);
                            } else {
                                playerSettingDatabase.setIntStatement(player, Integer.parseInt(strings[3]), strings[2]);
                            }
                        }
                    } else {
                        sender.sendMessage(colorize(translationManager.getString("translations.errors.cr_command_incorrect_parameter")));
                    }
                }
                return true;
            } else if (strings[0].equals("get") && sender.hasPermission("cr.get")) {
                if (strings.length == 1) {
                    sender.sendMessage(colorize(translationManager.getString("translations.errors.cr_command_wrong_arguments")));
                    return false;
                }
                Player player = getPlayerFromCommand(strings, sender);
                if (player == null) {
                    sender.sendMessage(colorize(translationManager.getString("translations.errors.cr_command_missing_player")));
                    return false;
                }
                if (strings.length == 2) {
                    sender.sendMessage("Visibility: " + playerSettingDatabase.getVisibility(player) +
                            "\nLocation: " + playerSettingDatabase.getLocation(player) +
                            "\nLocation Type: " + playerSettingDatabase.getLocationType(player) +
                            "\nDirection: " + playerSettingDatabase.getDirection(player) +
                            "\nDirection Type: " + playerSettingDatabase.getDirectionType(player) +
                            "\nTime: " + playerSettingDatabase.getTime(player) +
                            "\nTime Type: " + playerSettingDatabase.getTimeType(player));
                    return true;
                } else if (Arrays.asList(OPTIONS).contains(strings[2])) {
                    if (Arrays.asList(BOOL_OPTIONS).contains((strings[2]))) {
                        sender.sendMessage(strings[2] + ": " + playerSettingDatabase.getBoolStatement(player, strings[2]));
                    } else {
                        sender.sendMessage(strings[2] + ": " + playerSettingDatabase.getIntStatement(player, strings[2]));
                    }
                    return true;
                }
            }
        }
        sender.sendMessage(colorize(translationManager.getString("translations.errors.cr_command_wrong_arguments")));
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
        } else if (strings.length == 4 && strings[0].equals("set") && !strings[1].equals("defaults") && !strings[2].equals("defaults") && !Arrays.asList(BOOL_OPTIONS).contains((strings[2]))) {
            completions = Arrays.asList(SET_2);
            completions = concat(completions, Arrays.asList(INT_SETTINGS));
        }else if (strings.length == 4 && strings[0].equals("set") && !strings[1].equals("defaults") && !strings[2].equals("defaults") && Arrays.asList(BOOL_OPTIONS).contains((strings[2]))) {
            completions = Arrays.asList(SET_2);
            completions = concat(completions, Arrays.asList(BOOL_SETTINGS));
        }


        return completions;
    }

    private static List<String> concat(List<String> list1, List<String> list2) {
        return Arrays.asList(Stream.concat(list1.stream(), list2.stream()).toArray(String[]::new));
    }

    private static void prepMenu(Player sender) {
        Inventory chestGui = Bukkit.createInventory(null, 27, colorize(translationManager.getString("translations.gui.title")));



        for (int i = 0; i < 27; i++) {
            chestGui.setItem(i, itemWithData(new ItemStack(Material.valueOf(structureManager.getString("l.material")), 1), colorize(translationManager.getString(structureManager.getString("l.name")))));
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


    public static ItemStack itemWithData(ItemStack stack, String name) {
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
        }
        stack.setItemMeta(meta);
        return stack;
    }
}