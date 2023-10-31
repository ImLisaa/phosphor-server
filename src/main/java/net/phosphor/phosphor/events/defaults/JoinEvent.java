package net.phosphor.phosphor.events.defaults;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.fakeplayer.FakePlayer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.phosphor.phosphor.api.PhosphorServerAPI;
import net.phosphor.phosphor.api.permission.PhosphorPermissionPlayer;
import net.phosphor.phosphor.api.text.KyoriTextFormatter;
import org.jetbrains.annotations.NotNull;

public class JoinEvent implements EventListener<PlayerLoginEvent> {

    @Override
    public @NotNull Class<PlayerLoginEvent> eventType() {
        return PlayerLoginEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (player instanceof FakePlayer) {
            return Result.SUCCESS;
        }

        PhosphorServerAPI.getInstance().getInstanceProvider().getInstance("world").ifPresent(instanceContainer -> {
            event.setSpawningInstance(instanceContainer);
            Component joinMessage = new KyoriTextFormatter("<dark_gray>┃ <red>Server <dark_gray>› <#419D78>" + player.getUsername() + " <gray>joined.").getComponent();
            PhosphorServerAPI.getInstance().broadcastMessage(joinMessage);
        });
        return Result.SUCCESS;
    }
}
