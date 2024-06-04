package net.zapp.coordinator.helper;

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
            return colorize(translationManager.get("translations.directions.south_west"));
        } else if (localRotation >= 65 && localRotation < 115) {
            return colorize(translationManager.get("translations.directions.west"));
        } else if (localRotation >= 115 && localRotation < 155) {
            return colorize(translationManager.get("translations.directions.north_west"));
        } else if (localRotation >= 155 && localRotation < 205) {
            return colorize(translationManager.get("translations.directions.north"));
        } else if (localRotation >= 205 && localRotation < 245) {
            return colorize(translationManager.get("translations.directions.north_east"));
        } else if (localRotation >= 245 && localRotation < 295) {
            return colorize(translationManager.get("translations.directions.east"));
        } else if (localRotation >= 295 && localRotation < 335) {
            return colorize(translationManager.get("translations.directions.south_east"));
        } else if (localRotation >= 335 && localRotation <= 360) {
            return colorize(translationManager.get("translations.directions.south"));
        } else if (localRotation >= 0 && localRotation < 25) {
            return colorize(translationManager.get("translations.directions.south"));
        } else {
            return colorize(translationManager.get("translations.directions.unknown"));
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
            return colorize(translationManager.get("directions.unknown"));
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
