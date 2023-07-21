package net.phosphor.phosphor.commands.defaults;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.permission.Permission;
import net.phosphor.phosphor.api.PhosphorServerAPI;
import net.phosphor.phosphor.api.command.NetworkCommand;
import net.phosphor.phosphor.api.text.KyoriTextFormatter;

@NetworkCommand
public class GameModeCommand extends Command {

    public GameModeCommand() {
        super("gamemode", "gm");

        setCondition((sender, commandString) -> sender.hasPermission(new Permission("phosphor.command.gamemode")));

        ArgumentInteger numberArgument = ArgumentType.Integer("gamemode");

        numberArgument.setCallback((sender, exception) -> {
            Component wrongArgument = new KyoriTextFormatter("<dark_gray>┃ <red>Argument <dark_gray>› <gray>Invalid " +
                    "argument. Please enter a number between 0 and 3.").getComponent();
            sender.sendMessage(wrongArgument);
        });

        setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player player) {
                if (player.hasPermission(new Permission("phosphor.command.gamemode"))) {
                    Component wrongArgument = new KyoriTextFormatter("<dark_gray>┃ <red>Argument <dark_gray>› <gray>Invalid " +
                            "argument. Please enter a number between 0 and 3.").getComponent();
                    sender.sendMessage(wrongArgument);
                } else {
                    String noPermissionMessage = PhosphorServerAPI.getInstance().getServerProperties().getProperty("no-permission-message", String.class);
                    Component noPermission =
                            new KyoriTextFormatter(noPermissionMessage).getComponent();
                    player.sendMessage(noPermission);
                }
            } else {
                sender.sendMessage("You must be a player to execute this command.");
            }
        });

        addSyntax((sender, context) -> {
            if (sender instanceof Player player) {
                if (player.hasPermission(new Permission("phosphor.command.gamemode"))) {
                    Integer gameModeId = context.get(numberArgument);
                    GameMode gameMode = GameMode.fromId(gameModeId);
                    player.setGameMode(gameMode);
                    Component message = new KyoriTextFormatter("<dark_gray>┃ <red>GameMode <dark_gray>› <gray>Your " +
                            "GameMode has been changed to <#419D78>" + gameMode.name() + " <dark_gray>.").getComponent();
                    player.sendMessage(message);
                } else {
                    String noPermissionMessage = PhosphorServerAPI.getInstance().getServerProperties().getProperty("no-permission-message", String.class);
                    Component noPermission =
                            new KyoriTextFormatter(noPermissionMessage).getComponent();
                    player.sendMessage(noPermission);
                }
            } else {
                sender.sendMessage("You must be a player to execute this command.");
            }
        }, numberArgument);
    }
}
