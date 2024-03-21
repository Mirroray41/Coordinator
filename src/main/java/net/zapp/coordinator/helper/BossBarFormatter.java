package net.zapp.coordinator.helper;

import org.bukkit.Location;
import org.bukkit.boss.BarColor;

import static net.zapp.coordinator.Coordinator.colorize;
import static net.zapp.coordinator.Coordinator.translationManager;

public class BossBarFormatter {
    public static BarColor getColorFromYaw(float rotation) {
        rotation += 180;
        if (rotation >= 25 && rotation < 65) {
            return BarColor.WHITE;
        } else if (rotation >= 65 && rotation < 115) {
            return BarColor.BLUE;
        } else if (rotation >= 115 && rotation < 155) {
            return BarColor.WHITE;
        } else if (rotation >= 155 && rotation < 205) {
            return BarColor.GREEN;
        } else if (rotation >= 205 && rotation < 245) {
            return BarColor.WHITE;
        } else if (rotation >= 245 && rotation < 295) {
            return BarColor.YELLOW;
        } else if (rotation >= 295 && rotation < 335) {
            return BarColor.WHITE;
        } else if (rotation >= 335 && rotation <= 360) {
            return BarColor.RED;
        } else if (rotation >= 0 && rotation < 25) {
            return BarColor.RED;
        } else {
            return BarColor.PURPLE;
        }
    }

    public static String getFormattedLocation(Location location) {
        return colorize(translationManager.get("prefixes.location") + Math.round(location.getX()) + "§r" + " " + translationManager.get("prefixes.location") + Math.round(location.getY()) + "§r" + " " + translationManager.get("prefixes.location") + Math.round(location.getZ()) + "§r");
    }

    public static String getFormattedLocation2(Location location) {
        return colorize(translationManager.get("prefixes.location") + String.format("%.2f", location.getX()) + "§r" + " " + translationManager.get("prefixes.location") + String.format("%.2f", location.getY()) + "§r" + " " + translationManager.get("prefixes.location") + String.format("%.2f", location.getZ()) + "§r");
    }

    public static String getProperDirection(float rotation) {
        rotation += 180;
        if (rotation >= 25 && rotation < 65) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.north_east") + "§r");
        } else if (rotation >= 65 && rotation < 115) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.east") + "§r");
        } else if (rotation >= 115 && rotation < 155) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.south_east") + "§r");
        } else if (rotation >= 155 && rotation < 205) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.south") + "§r");
        } else if (rotation >= 205 && rotation < 245) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.south_west") + "§r");
        } else if (rotation >= 245 && rotation < 295) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.west") + "§r");
        } else if (rotation >= 295 && rotation < 335) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.north_west") + "§r");
        } else if (rotation >= 335 && rotation <= 360) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.north") + "§r");
        } else if (rotation >= 0 && rotation < 25) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.north") + "§r");
        } else {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.unknown") + "§r");
        }
    }

    public static String getProperDirectionPrediction(float rotation) {
        rotation += 180;
        if (rotation >= 25 && rotation < 65) {
            return colorize(translationManager.get("prefixes.direction") + "-Z +X" + "§r");
        } else if (rotation >= 65 && rotation < 115) {
            return colorize(translationManager.get("prefixes.direction") + "+X" + "§r");
        } else if (rotation >= 115 && rotation < 155) {
            return colorize(translationManager.get("prefixes.direction") + "+Z +X" + "§r");
        } else if (rotation >= 155 && rotation < 205) {
            return colorize(translationManager.get("prefixes.direction") + "+Z" + "§r");
        } else if (rotation >= 205 && rotation < 245) {
            return colorize(translationManager.get("prefixes.direction") + "+Z -X" + "§r");
        } else if (rotation >= 245 && rotation < 295) {
            return colorize(translationManager.get("prefixes.direction") + "-X" + "§r");
        } else if (rotation >= 295 && rotation < 335) {
            return colorize(translationManager.get("prefixes.direction") + "-Z -X" + "§r");
        } else if (rotation >= 335 && rotation <= 360) {
            return colorize(translationManager.get("prefixes.direction") + "-Z" + "§r");
        } else if (rotation >= 0 && rotation < 25) {
            return colorize(translationManager.get("prefixes.direction") + "-Z" + "§r");
        } else {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("directions.unknown") + "§r");
        }
    }

    public static String getFormattedTime(long time) {
        long offsetTime;
        if (time > 18000) {
            offsetTime = time - 18000;
        } else {
            offsetTime = time + 6000;
        }
        int sec = (int) Math.floor((offsetTime % 1000) / 16.666);
        int hour = (int) Math.floor((double) offsetTime / 1000);


        return colorize(timeIntToString(hour) + ":" + timeIntToString(sec));
    }

    private static String timeIntToString(int time) {
        if (time < 10) {
            return translationManager.get("prefixes.time") + "0" + time + "§r";
        } else {
            return translationManager.get("prefixes.time") + time + "§r";
        }
    }
}
