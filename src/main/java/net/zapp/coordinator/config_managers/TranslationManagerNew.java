package net.zapp.coordinator.config_managers;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import static org.bukkit.Bukkit.getLogger;

public class TranslationManagerNew extends YamlConfigManager {
    private YamlConfiguration customConfig;

    final Logger logger = getLogger();

    String fileName;

    public TranslationManagerNew(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    @Override
    public String getString(String path) {
        Object obj = customConfig.get(path);
        if (obj == null) {
            logger.warning("Missing translation at: " +  path);
            return "";
        }
        return obj.toString();
    }
    public String getStringWithReplace(String path) {
        Object obj = customConfig.get(path);
        if (obj == null) {
            logger.warning("Missing translation at: " +  path);
            return "";
        }
        return obj.toString().replace("{state}", this.getString("state"))
                .replace("{select_state}", this.getString("select_state"));
    }
}
