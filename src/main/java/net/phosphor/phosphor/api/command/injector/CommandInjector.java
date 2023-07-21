package net.phosphor.phosphor.api.command.injector;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class CommandInjector {

    private CommandInjector(List<Class<?>> classSet) {
        for (Class<?> commandClass : classSet) {
            try {
                Command command = (Command) commandClass.getDeclaredConstructor().newInstance();
                MinecraftServer.getCommandManager().register(command);
                MinecraftServer.LOGGER.info("Injected command {}.", command.getName());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException exception) {
                MinecraftServer.LOGGER.error("An error occured whilst injecting commands.");
                exception.printStackTrace();
            }
        }
    }

    public static void inject(List<Class<?>> classSet) {
        new CommandInjector(classSet);
    }
}
