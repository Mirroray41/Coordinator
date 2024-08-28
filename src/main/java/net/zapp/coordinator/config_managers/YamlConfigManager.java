package net.zapp.coordinator.config_managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class YamlConfigManager {
    public File configFile;
    private YamlConfiguration config;

    private final String fileName;

    public YamlConfigManager(JavaPlugin plugin, String fileName) {
        this.fileName = fileName;
        reload(plugin);
    }

    public Object get(String path) {
        Object obj = config.get(path);
        if (obj == null) {
            return new Object();
        }
        return obj;
    }

    public String getString(String path) {
        String text = config.getString(path);
        if (text == null) {
            return "";
        }
        return text;
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

    public void reload(Plugin plugin) {
        configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
