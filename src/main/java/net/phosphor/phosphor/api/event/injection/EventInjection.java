package net.phosphor.phosphor.api.event.injection;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

public class EventInjection {

    private EventInjection(List<Class<?>> classSet) {
        for (Class<?> eventClass : classSet) {
            try {
                EventListener<?> event = (EventListener<?>) eventClass.getDeclaredConstructor().newInstance();
                MinecraftServer.getGlobalEventHandler().addListener(event);
                MinecraftServer.LOGGER.info("Injected event {}.", event.eventType().getSimpleName());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException exception) {
                MinecraftServer.LOGGER.error("An error occured whilst injecting commands.");
                exception.printStackTrace();
            }
        }
    }

    public static void inject(List<Class<?>> classSet) {
        new EventInjection(classSet);
    }
}
