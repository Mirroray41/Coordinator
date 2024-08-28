package net.zapp.coordinator.config_managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getLogger;

public class TranslationManagerNew {
    final Logger logger = getLogger();

    public File configFile;
    private YamlConfiguration config;

    private final String fileName;

    public TranslationManagerNew(JavaPlugin plugin, String fileName) {
        this.fileName = fileName;
        reload(plugin);
    }

    public String getString(String path) {
        Object obj = config.get(path);
        if (obj == null) {
            logger.warning("Missing translation at: " +  path);
            return "";
        }
        return obj.toString();
    }
    public String getStringWithReplace(String path) {
        Object obj = config.get(path);
        if (obj == null) {
            logger.warning("Missing translation at: " +  path);
            return "";
        }
        return obj.toString().replace("{state}", this.getString("state"))
                .replace("{select_state}", this.getString("select_state"));
    }

    public void reload(Plugin plugin) {
        configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }


    public Object get(String path) {
        Object obj = config.get(path);
        if (obj == null) {
            return new Object();
        }
        return obj;
    }


    public int getInt(String path) {
        return config.getInt(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public float getFloat(String path) {
        Object obj = config.get(path);
        if (obj instanceof Float) {
            return (Float) obj;
        }
        return 0.0F;
    }

    public boolean has(String path) {
        Object obj = config.get(path);
        if (obj == null) {
            return false;
        }
        return true;
    }
}
