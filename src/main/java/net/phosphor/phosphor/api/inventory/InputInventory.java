package net.phosphor.phosphor.api.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.client.play.ClientNameItemPacket;

public class InputInventory {

    private final Inventory inventory;

    public InputInventory(Component name, ItemStack icon) {
        this.inventory = new Inventory(InventoryType.ANVIL, name);
        this.inventory.setItemStack(0, icon);
    }

    public void open(Player player) {
        player.openInventory(this.inventory);
    }
}
