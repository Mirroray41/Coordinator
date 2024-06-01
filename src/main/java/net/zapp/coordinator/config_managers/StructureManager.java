package net.zapp.coordinator.config_managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

import static net.zapp.coordinator.Coordinator.plugin;
import static org.bukkit.Bukkit.getLogger;

public class StructureManager {
    public File customConfigFile;
    private YamlConfiguration customConfig;

    final Logger logger = getLogger();

    String fileName;

    public StructureManager(JavaPlugin plugin, String fileName) {
        this.fileName = fileName;
        customConfigFile = new File(plugin.getDataFolder(), fileName);
        if (!customConfigFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    }

    public String get(String path) {
        Object obj = customConfig.get(path);
        if (obj == null) {
            logger.warning("Missing translation at: " +  path);
            return "";
        }
        return obj.toString();
    }



    public void reloadStructure() {
        customConfigFile = new File(plugin.getDataFolder(), fileName);
        if (!customConfigFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    }
}
