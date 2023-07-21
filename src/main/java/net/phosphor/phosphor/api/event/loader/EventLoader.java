package net.phosphor.phosphor.api.event.loader;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record EventLoader(@NotNull String packageName) {


    public List<Class<?>> load() {
        try(ScanResult scanResult = new ClassGraph().verbose(false).enableAllInfo().acceptPackages(this.packageName).scan()) {
            return scanResult.getClassesImplementing(EventListener.class).loadClasses();
        } catch (Exception exception) {
            MinecraftServer.LOGGER.error("There was an error loading events.");
            exception.printStackTrace();
            return List.of();
        }
    }
}
