package net.phosphor.phosphor;

import net.hollowcube.minestom.extensions.ExtensionBootstrap;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.extensions.ExtensionManager;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.Instance;
import net.phosphor.phosphor.api.PhosphorServerAPI;
import net.phosphor.phosphor.api.command.injector.CommandInjector;
import net.phosphor.phosphor.api.command.loader.CommandLoader;
import net.phosphor.phosphor.events.defaults.AsyncJoinEvent;
import net.phosphor.phosphor.events.defaults.JoinEvent;
import net.phosphor.phosphor.events.defaults.PingEvent;
import net.phosphor.phosphor.events.defaults.QuitEvent;
import net.phosphor.phosphor.properties.ServerProperties;
import net.phosphor.phosphor.terminal.PhosphorTerminal;

import java.io.IOException;
import java.util.List;

public class PhosphorServerBootstrap {

    private static PhosphorServerBootstrap instance;
    private final ExtensionBootstrap extensionBootstrap;

    private PhosphorServerBootstrap() throws IOException {
        instance = this;
        this.extensionBootstrap = ExtensionBootstrap.init();
        new PhosphorServerAPI(this.extensionBootstrap) {
        };
        MinecraftServer.setTerminalEnabled(false);
        MinecraftServer.setBrandName("Phosphor 1.20.1-R0.1-SNAPSHOT");
        System.setProperty("minestom.chunk-view-distance", "10");
        System.setProperty("minestom.entity-view-distance", "32");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            MinecraftServer.LOGGER.info("Shutting down Phosphor-Server...");
            for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
                instance.saveChunksToStorage().join();
                MinecraftServer.LOGGER.info("Instance {} has been saved.", instance.getUniqueId());
            }
            PhosphorTerminal.stop();
            MinecraftServer.stopCleanly();
        }));
        ServerProperties serverProperties = PhosphorServerAPI.getInstance().getServerProperties();


        if (serverProperties.getProperty("enable-default-commands", Boolean.class)) {
            CommandLoader commandLoader = new CommandLoader("net.phosphor.phosphor.commands.defaults");
            List<Class<?>> classSet = commandLoader.load();
            CommandInjector.inject(classSet);
        }

        if (serverProperties.getProperty("online-mode", Boolean.class)) {
            MojangAuth.init();
        }

        if (serverProperties.getProperty("bungeecord", Boolean.class)) {
            BungeeCordProxy.enable();
        }

        if (serverProperties.getProperty("velocity", Boolean.class) && BungeeCordProxy.isEnabled()) {
            MinecraftServer.LOGGER.warn("BungeeCord and VelocitySupport are both enabled. VelocitySupport will be disabled.");
            serverProperties.setProperty("velocity", false);
            serverProperties.updateDocument();
            serverProperties.reload();
        }

        if (serverProperties.getProperty("velocity", Boolean.class)) {
            VelocityProxy.enable(serverProperties.getProperty("velocity-secret", String.class));
        }

        if (serverProperties.getProperty("enable-default-events", Boolean.class)) {
            EventNode<Event> defaultNode = EventNode.all("default-node");
            defaultNode.addListener(new PingEvent());
            defaultNode.addListener(new JoinEvent());
            defaultNode.addListener(new QuitEvent());
            defaultNode.addListener(new AsyncJoinEvent());
            MinecraftServer.getGlobalEventHandler().addChild(defaultNode);
        }
        PhosphorTerminal.start();
        this.extensionBootstrap.start(serverProperties.getProperty("address", String.class), serverProperties.getProperty("port", int.class));
    }

    public static PhosphorServerBootstrap getInstance() {
        return instance;
    }

    public static void main(String[] args) throws IOException {
        new PhosphorServerBootstrap();
    }
}
