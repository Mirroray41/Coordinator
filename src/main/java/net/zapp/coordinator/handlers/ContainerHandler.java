package net.zapp.coordinator.handlers;

import net.zapp.coordinator.Coordinator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import static net.zapp.coordinator.Coordinator.*;
import static net.zapp.coordinator.helper.GUIHelper.rollChoices;
import static net.zapp.coordinator.helper.GUISettingHandling.syncGUI;

public class ContainerHandler implements Listener {
    public ContainerHandler(Coordinator plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    Logger logger = Bukkit.getLogger();

    @EventHandler
    public void onContainerInteract(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(colorize(translationManager.get("translations.gui.title")))) {
            event.setCancelled(true);
            int slot = event.getSlot();
            Map<String, Integer> config = Coordinator.playerConfig.get(event.getWhoClicked().getUniqueId());

            if (slot == 10 && plugin.getConfig().getBoolean("globals.visibility.is_enabled")) {
                config.replace("visibility", config.get("visibility") == 0 ? 1 : 0);
                playerSettingDatabase.setVisibility((Player) event.getWhoClicked(),
                        !playerSettingDatabase.getVisibility((Player) event.getWhoClicked()));
            } else if (slot == 4 && plugin.getConfig().getBoolean("globals.location.is_enabled")) {
                config.replace("location", config.get("location") == 0 ? 1 : 0);
                playerSettingDatabase.setLocation((Player) event.getWhoClicked(),
                        !playerSettingDatabase.getLocation((Player) event.getWhoClicked()));
            } else if (slot == 6 && plugin.getConfig().getBoolean("globals.direction.is_enabled")) {
                config.replace("direction", config.get("direction") == 0 ? 1 : 0);
                playerSettingDatabase.setDirection((Player) event.getWhoClicked(),
                        !playerSettingDatabase.getDirection((Player) event.getWhoClicked()));
            } else if (slot == 8 && plugin.getConfig().getBoolean("globals.time.is_enabled")) {
                config.replace("time", config.get("time") == 0 ? 1 : 0);
                playerSettingDatabase.setTime((Player) event.getWhoClicked(),
                        !playerSettingDatabase.getTime((Player) event.getWhoClicked()));
            } else if (slot == 13) {
                config.replace("location_type", rollChoices(config.get("location_type"), 1));
                playerSettingDatabase.setLocationType((Player) event.getWhoClicked(),
                        rollChoices(playerSettingDatabase.getLocationType((Player) event.getWhoClicked()), 1));
            } else if (slot == 15) {
                config.replace("direction_type", rollChoices(config.get("direction_type"), 2));
                playerSettingDatabase.setDirectionType((Player) event.getWhoClicked(),
                        rollChoices(playerSettingDatabase.getDirectionType((Player) event.getWhoClicked()), 2));
            }else if (slot == 17) {
                config.replace("time_type", rollChoices(config.get("time_type"), 1));
                playerSettingDatabase.setTimeType((Player) event.getWhoClicked(),
                        rollChoices(playerSettingDatabase.getTimeType((Player) event.getWhoClicked()), 1));
            } else {
                return;
            }

            syncGUI((Player) event.getWhoClicked(), event.getInventory());

            Coordinator.playerConfig.replace(event.getWhoClicked().getUniqueId(), config);
        }
    }
}
