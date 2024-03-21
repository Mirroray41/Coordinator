package net.zapp.coordinator.handlers;

import net.zapp.coordinator.Coordinator;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinHandler implements Listener {
    public PlayerJoinHandler(Coordinator plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        BossBar bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);

        bossBar.addPlayer(player);

        Coordinator.playerBossBars.put(player.getUniqueId(), bossBar);
        if (!Coordinator.playerConfig.containsKey(player.getUniqueId())) {
            Coordinator.playerConfig.put(player.getUniqueId(), Coordinator.defaultConfig);
        }
    }
}
