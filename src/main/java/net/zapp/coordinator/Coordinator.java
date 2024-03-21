package net.zapp.coordinator;

import net.zapp.coordinator.commands.CrCommand;
import net.zapp.coordinator.config_managers.TranslationManager;
import net.zapp.coordinator.handlers.ContainerHandler;
import net.zapp.coordinator.handlers.PlayerJoinHandler;
import net.zapp.coordinator.handlers.PlayerLeaveHandler;
import net.zapp.coordinator.config_managers.PlayerSettingManager;
import net.zapp.coordinator.runnable.BossBarRunnable;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public final class Coordinator extends JavaPlugin {

    private static final int CONFIG_VERSION = 1;
    private static final int TRANSLATIONS_VERSION = 1;

    public final Logger logger = getLogger();

    public static final Map<UUID, BossBar> playerBossBars = new HashMap<>();

    public static Map<UUID, Map<String, Integer>> playerConfig = new HashMap<>();

    public static Map<String, Integer> defaultConfig;

    public static JavaPlugin plugin;

    private static PlayerSettingManager playerSettingManager;
    public static TranslationManager translationManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logger.info("Coordinator plugin has been enabled!");

        plugin = this;

        saveDefaultConfig();
        reloadConfig();

        playerSettingManager = new PlayerSettingManager(plugin, "player_settings.yml");
        translationManager = new TranslationManager(plugin, "translations.yml");

        if (getConfig().getInt("file_format") != CONFIG_VERSION) {
            logger.warning("Incorrect config format check for changes on the official github page");
            logger.warning("Coordinator requires config file format version " + CONFIG_VERSION + ", your current file format version is " + getConfig().getInt("file_format"));
        }
        if (!Objects.equals(translationManager.get("file_format"), String.valueOf(TRANSLATIONS_VERSION))) {
            logger.warning("Incorrect config format check for changes on the official github page");
            logger.warning("Coordinator requires translations file format version " + TRANSLATIONS_VERSION + ", your current file format version is " + translationManager.get("file_format"));
        }

        int interval = getConfig().getInt("bossbar_refresh_interval");
        if (interval < 1) {
            interval = 1;
        }

        defaultConfig = new HashMap<>() {{
            put("visibility", getConfig().getBoolean("default_settings.visibility") ? 1 : 0);
            put("location_type", getConfig().getInt("default_settings.location_type"));
            put("location", getConfig().getBoolean("default_settings.location") ? 1 : 0);
            put("direction_type", getConfig().getInt("default_settings.direction_type"));
            put("direction", getConfig().getBoolean("default_settings.direction") ? 1 : 0);
            put("time_type", getConfig().getInt("default_settings.time_type"));
            put("time", getConfig().getBoolean("default_settings.time") ? 1 : 0);
        }};


        playerConfig = playerSettingManager.convertYamlToMap(playerSettingManager.customConfigFile);

        Objects.requireNonNull(this.getCommand("cr")).setExecutor(new CrCommand());

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

    public static String colorize(String msg)
    {
        return "§r" + msg.replace("&", "§");
    }

    public static void reload() {
        plugin.reloadConfig();
        defaultConfig = new HashMap<>() {{
            put("visibility", plugin.getConfig().getBoolean("default_settings.visibility") ? 1 : 0);
            put("location_type", plugin.getConfig().getInt("default_settings.location_type"));
            put("location", plugin.getConfig().getBoolean("default_settings.location") ? 1 : 0);
            put("direction_type", plugin.getConfig().getInt("default_settings.direction_type"));
            put("direction", plugin.getConfig().getBoolean("default_settings.direction") ? 1 : 0);
            put("time_type", plugin.getConfig().getInt("default_settings.time_type"));
            put("time", plugin.getConfig().getBoolean("default_settings.time") ? 1 : 0);
        }};
    }

}
