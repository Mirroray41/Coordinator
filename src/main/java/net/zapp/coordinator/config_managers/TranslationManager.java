package net.zapp.coordinator.config_managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

import static net.zapp.coordinator.Coordinator.plugin;
import static org.bukkit.Bukkit.getLogger;

public class TranslationManager {
    public File customConfigFile;
    private YamlConfiguration customConfig;

    final Logger logger = getLogger();

    String fileName;

    public TranslationManager(JavaPlugin plugin, String fileName) {
        this.fileName = fileName;
        customConfigFile = new File(plugin.getDataFolder(), fileName);
        if (!customConfigFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    }


    public String getStringWithReplace(String path) {
        Object obj = customConfig.get(path);
        if (obj == null) {
            logger.warning("Missing translation at: " +  path);
            return "";
        }
        return obj.toString().replace("{state}", nonReplaceGet("state"))
                .replace("{select_state}", nonReplaceGet("select_state"));
    }

    private String nonReplaceGet(String path) {
        Object obj = customConfig.get(path);
        if (obj == null) {
            logger.warning("Missing translation at: " +  path);
            return "";
        }
        return obj.toString();
    }

    public void reloadLang() {
        customConfigFile = new File(plugin.getDataFolder(), fileName);
        if (!customConfigFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    }

    public void reload(JavaPlugin plugin) {
    }
}
