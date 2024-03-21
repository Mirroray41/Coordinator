package net.zapp.coordinator.runnable;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static net.zapp.coordinator.Coordinator.*;
import static net.zapp.coordinator.helper.BossBarFormatter.*;
import static org.bukkit.Bukkit.getServer;

public class BossBarRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (Map.Entry<UUID, BossBar> entry : playerBossBars.entrySet()) {
            BossBar bossBar = entry.getValue();
            Player player = getServer().getPlayer(entry.getKey());
            String location;
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

            location = config.get("location_type") == 0 ? getFormattedLocation(player.getLocation()) : getFormattedLocation2(player.getLocation());
            time = config.get("time_type") == 0 ? gameTime : realTime;
            bossBar.setColor(getColorFromYaw(player.getLocation().getYaw()));

            bossBar.setTitle((config.get("location") == 1 ? " " + location + " " : "") + (config.get("direction") == 1 ? " " + direction + " " : "") + (config.get("time") == 1 ? " " + time + " " : ""));
        }
    }
}
