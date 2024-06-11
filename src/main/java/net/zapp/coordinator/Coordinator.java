package net.zapp.coordinator;

import net.zapp.coordinator.commands.CrCommand;
import net.zapp.coordinator.config_managers.StructureManager;
import net.zapp.coordinator.config_managers.TranslationManager;
import net.zapp.coordinator.handlers.ContainerHandler;
import net.zapp.coordinator.handlers.PlayerJoinHandler;
import net.zapp.coordinator.handlers.PlayerLeaveHandler;
import net.zapp.coordinator.config_managers.PlayerSettingManager;
import net.zapp.coordinator.papi.CoordinatorExpansion;
import net.zapp.coordinator.runnable.BossBarRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Coordinator extends JavaPlugin {

    private static final int CONFIG_VERSION = 5;
    private static final int TRANSLATIONS_VERSION = 4;
    private static final int STRUCTURE_VERSION = 1;

    public final Logger logger = getLogger();

    public static final Map<UUID, BossBar> playerBossBars = new HashMap<>();

    public static Map<UUID, Map<String, Integer>> playerConfig = new HashMap<>();

    public static Map<String, Integer> defaultConfig;

    public static JavaPlugin plugin;

    public static boolean isLegacy = true;

    public static int timeOffset = 0;

    public static FileConfiguration config;

    private static PlayerSettingManager playerSettingManager;
    public static TranslationManager translationManager;
    public static StructureManager structureManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logger.info("Coordinator plugin has been enabled!");

        plugin = this;

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CoordinatorExpansion().register();
        }

        config = plugin.getConfig();


        isLegacy = getConfig().getBoolean("legacy");
        if (isLegacy) {
            logger.warning("legacy mode is enabled, if your server version is 1.13 or higher something went wrong, please report it to the author");
        }

        saveDefaultConfig();
        reloadConfig();

        playerSettingManager = new PlayerSettingManager(plugin, "player_settings.yml");
        translationManager = new TranslationManager(plugin, "translations.yml");
        structureManager = new StructureManager(plugin, "gui_structure.yml");


        if (getConfig().getInt("file_format") != CONFIG_VERSION) {
            logger.warning("Incorrect config format, check for changes on the official github page");
            logger.warning("Coordinator requires config file format version " + CONFIG_VERSION + ", your current file format version is " + getConfig().getInt("file_format"));
            Bukkit.getPluginManager().disablePlugin(this);
        }
        if (!Objects.equals(translationManager.get("file_format"), String.valueOf(TRANSLATIONS_VERSION))) {
            logger.warning("Incorrect config format, check for changes on the official github page");
            logger.warning("Coordinator requires translations file format version " + TRANSLATIONS_VERSION + ", your current file format version is " + translationManager.get("file_format"));
            Bukkit.getPluginManager().disablePlugin(this);
        }
        if (!Objects.equals(structureManager.get("file_format"), String.valueOf(STRUCTURE_VERSION))) {
            logger.warning("Incorrect structure format, check for changes on the official github page");
            logger.warning("Coordinator requires gui_structure file format version " + STRUCTURE_VERSION + ", your current file format version is " + structureManager.get("file_format"));
            Bukkit.getPluginManager().disablePlugin(this);
        }

        int interval = getConfig().getInt("bossbar_refresh_interval");
        if (interval < 1) {
            interval = 1;
        }

        timeOffset = getConfig().getInt("time_offset");

        defaultConfig = new HashMap() {{
            put("visibility", getConfig().getBoolean("default_settings.visibility.is_visible") ? 1 : 0);
            put("location_type", getConfig().getInt("default_settings.location.default_type"));
            put("location", getConfig().getBoolean("default_settings.location.is_visible") ? 1 : 0);
            put("direction_type", getConfig().getInt("default_settings.direction.default_type"));
            put("direction", getConfig().getBoolean("default_settings.direction.is_visible") ? 1 : 0);
            put("time_type", getConfig().getInt("default_settings.time.default_type"));
            put("time", getConfig().getBoolean("default_settings.time.is_visible") ? 1 : 0);
        }};


        playerConfig = playerSettingManager.convertYamlToMap(playerSettingManager.customConfigFile);

        Objects.requireNonNull(this.getCommand("cr")).setExecutor(new CrCommand());
        Objects.requireNonNull(this.getCommand("cr")).setTabCompleter(new CrCommand());

        new PlayerJoinHandler(this);
        new PlayerLeaveHandler(this);
        new ContainerHandler(this);
        new BossBarRunnable().runTaskTimer(this, 0, interval);
    }

    @Override
    public void onDisable() {
        flushSettingsToFile();
    }

    public static void flushSettingsToFile() {
        for (Map.Entry<UUID, Map<String, Integer>> entry : playerConfig.entrySet()) {
            playerSettingManager.set(String.valueOf(entry.getKey()), entry.getValue());
        }
    }

    public static String colorize(String msg) {
        return hex(msg.replace("&", "§"));
    }


    // function by zwrumpy, all credit goes to him
    public static String hex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void reload() {
        plugin.reloadConfig();
        timeOffset = plugin.getConfig().getInt("time_offset");
        defaultConfig = new HashMap() {{
            put("visibility", plugin.getConfig().getBoolean("default_settings.visibility.is_visible") ? 1 : 0);
            put("location_type", plugin.getConfig().getInt("default_settings.location.default_type"));
            put("location", plugin.getConfig().getBoolean("default_settings.location.is_visible") ? 1 : 0);
            put("direction_type", plugin.getConfig().getInt("default_settings.direction.default_type"));
            put("direction", plugin.getConfig().getBoolean("default_settings.direction.is_visible") ? 1 : 0);
            put("time_type", plugin.getConfig().getInt("default_settings.time.default_type"));
            put("time", plugin.getConfig().getBoolean("default_settings.time.is_visible") ? 1 : 0);
        }};
    }

}
