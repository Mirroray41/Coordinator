package net.zapp.coordinator.config_managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getLogger;

public class PlayerSettingManager {
    public final File customConfigFile;
    private final YamlConfiguration customConfig;

    final Logger logger = getLogger();

    public PlayerSettingManager(JavaPlugin plugin, String fileName) {
        customConfigFile = new File(plugin.getDataFolder(), fileName);
        if (!customConfigFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    }

    public void set(String path, Object value) {
        customConfig.set(path, value);
        saveConfig();
    }

    public void saveConfig() {
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            logger.warning(Arrays.toString(e.getStackTrace()));
        }
    }

    public Map<UUID, Map<String, Integer>> convertYamlToMap(File yamlFile) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);
        Map<UUID, Map<String, Integer>> uuidToConfigMap = new HashMap<>();

        for (String uuid : config.getKeys(false)) {
            Map<String, Integer> configMap = new HashMap<>();
            ConfigurationSection section = config.getConfigurationSection(uuid);
            assert section != null;
            for (String key : section.getKeys(true)) {
                if (section.get(key) instanceof Integer) {
                    configMap.put(key, section.getInt(key));
                }
            }
            uuidToConfigMap.put(UUID.fromString(uuid), configMap);
        }

        return uuidToConfigMap;
    }
}
