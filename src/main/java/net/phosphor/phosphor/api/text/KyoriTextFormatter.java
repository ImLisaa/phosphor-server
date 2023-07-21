package net.phosphor.phosphor.api.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class KyoriTextFormatter {

    private final Component component;

    public KyoriTextFormatter(String input) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        this.component = miniMessage.deserialize(input);
    }

    public static String stripColor(String input) {
        return PlainTextComponentSerializer.plainText().serialize(LegacyComponentSerializer.legacySection().deserialize(input));
    }

    public Component getComponent() {
        return component;
    }
}

