package net.zapp.coordinator.helper;

import org.bukkit.Location;
import org.bukkit.boss.BarColor;

import static net.zapp.coordinator.Coordinator.colorize;
import static net.zapp.coordinator.Coordinator.translationManager;

public class BossBarFormatter {
    public static BarColor getColorFromYaw(float rotation) {
        float localRotation = rotation;
        if (localRotation < 0) {
            localRotation += 360;
        }
        if (localRotation >= 25 && localRotation < 65) {
            return BarColor.WHITE;
        } else if (localRotation >= 65 && localRotation < 115) {
            return BarColor.YELLOW;
        } else if (localRotation >= 115 && localRotation < 155) {
            return BarColor.WHITE;
        } else if (localRotation >= 155 && localRotation < 205) {
            return BarColor.RED;
        } else if (localRotation >= 205 && localRotation < 245) {
            return BarColor.WHITE;
        } else if (localRotation >= 245 && localRotation < 295) {
            return BarColor.BLUE;
        } else if (localRotation >= 295 && localRotation < 335) {
            return BarColor.WHITE;
        } else if (localRotation >= 335 && localRotation <= 360) {
            return BarColor.GREEN;
        } else if (localRotation >= 0 && localRotation < 25) {
            return BarColor.GREEN;
        } else {
            return BarColor.PURPLE;
        }
    }

    public static String getFormattedLocation(float i) {
        return String.valueOf(Math.round(i));
    }

    public static String getFormattedLocation2(float i) {
        return String.format("%.2f", i);
    }

    public static String getProperDirection(float rotation) {
        float localRotation = rotation;
        if (localRotation < 0) {
            localRotation += 360;
        }
        if (localRotation >= 25 && localRotation < 65) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.south_west") + "§r");
        } else if (localRotation >= 65 && localRotation < 115) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.west") + "§r");
        } else if (localRotation >= 115 && localRotation < 155) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.north_west") + "§r");
        } else if (localRotation >= 155 && localRotation < 205) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.north") + "§r");
        } else if (localRotation >= 205 && localRotation < 245) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.north_east") + "§r");
        } else if (localRotation >= 245 && localRotation < 295) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.east") + "§r");
        } else if (localRotation >= 295 && localRotation < 335) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.south_east") + "§r");
        } else if (localRotation >= 335 && localRotation <= 360) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.south") + "§r");
        } else if (localRotation >= 0 && localRotation < 25) {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.south") + "§r");
        } else {
            return colorize(translationManager.get("prefixes.direction") + translationManager.get("translations.directions.unknown") + "§r");
        }
    }

    public static String getProperDirectionPrediction(float rotation) {
        float localRotation = rotation;
        if (localRotation < 0) {
            localRotation += 360;
        }
        if (localRotation >= 25 && localRotation < 65) {
            return colorize(translationManager.get("prefixes.direction") + "+Z -X" + "§r");
        } else if (localRotation >= 65 && localRotation < 115) {
            return colorize(translationManager.get("prefixes.direction") + "-X" + "§r");
        } else if (localRotation >= 115 && localRotation < 155) {
            return colorize(translationManager.get("prefixes.direction") + "-Z -X" + "§r");
        } else if (localRotation >= 155 && localRotation < 205) {
            return colorize(translationManager.get("prefixes.direction") + "-Z" + "§r");
        } else if (localRotation >= 205 && localRotation < 245) {
            return colorize(translationManager.get("prefixes.direction") + "-Z +X" + "§r");
        } else if (localRotation >= 245 && localRotation < 295) {
            return colorize(translationManager.get("prefixes.direction") + "+X" + "§r");
        } else if (localRotation >= 295 && localRotation < 335) {
            return colorize(translationManager.get("prefixes.direction") + "+Z +X" + "§r");
        } else if (localRotation >= 335 && localRotation <= 360) {
            return colorize(translationManager.get("prefixes.direction") + "+Z" + "§r");
        } else if (localRotation >= 0 && localRotation < 25) {
            return colorize(translationManager.get("prefixes.direction") + "+Z" + "§r");
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
