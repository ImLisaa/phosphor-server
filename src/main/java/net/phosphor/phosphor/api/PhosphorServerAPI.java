package net.phosphor.phosphor.api;

import net.hollowcube.minestom.extensions.ExtensionBootstrap;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.extensions.ExtensionManager;
import net.phosphor.phosphor.api.instance.InstanceProvider;
import net.phosphor.phosphor.api.instance.type.InstanceType;
import net.phosphor.phosphor.properties.ServerProperties;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public abstract class PhosphorServerAPI {

    private static PhosphorServerAPI instance;
    private final ExtensionBootstrap extensionBootstrap;
    private final InstanceProvider instanceProvider;
    private final ServerProperties serverProperties;

    public PhosphorServerAPI(@NotNull ExtensionBootstrap extensionBootstrap) throws IOException {
        instance = this;
        this.serverProperties = new ServerProperties();
        this.extensionBootstrap = extensionBootstrap;
        this.instanceProvider = new InstanceProvider();
        this.instanceProvider.createInstance("world", InstanceType.FLAT);

        MinecraftServer.LOGGER.info("PhosphorServerAPI initialized.");
    }

    public static PhosphorServerAPI getInstance() {
        return instance;
    }

    public ExtensionBootstrap getExtensionBootstrap() {
        return this.extensionBootstrap;
    }

    public InstanceProvider getInstanceProvider() {
        return this.instanceProvider;
    }

    public ServerProperties getServerProperties() {
        return this.serverProperties;
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
