package net.phosphor.phosphor.api.scoreboard;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.TaskSchedule;

public class Scoreboard {

    private final Sidebar sidebar;
    private int animationTaskId = 0;

    public Scoreboard(Component component) {
        this.sidebar = new Sidebar(component);
    }

    public void setLines(Component... lines) {
        for (int i = 0; i < lines.length; i++) {
            this.sidebar.createLine(new Sidebar.ScoreboardLine("line-" + i, lines[i], lines.length - i));
        }
    }

    public void updateLine(int lineId, Component updateLine) {
        if (this.sidebar.getLine("line-" + lineId) == null) {
            MinecraftServer.LOGGER.warn("Scoreboard has no line with id {}.", lineId);
            return;
        }
        this.sidebar.updateLineContent("line-" + lineId, updateLine);
    }

    public void runAnimation(Component... components) {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            animationTaskId++;

            if (animationTaskId > components.length) {
                animationTaskId = 0;
            }

            this.sidebar.setTitle(components[animationTaskId]);
            return TaskSchedule.nextTick();
        });
    }

    public void addPlayer(Player player) {
        this.sidebar.addViewer(player);
    }

    public void removePlayer(Player player) {
        this.sidebar.removeViewer(player);
    }

    public void addPlayers(Player... players) {
        for (Player player : players) {
            this.sidebar.addViewer(player);
        }
    }

    public void removePlayers(Player... players) {
        for (Player player : players) {
            this.sidebar.removeViewer(player);
        }
    }

    public Sidebar getSidebar() {
        return this.sidebar;
    }
}
