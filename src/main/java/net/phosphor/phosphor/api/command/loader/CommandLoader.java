package net.phosphor.phosphor.api.command.loader;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import net.minestom.server.MinecraftServer;
import net.phosphor.phosphor.api.command.NetworkCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public record CommandLoader(@NotNull String packageName) {


    public List<Class<?>> load() {
        try(ScanResult scanResult = new ClassGraph().verbose(false).enableAnnotationInfo().acceptPackages(this.packageName).scan()) {
            return scanResult.getClassesWithAnnotation(NetworkCommand.class).loadClasses();
        } catch (Exception exception) {
            MinecraftServer.LOGGER.error("There was an error loading commands.");
            exception.printStackTrace();
            return List.of();
        }
    }
}
