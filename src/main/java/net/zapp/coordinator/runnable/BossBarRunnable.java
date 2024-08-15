package net.zapp.coordinator.runnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            String hours;
            String minutes;
            String gameMinutes = timeIntToString(getMinutesFromGameTime(getAjustedGameTime(getServer().getWorld("world").getTime())));
            String gameHours = timeIntToString(getHoursFromGameTime(getAjustedGameTime(getServer().getWorld("world").getTime())));


            OffsetDateTime realWorldTimeMillis = OffsetDateTime.now();

            OffsetDateTime adjustedTime = realWorldTimeMillis.plusHours((long) timeOffset);

            long millisSinceEpoch = adjustedTime.toInstant().toEpochMilli();

            Date realWorldTime = new Date(millisSinceEpoch);

            SimpleDateFormat sdfMinutes = new SimpleDateFormat("mm");
            SimpleDateFormat sdfHours = new SimpleDateFormat("HH");
            String realTimeMinutes = colorize(sdfMinutes.format(realWorldTime));
            String realTimeHours = colorize(sdfHours.format(realWorldTime));

            if (player == null) {
                continue;
            }

            Map<String, Integer> config = playerConfig.get(player.getUniqueId());

            if(config.get("visibility") == 0 || plugin.getConfig().getBoolean("hotbar")) {
                bossBar.setVisible(false);
                if (config.get("visibility") == 0) continue;
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
            minutes = config.get("time_type") == 0 ? gameMinutes : realTimeMinutes;
            hours = config.get("time_type") == 0 ? gameHours : realTimeHours;



            String assembledTitle = translationManager.get("bossbar")
                    .replace("{x}", config.get("location") == 1 ? String.valueOf(x) : "")
                    .replace("{y}", config.get("location") == 1 ? String.valueOf(y) : "")
                    .replace("{z}", config.get("location") == 1 ? String.valueOf(z) : "")
                    .replace("{direction}", config.get("direction") == 1 ? direction : "");


            String patternStr = "\\{HH\\}(.*?)\\{MM\\}";
            Pattern pattern = Pattern.compile(patternStr);

            Matcher matcher = pattern.matcher(assembledTitle);

            if (matcher.find()) {
                String matchedGroup = matcher.group(1);
                assembledTitle =  assembledTitle.replace(matchedGroup, config.get("time") == 1 ? matchedGroup : "");
            }

            assembledTitle = assembledTitle.replace("{HH}", config.get("time") == 1 ? hours : "")
                    .replace("{MM}", config.get("time") == 1 ? minutes : "");

            bossBar.setColor(getColorFromYaw(player.getLocation().getYaw()));
            bossBar.setTitle(colorize(assembledTitle));

            if (!BossBarMap.containsKey(player.getUniqueId())) {
                BossBarMap.put(player.getUniqueId(), assembledTitle);
            } else {
                BossBarMap.replace(player.getUniqueId(), assembledTitle);
            }

            if (plugin.getConfig().getBoolean("hotbar")) {
                if (config.get("visibility") == 1) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                            ChatColor.valueOf(getColorFromYaw(player.getLocation().getYaw()).toString()) + colorize(assembledTitle)));
                }
            }
        }
    }
}
