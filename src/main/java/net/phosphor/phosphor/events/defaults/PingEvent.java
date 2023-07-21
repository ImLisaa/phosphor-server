package net.phosphor.phosphor.events.defaults;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.ResponseData;
import net.phosphor.phosphor.api.PhosphorServerAPI;
import net.phosphor.phosphor.api.text.KyoriTextFormatter;
import org.jetbrains.annotations.NotNull;

public class PingEvent implements EventListener<ServerListPingEvent> {

    @Override
    public @NotNull Class<ServerListPingEvent> eventType() {
        return ServerListPingEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull ServerListPingEvent event) {
        String motdString = PhosphorServerAPI.getInstance().getServerProperties().getProperty("motd", String.class);
        Integer slots = PhosphorServerAPI.getInstance().getServerProperties().getProperty("max-players", Integer.class);
        Component motd = new KyoriTextFormatter(motdString).getComponent();
        ResponseData responseData = new ResponseData();
        responseData.setMaxPlayer(slots);
        responseData.setDescription(motd);
        event.setResponseData(responseData);
        return Result.SUCCESS;
    }
}
