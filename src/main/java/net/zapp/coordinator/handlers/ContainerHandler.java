package net.zapp.coordinator.handlers;

import net.zapp.coordinator.Coordinator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

import static net.zapp.coordinator.Coordinator.colorize;
import static net.zapp.coordinator.Coordinator.translationManager;
import static net.zapp.coordinator.helper.GUIHelper.rollChoices;
import static net.zapp.coordinator.helper.GUISettingHandling.syncGui;

public class ContainerHandler implements Listener {
    public ContainerHandler(Coordinator plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onContainerInteract(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(colorize(translationManager.get("translations.gui.title")))) {
            event.setCancelled(true);
            int slot = event.getSlot();
            Map<String, Integer> config = Coordinator.playerConfig.get(event.getWhoClicked().getUniqueId());

            if (slot == 10) {
                config.replace("visibility", config.get("visibility") == 0 ? 1 : 0);
            } else if (slot == 4) {
                config.replace("location", config.get("location") == 0 ? 1 : 0);
            } else if (slot == 6) {
                config.replace("direction", config.get("direction") == 0 ? 1 : 0);
            } else if (slot == 8) {
                config.replace("time", config.get("time") == 0 ? 1 : 0);
            } else if (slot == 13) {
                config.replace("location_type", rollChoices(config.get("location_type"), 1));
            } else if (slot == 15) {
                config.replace("direction_type", rollChoices(config.get("direction_type"), 2));
            }else if (slot == 17) {
                config.replace("time_type", rollChoices(config.get("time_type"), 1));
            } else {
                return;
            }

            syncGui((Player) event.getWhoClicked(), event.getInventory());

            Coordinator.playerConfig.replace(event.getWhoClicked().getUniqueId(), config);
        }
    }
}
