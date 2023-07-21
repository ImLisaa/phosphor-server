package net.phosphor.phosphor.api.extension;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.extensions.Extension;

import java.lang.reflect.Field;
import java.util.*;

public class PhosphorExtensionManager {

    public void reloadExtensions() {
        Collection<Extension> extensions = MinecraftServer.getExtensionManager().getExtensions().stream().filter(this::isValidExtension).toList();
        for (Extension extension : extensions) {
            unloadExtension(extension);
            loadExtensions();
        }
    }

    public void reloadExtension(Extension extension) {
        if (extension.isReloadable()) {
            unloadExtension(extension);
            loadExtension(extension);
        } else {
            MinecraftServer.LOGGER.warn("Extension {} is not reloadable!", extension.getOrigin().getName());
        }
    }

    public void unloadExtension(Extension extension) {
        List<String> dependencies = new LinkedList<>(extension.getDependents());
        for (String dependency : dependencies) {
            Extension dependencyExtension = Objects.requireNonNull(getExtensions()).get(dependency.toLowerCase());
            if (dependencyExtension != null) {
                MinecraftServer.LOGGER.info("Unloading dependency {} because it depends on {}.", dependency, extension.getOrigin().getName());
                unloadExtension(dependencyExtension);
            }
        }
        MinecraftServer.LOGGER.info("Unloading extension {}.", extension.getOrigin().getName());
        unload(extension);
    }

    public void loadExtension(Extension extension) {
        MinecraftServer.LOGGER.info("Loading extension {}.", extension.getOrigin().getName());
        extension.preInitialize();
        MinecraftServer.getGlobalEventHandler().addChild(extension.getEventNode());
        extension.initialize();
        extension.postInitialize();
    }


    private void unload(Extension extension) {
        extension.preTerminate();
        extension.terminate();
        EventNode<Event> extensionEventNode = extension.getEventNode();
        MinecraftServer.getGlobalEventHandler().removeChild(extensionEventNode);
        extension.postTerminate();
    }

    private void loadExtensions() {
        for (Extension extension : Objects.requireNonNull(getExtensions().values())) {
            loadExtension(extension);
        }
    }

    private boolean isValidExtension(Extension extension) {
        return extension.isReloadable();
    }

    private Map<String, Extension> getExtensions() {
        try {
            Field field = MinecraftServer.getExtensionManager().getClass().getDeclaredField("extensions");
            field.setAccessible(true);
            return (Map<String, Extension>) field.get(MinecraftServer.getExtensionManager());
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
            return Map.of();
        }
    }
}
