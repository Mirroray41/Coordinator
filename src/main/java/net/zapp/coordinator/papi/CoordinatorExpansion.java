package net.zapp.coordinator.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.zapp.coordinator.runnable.BossBarRunnable;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoordinatorExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "coordinator";
    }

    @Override
    public @NotNull String getAuthor() {
        return "zapp";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.2";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (BossBarRunnable.BossBarMap == null) return null;
        if (params.equalsIgnoreCase("title")) {
            return BossBarRunnable.BossBarMap.get(player.getUniqueId());
        }
        return null;
    }
}
