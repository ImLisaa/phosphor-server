package net.phosphor.phosphor.api;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.phosphor.phosphor.api.extension.PhosphorExtensionManager;
import net.phosphor.phosphor.api.instance.InstanceProvider;
import net.phosphor.phosphor.api.instance.type.InstanceType;
import net.phosphor.phosphor.properties.ServerProperties;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public abstract class PhosphorServerAPI {

    private static PhosphorServerAPI instance;
    private final MinecraftServer minecraftServer;
    private final InstanceProvider instanceProvider;
    private final ServerProperties serverProperties;
    private final PhosphorExtensionManager phosphorExtensionManager;

    public PhosphorServerAPI(@NotNull MinecraftServer minecraftServer) throws IOException {
        instance = this;
        this.serverProperties = new ServerProperties();
        this.minecraftServer = minecraftServer;
        this.phosphorExtensionManager = new PhosphorExtensionManager();
        this.instanceProvider = new InstanceProvider();
        this.instanceProvider.createInstance("world", InstanceType.FLAT);

        MinecraftServer.LOGGER.info("PhosphorServerAPI initialized.");
    }

    public static PhosphorServerAPI getInstance() {
        return instance;
    }

    public MinecraftServer getMinecraftServer() {
        return minecraftServer;
    }

    public InstanceProvider getInstanceProvider() {
        return instanceProvider;
    }

    public ServerProperties getServerProperties() {
        return serverProperties;
    }

    public PhosphorExtensionManager getPhosphorExtensionManager() {
        return phosphorExtensionManager;
    }

    public Optional<Player> findPlayer(String name) {
        return Optional.ofNullable(MinecraftServer.getConnectionManager().getPlayer(name));
    }

    public Optional<Player> findPlayer(UUID uniqueId) {
        return Optional.ofNullable(MinecraftServer.getConnectionManager().getPlayer(uniqueId));
    }

    public void broadcastMessage(Component message) {
        for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }

    public void changeSlots(int slots) {
        this.serverProperties.setProperty("max-players", slots);
        this.serverProperties.updateDocument();
        this.serverProperties.reload();
    }
}
