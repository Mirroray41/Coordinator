package net.zapp.coordinator.runnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.zapp.coordinator.Coordinator.*;
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
            String gameMinutes = timeIntToString(getMinutesFromGameTime(getAjustedGameTime(player.getWorld().getTime())));
            String gameHours = timeIntToString(getHoursFromGameTime(getAjustedGameTime(player.getWorld().getTime())));


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

            if(!playerSettingDatabase.getVisibility(player) || plugin.getConfig().getBoolean("hotbar")) {
                bossBar.setVisible(false);
                if (!playerSettingDatabase.getVisibility(player)) continue;
            } else {
                bossBar.setVisible(true);
            }

            if (playerSettingDatabase.getDirectionType(player) == 0) {
                direction = getProperDirection(player.getLocation().getYaw());
            } else if (playerSettingDatabase.getDirectionType(player) == 1) {
                direction = getProperDirectionPrediction(player.getLocation().getYaw());
            } else {
                direction = getProperDirection(player.getLocation().getYaw()) + " " + getProperDirectionPrediction(player.getLocation().getYaw());
            }

            x = playerSettingDatabase.getLocationType(player) == 0 ? getFormattedLocation((float) player.getLocation().getX()) : getFormattedLocation2((float) player.getLocation().getX());
            y = playerSettingDatabase.getLocationType(player) == 0 ? getFormattedLocation((float) player.getLocation().getY()) : getFormattedLocation2((float) player.getLocation().getY());
            z = playerSettingDatabase.getLocationType(player) == 0 ? getFormattedLocation((float) player.getLocation().getZ()) : getFormattedLocation2((float) player.getLocation().getZ());
            minutes = playerSettingDatabase.getTimeType(player) == 0 ? gameMinutes : realTimeMinutes;
            hours = playerSettingDatabase.getTimeType(player) == 0 ? gameHours : realTimeHours;




            String assembledTitle = translationManager.getString("bossbar")
                    .replace("{x}", playerSettingDatabase.getLocation(player) ? x : "")
                    .replace("{y}", playerSettingDatabase.getLocation(player) ? y : "")
                    .replace("{z}", playerSettingDatabase.getLocation(player) ? z : "")
                    .replace("{direction}", playerSettingDatabase.getDirection(player) ? direction : "");


            String patternStr = "\\{HH\\}(.*?)\\{MM\\}";
            Pattern pattern = Pattern.compile(patternStr);

            Matcher matcher = pattern.matcher(assembledTitle);

            if (matcher.find()) {
                String matchedGroup = matcher.group(1);
                assembledTitle =  assembledTitle.replace(matchedGroup, playerSettingDatabase.getTime(player) ? matchedGroup : "");
            }

            assembledTitle = assembledTitle.replace("{HH}", playerSettingDatabase.getTime(player) ? hours : "")
                    .replace("{MM}", playerSettingDatabase.getTime(player) ? minutes : "");

            bossBar.setColor(getColorFromYaw(player.getLocation().getYaw()));
            bossBar.setTitle(colorize(assembledTitle));

            if (!BossBarMap.containsKey(player.getUniqueId())) {
                BossBarMap.put(player.getUniqueId(), assembledTitle);
            } else {
                BossBarMap.replace(player.getUniqueId(), assembledTitle);
            }

            if (plugin.getConfig().getBoolean("hotbar")) {
                if (playerSettingDatabase.getTime(player)) {
                    String color = getColorFromYaw(player.getLocation().getYaw()).toString();
                    if (color.equals("PURPLE")) {
                        color = "LIGHT_PURPLE";
                    }
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                            ChatColor.valueOf(color) + colorize(assembledTitle)));
                }
            }
        }
    }

    public BarColor getColorFromYaw(float rotation) {
        float localRotation = rotation;
        if (localRotation < 0) {
            localRotation += 360;
        }
        if (localRotation >= 25 && localRotation < 65) {
            return BarColor.valueOf(config.getString("style.south_west"));
        } else if (localRotation >= 65 && localRotation < 115) {
            return BarColor.valueOf(config.getString("style.west"));
        } else if (localRotation >= 115 && localRotation < 155) {
            return BarColor.valueOf(config.getString("style.north_west"));
        } else if (localRotation >= 155 && localRotation < 205) {
            return BarColor.valueOf(config.getString("style.north"));
        } else if (localRotation >= 205 && localRotation < 245) {
            return BarColor.valueOf(config.getString("style.north_east"));
        } else if (localRotation >= 245 && localRotation < 295) {
            return BarColor.valueOf(config.getString("style.east"));
        } else if (localRotation >= 295 && localRotation < 335) {
            return BarColor.valueOf(config.getString("style.south_east"));
        } else if (localRotation >= 335 && localRotation <= 360) {
            return BarColor.valueOf(config.getString("style.south"));
        } else if (localRotation >= 0 && localRotation < 25) {
            return BarColor.valueOf(config.getString("style.south_west"));
        } else {
            return BarColor.valueOf(config.getString("style.unknown"));
        }
    }

    public static String getFormattedLocation(float i) {
        return String.valueOf(Math.round(Math.floor(i)));
    }

    public static String getFormattedLocation2(float i) {
        return String.format("%.2f", i);
    }

    public String getProperDirection(float rotation) {
        float localRotation = rotation;
        if (localRotation < 0) {
            localRotation += 360;
        }
        if (localRotation >= 25 && localRotation < 65) {
            return colorize(translationManager.getString("translations.directions.south_west"));
        } else if (localRotation >= 65 && localRotation < 115) {
            return colorize(translationManager.getString("translations.directions.west"));
        } else if (localRotation >= 115 && localRotation < 155) {
            return colorize(translationManager.getString("translations.directions.north_west"));
        } else if (localRotation >= 155 && localRotation < 205) {
            return colorize(translationManager.getString("translations.directions.north"));
        } else if (localRotation >= 205 && localRotation < 245) {
            return colorize(translationManager.getString("translations.directions.north_east"));
        } else if (localRotation >= 245 && localRotation < 295) {
            return colorize(translationManager.getString("translations.directions.east"));
        } else if (localRotation >= 295 && localRotation < 335) {
            return colorize(translationManager.getString("translations.directions.south_east"));
        } else if (localRotation >= 335 && localRotation <= 360) {
            return colorize(translationManager.getString("translations.directions.south"));
        } else if (localRotation >= 0 && localRotation < 25) {
            return colorize(translationManager.getString("translations.directions.south"));
        } else {
            return colorize(translationManager.getString("translations.directions.unknown"));
        }
    }

    public static String getProperDirectionPrediction(float rotation) {
        float localRotation = rotation;
        if (localRotation < 0) {
            localRotation += 360;
        }
        if (localRotation >= 25 && localRotation < 65) {
            return colorize("+Z -X");
        } else if (localRotation >= 65 && localRotation < 115) {
            return colorize("-X");
        } else if (localRotation >= 115 && localRotation < 155) {
            return colorize("-Z -X");
        } else if (localRotation >= 155 && localRotation < 205) {
            return colorize("-Z");
        } else if (localRotation >= 205 && localRotation < 245) {
            return colorize("-Z +X");
        } else if (localRotation >= 245 && localRotation < 295) {
            return colorize("+X");
        } else if (localRotation >= 295 && localRotation < 335) {
            return colorize("+Z +X");
        } else if (localRotation >= 335 && localRotation <= 360) {
            return colorize("+Z");
        } else if (localRotation >= 0 && localRotation < 25) {
            return colorize("+Z");
        } else {
            return colorize(translationManager.getString("directions.unknown"));
        }
    }

    public static int getAjustedGameTime(long time) {
        long offsetTime;
        if (time > 18000) {
            offsetTime = time - 18000;
        } else {
            offsetTime = time + 6000;
        }
        return (int) offsetTime;
    }

    public static int getMinutesFromGameTime(int time) {
        return (int) Math.floor((time % 1000) / 16.666);
    }

    public static int getHoursFromGameTime(int time) {
        return (int) Math.floor((double) time / 1000);
    }

    public static String timeIntToString(int time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return String.valueOf(time);
        }
    }
}
