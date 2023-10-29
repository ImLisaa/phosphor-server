package net.phosphor.phosphor.terminal;

import net.minestom.server.MinecraftServer;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PhosphorTerminal {

    private static final String PREFIX = "Phosphor-Server » ";
    private static volatile LineReader lineReader;
    private static volatile Terminal terminal;
    private static volatile boolean running = false;

    public static void start() {
        final Thread thread = new Thread(null, () -> {
            try {
                terminal = TerminalBuilder
                        .builder()
                        .system(true)
                        .dumb(true)
                        .encoding("UTF-8")
                        .name("Phosphor-Terminal")
                        .build();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            lineReader = LineReaderBuilder
                    .builder()
                    .terminal(terminal)
                    .build();

            running = true;

            while (running) {
                String command;

                try {
                    command = lineReader.readLine(PREFIX);
                    MinecraftServer.getCommandManager().execute(MinecraftServer.getCommandManager().getConsoleSender(), command);
                } catch (UserInterruptException exception) {
                    System.exit(0);
                    return;
                } catch (EndOfFileException e) {
                    return;
                }
            }
        }, "Phosphor-Terminal");
        thread.setDaemon(true);
        thread.start();
    }

    public static void stop() {
        running = false;
        if(terminal != null) {
            try {
                terminal.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            lineReader = null;
        }
    }
}
