package net.phosphor.phosphor.events.defaults;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.phosphor.phosphor.api.PhosphorServerAPI;
import net.phosphor.phosphor.api.text.KyoriTextFormatter;
import org.jetbrains.annotations.NotNull;

public class QuitEvent implements EventListener<PlayerDisconnectEvent> {

    @Override
    public @NotNull Class<PlayerDisconnectEvent> eventType() {
        return PlayerDisconnectEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerDisconnectEvent event) {
        Player player = event.getPlayer();
        Component joinMessage = new KyoriTextFormatter("<dark_gray>┃ <red>Server <dark_gray>› <#419D78>" + player.getUsername() + " <gray>left.").getComponent();
        PhosphorServerAPI.getInstance().broadcastMessage(joinMessage);
        return Result.SUCCESS;
    }
}
