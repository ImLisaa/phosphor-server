package net.phosphor.phosphor.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.hollowcube.minestom.extensions.ExtensionBootstrap;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.phosphor.phosphor.api.instance.InstanceProvider;
import net.phosphor.phosphor.api.instance.type.InstanceType;
import net.phosphor.phosphor.api.permission.PhosphorPermissionPlayer;
import net.phosphor.phosphor.properties.PermissionsProperties;
import net.phosphor.phosphor.properties.ServerProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public abstract class PhosphorServerAPI {

    private static PhosphorServerAPI instance;
    private final ExtensionBootstrap extensionBootstrap;
    private final InstanceProvider instanceProvider;
    private final ServerProperties serverProperties;
    private final PermissionsProperties permissionsProperties;

    public PhosphorServerAPI(@NotNull ExtensionBootstrap extensionBootstrap) throws IOException {
        instance = this;
        this.serverProperties = new ServerProperties();
        this.extensionBootstrap = extensionBootstrap;
        this.instanceProvider = new InstanceProvider();
        this.instanceProvider.createInstance("world", InstanceType.FLAT);

        this.permissionsProperties = new PermissionsProperties();

        MinecraftServer.getConnectionManager().setPlayerProvider((PhosphorPermissionPlayer::new));

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

    public void opPlayer(String user) {
        JsonArray jsonArray = this.permissionsProperties.getJsonArray();
        jsonArray.add(user);
        this.permissionsProperties.setProperty("operators", jsonArray);
        this.permissionsProperties.updateDocument();
        this.permissionsProperties.reload();
    }

    public void deopPlayer(String user) {
        JsonArray jsonArray = this.permissionsProperties.getJsonArray();
        for (int i = 0; i < jsonArray.asList().size(); i++) {
            if (jsonArray.get(i).getAsString().equals(user)) {
                jsonArray.remove(i);
                break;
            }
        }
        this.permissionsProperties.setProperty("operators", jsonArray);
        this.permissionsProperties.updateDocument();
        this.permissionsProperties.reload();
    }

    public boolean isOp(String user) {
        JsonArray jsonArray = this.permissionsProperties.getJsonArray();
        boolean isOp = false;
        for (int i = 0; i < jsonArray.asList().size(); i++) {
            if (jsonArray.get(i).getAsString().equals(user)) {
                isOp = true;
                break;
            }
        }
        return isOp;
    }
}
