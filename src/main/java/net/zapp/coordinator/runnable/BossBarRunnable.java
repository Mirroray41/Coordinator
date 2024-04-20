package net.zapp.coordinator.runnable;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.*;

import static net.zapp.coordinator.Coordinator.*;
import static net.zapp.coordinator.helper.BossBarFormatter.*;
import static org.bukkit.Bukkit.getServer;

public class BossBarRunnable extends BukkitRunnable {

    public static Map<UUID, String> BossBarMap = new HashMap<>();
    @Override
    public void run() {
        for (Map.Entry<UUID, BossBar> entry : playerBossBars.entrySet()) {
            BossBar bossBar = entry.getValue();
            Player player = getServer().getPlayer(entry.getKey());
            String x;
            String y;
            String z;
            String direction;
            String time;
            String gameTime = getFormattedTime(Objects.requireNonNull(getServer().getWorld("world")).getTime());

            long realWorldTimeMillis = System.currentTimeMillis();

            Date realWorldTime = new Date(realWorldTimeMillis);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String realTime = colorize(translationManager.get("prefixes.time") + sdf.format(realWorldTime) + "§r");

            if (player == null) {
                continue;
            }

            Map<String, Integer> config = playerConfig.get(player.getUniqueId());

            if(config.get("visibility") == 0) {
                bossBar.setVisible(false);
                continue;
            } else {
                bossBar.setVisible(true);
            }

            if (config.get("direction_type") == 0) {
                direction = getProperDirection(player.getLocation().getYaw());
            } else if (config.get("direction_type") == 1) {
                direction = getProperDirectionPrediction(player.getLocation().getYaw());
            } else {
                direction = getProperDirection(player.getLocation().getYaw()) + " " + getProperDirectionPrediction(player.getLocation().getYaw());
            }

            x = config.get("location_type") == 0 ? getFormattedLocation((float) player.getLocation().getX()) : getFormattedLocation2((float) player.getLocation().getX());
            y = config.get("location_type") == 0 ? getFormattedLocation((float) player.getLocation().getY()) : getFormattedLocation2((float) player.getLocation().getY());
            z = config.get("location_type") == 0 ? getFormattedLocation((float) player.getLocation().getZ()) : getFormattedLocation2((float) player.getLocation().getZ());
            time = config.get("time_type") == 0 ? gameTime : realTime;
            bossBar.setColor(getColorFromYaw(player.getLocation().getYaw()));


            String assembledTitle = translationManager.get("bossbar")
                    .replace("{x}", config.get("location") == 1 ? String.valueOf(x) : "")
                    .replace("{y}", config.get("location") == 1 ? String.valueOf(y) : "")
                    .replace("{z}", config.get("location") == 1 ? String.valueOf(z) : "")
                    .replace("{direction}", config.get("direction") == 1 ? direction : "")
                    .replace("{time}", config.get("time") == 1 ? time : "");

            bossBar.setTitle(colorize(assembledTitle));

            if (!BossBarMap.containsKey(player.getUniqueId())) {
                BossBarMap.put(player.getUniqueId(), assembledTitle);
            } else {
                BossBarMap.replace(player.getUniqueId(), assembledTitle);
            }
        }
    }
}
