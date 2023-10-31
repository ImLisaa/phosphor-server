package net.phosphor.phosphor.commands.defaults;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.permission.Permission;
import net.phosphor.phosphor.api.PhosphorServerAPI;
import net.phosphor.phosphor.api.command.NetworkCommand;
import net.phosphor.phosphor.api.text.KyoriTextFormatter;
import org.jetbrains.annotations.NotNull;

/**
 * @author : Lisa
 * @created : 30. Okt.. 2023 | 18:25
 * @contact : @imlisaa_ (Discord)
 * <p>
 * You are not allowed to modify or make changes to
 * this file without permission.
 **/
@NetworkCommand
public class DeopCommand extends Command {

    public DeopCommand() {
        super("deop");

        ArgumentString argumentString = ArgumentType.String("username");

        setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player player) {
                if (player.hasPermission(new Permission("phosphor.command.deop"))) {
                    Component wrongArgument = new KyoriTextFormatter("<dark_gray>┃ <red>Argument <dark_gray>› <gray>Please " +
                            "enter a valid username.").getComponent();
                    sender.sendMessage(wrongArgument);
                } else {
                    String noPermissionMessage = PhosphorServerAPI.getInstance().getServerProperties().getProperty("no-permission-message", String.class);
                    Component noPermission =
                            new KyoriTextFormatter(noPermissionMessage).getComponent();
                    player.sendMessage(noPermission);
                }
            } else {
                sender.sendMessage("Please provide a valid username.");
            }
        });

        addSyntax((sender, context) -> {
            String userName = context.get(argumentString);
            if (sender instanceof Player player) {
                if (player.hasPermission(new Permission("phosphor.command.op"))) {
                    if (!isOp(userName)) {
                        Component message = new KyoriTextFormatter("<dark_gray>┃ <red>OP <dark_gray>› <gray>The player " +
                                "<#419D78>" + userName + " <gray>is not an <red>Operator<dark_gray>.").getComponent();
                        player.sendMessage(message);
                        return;
                    }
                    PhosphorServerAPI.getInstance().deopPlayer(userName);
                    Component message = new KyoriTextFormatter("<dark_gray>┃ <red>OP <dark_gray>› <gray>You have " +
                            "successfully removed <#419D78>" + userName + " <gray>as <red>Operator<dark_gray>.").getComponent();
                    player.sendMessage(message);
                } else {
                    String noPermissionMessage = PhosphorServerAPI.getInstance().getServerProperties().getProperty("no-permission-message", String.class);
                    Component noPermission = new KyoriTextFormatter(noPermissionMessage).getComponent();
                    player.sendMessage(noPermission);
                }
            } else {
                if (!isOp(userName)) {
                    sender.sendMessage("The player " + userName + " is not an Operator.");
                    return;
                }
                sender.sendMessage("You have successfully removed " + userName + " as Operator.");
                PhosphorServerAPI.getInstance().deopPlayer(userName);
            }
        }, argumentString);
    }

    private boolean isOp(@NotNull String username) {
        return PhosphorServerAPI.getInstance().isOp(username);
    }
}
