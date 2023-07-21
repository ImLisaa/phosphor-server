package net.phosphor.phosphor.properties.defaults;

import net.minestom.server.MinecraftServer;
import net.minestom.server.world.Difficulty;
import net.phosphor.phosphor.api.json.Document;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class DefaultServerProperties {


    public static Document get() {
        Document document = new Document();
        try {
            document.addIfNotExists("address", Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException ignored) {
            MinecraftServer.LOGGER.error("Could not get HostAddress. Defaulting to 127.0.0.1");
            document.addIfNotExists("address", "127.0.0.1");
        }
        document.addIfNotExists("port", 25565);
        document.addIfNotExists("max-players", 20);
        document.addIfNotExists("motd", "A Phosphor Server");
        document.addIfNotExists("online-mode", true);
        document.addIfNotExists("difficulty", Difficulty.EASY.name().toUpperCase());
        document.addIfNotExists("enable-default-commands", true);
        document.addIfNotExists("enable-default-handlers", true);
        document.addIfNotExists("enable-default-events", true);
        document.addIfNotExists("no-permission-message", "<dark_gray>┃ <red>Security <dark_gray>› <gray>You do not have permission to execute this command.");
        document.addIfNotExists("bungeecord", false);
        document.addIfNotExists("velocity", false);
        document.addIfNotExists("velocity-secret", "");
        return document;
    }
}
