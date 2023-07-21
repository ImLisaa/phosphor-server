package net.phosphor.phosphor.api.instance;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.fakeplayer.FakePlayer;
import net.minestom.server.entity.fakeplayer.FakePlayerOption;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.world.DimensionType;
import net.phosphor.phosphor.api.instance.type.InstanceType;
import net.phosphor.phosphor.api.json.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class InstanceProvider {

    private final Map<String, InstanceContainer> instances;

    public InstanceProvider() throws IOException {
        this.instances = new LinkedHashMap<>();
        Path mapPath = Path.of("maps");
        if (Files.notExists(mapPath)) {
            Files.createDirectory(mapPath);
        }
    }

    public Map<String, InstanceContainer> getInstances() {
        return instances;
    }

    public Set<InstanceContainer> getRawInstanceSet() {
        return Set.copyOf(instances.values());
    }

    public Optional<InstanceContainer> getInstance(String instanceName) {
        return Optional.ofNullable(instances.get(instanceName));
    }

    public InstanceContainer _UNSAFE_getInstance(String instanceName) {
        return getInstance(instanceName).orElse(null);
    }

    public void createInstance(String name, InstanceType instanceType) throws IOException {
        Path worldPath = Path.of("maps/" + name);
        if (Files.notExists(worldPath)) {
            Files.createDirectory(worldPath);
            UUID containerIdentifier = UUID.randomUUID();
            InstanceContainer container = new InstanceContainer(containerIdentifier, DimensionType.OVERWORLD, new AnvilLoader(worldPath));
            container.setGenerator(instanceType.generator());
            container.setChunkSupplier(LightingChunk::new);
            this.instances.put(name, container);
            MinecraftServer.getInstanceManager().registerInstance(container);
            this.writeIdToFile(containerIdentifier, Path.of("maps/" + name, "id.json"));
            FakePlayer.initPlayer(UUID.randomUUID(), "dummy", new FakePlayerOption().setRegistered(true).setInTabList(true), fakePlayer -> {
                if(!Objects.equals(fakePlayer.getInstance(), container)){
                    fakePlayer.setInstance(container, new Pos(0, 0, 0));
                }
                fakePlayer.teleport(new Pos(1, 0, 1));
                MinecraftServer.getSchedulerManager().buildTask(() -> {
                    fakePlayer.remove();
                    container.saveChunksToStorage().join();
                }).delay(TaskSchedule.duration(500, ChronoUnit.MILLIS)).schedule();
            });
            return;
        }
        loadInstance(name);
    }

    public void loadInstance(String name) {
        if (!this.instances.containsKey(name)) {
            Path containerPath = Path.of("maps/" + name);
            Path idPath = Path.of("maps/" + name, "id.json");
            if (Files.notExists(idPath)) {
                writeIdToFile(UUID.randomUUID(), idPath);
            }
            Document document = new Document(idPath);
            UUID containerIdentifier = UUID.fromString(document.get("identifier", String.class));
            InstanceContainer container = new InstanceContainer(containerIdentifier, DimensionType.OVERWORLD, new AnvilLoader(containerPath));
            container.setChunkSupplier(LightingChunk::new);
            this.instances.put(name, container);
            MinecraftServer.getInstanceManager().registerInstance(container);
            MinecraftServer.LOGGER.info("Instance {} has been registered with identifier {}.", name, containerIdentifier);
        }
    }

    public void unloadInstance(String name) {
        getInstance(name).ifPresentOrElse(instanceContainer -> {
            instanceContainer.saveChunksToStorage().join();
            MinecraftServer.getInstanceManager().unregisterInstance(instanceContainer);
            this.instances.remove(name);
            MinecraftServer.LOGGER.info("Instance {} has been unloaded.", name);
        }, () -> MinecraftServer.LOGGER.warn("Instance {} is not loaded.", name));
    }

    public void loadAllInstances() {
        File[] files = Path.of("maps").toFile().listFiles();
        if(files == null) {
            MinecraftServer.LOGGER.info("No instances to load.");
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                loadInstance(file.getName());
            }
        }
    }

    private void writeIdToFile(UUID containerIdentifier, Path path) {
        Document document = new Document();
        document.addIfNotExists("identifier", containerIdentifier.toString()).write(path);
    }
}
