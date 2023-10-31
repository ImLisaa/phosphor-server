package net.phosphor.phosphor.events.defaults;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.fakeplayer.FakePlayer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.phosphor.phosphor.api.PhosphorServerAPI;
import net.phosphor.phosphor.api.permission.PhosphorPermissionPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * @author : Lisa
 * @created : 30. Okt.. 2023 | 18:58
 * @contact : @imlisaa_ (Discord)
 * <p>
 * You are not allowed to modify or make changes to
 * this file without permission.
 **/
public class AsyncJoinEvent implements EventListener<AsyncPlayerPreLoginEvent> {

    @Override
    public @NotNull Class<AsyncPlayerPreLoginEvent> eventType() {
        return AsyncPlayerPreLoginEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull AsyncPlayerPreLoginEvent event) {
        Player player = event.getPlayer();

        return Result.SUCCESS;
    }
}
