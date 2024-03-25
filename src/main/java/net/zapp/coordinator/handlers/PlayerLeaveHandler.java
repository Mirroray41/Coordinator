package net.zapp.coordinator.handlers;

import net.zapp.coordinator.Coordinator;
import net.zapp.coordinator.runnable.BossBarRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveHandler implements Listener {
    public PlayerLeaveHandler(Coordinator plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Coordinator.playerBossBars.remove(player.getUniqueId());
        BossBarRunnable.BossBarMap.remove(player.getUniqueId());

        Coordinator.flushSettingsToFile();
    }
}
