package net.zapp.coordinator.handlers;

import net.zapp.coordinator.Coordinator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.Map;
import java.util.logging.Logger;

import static net.zapp.coordinator.Coordinator.*;
import static net.zapp.coordinator.gui.GUISettingHandling.syncGUI;

public class ContainerHandler implements Listener {
    public ContainerHandler(Coordinator plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onContainerInteract(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(colorize(translationManager.getString("translations.gui.title")))) {
            event.setCancelled(true);
            int slot = event.getSlot();

            if (slot == 10 && plugin.getConfig().getBoolean("globals.visibility.is_enabled")) {
                playerSettingDatabase.setVisibility((Player) event.getWhoClicked(),
                        !playerSettingDatabase.getVisibility((Player) event.getWhoClicked()));
            } else if (slot == 4 && plugin.getConfig().getBoolean("globals.location.is_enabled")) {
                playerSettingDatabase.setLocation((Player) event.getWhoClicked(),
                        !playerSettingDatabase.getLocation((Player) event.getWhoClicked()));
            } else if (slot == 6 && plugin.getConfig().getBoolean("globals.direction.is_enabled")) {
                playerSettingDatabase.setDirection((Player) event.getWhoClicked(),
                        !playerSettingDatabase.getDirection((Player) event.getWhoClicked()));
            } else if (slot == 8 && plugin.getConfig().getBoolean("globals.time.is_enabled")) {
                playerSettingDatabase.setTime((Player) event.getWhoClicked(),
                        !playerSettingDatabase.getTime((Player) event.getWhoClicked()));
            } else if (slot == 13) {
                playerSettingDatabase.setLocationType((Player) event.getWhoClicked(),
                        rollChoices(playerSettingDatabase.getLocationType((Player) event.getWhoClicked()), 1));
            } else if (slot == 15) {
                playerSettingDatabase.setDirectionType((Player) event.getWhoClicked(),
                        rollChoices(playerSettingDatabase.getDirectionType((Player) event.getWhoClicked()), 2));
            }else if (slot == 17) {
                playerSettingDatabase.setTimeType((Player) event.getWhoClicked(),
                        rollChoices(playerSettingDatabase.getTimeType((Player) event.getWhoClicked()), 1));
            } else {
                return;
            }

            syncGUI((Player) event.getWhoClicked(), event.getInventory());
        }
    }


    public static int rollChoices(int previous, int rollLimit) {
        if (previous >= rollLimit) {
            return 0;
        }
        return previous+1;

    }
}
