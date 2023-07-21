package net.phosphor.phosphor.api.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.phosphor.phosphor.api.item.ClickableItem;
import net.phosphor.phosphor.api.item.action.IClickAction;

import java.util.LinkedHashMap;
import java.util.Map;

public class SingletonInventory {

    private final Inventory inventory;
    private final Map<Integer, ClickableItem> inventoryItems = new LinkedHashMap<>();

    public SingletonInventory(InventoryType type, boolean clickable, Component title) {
        this.inventory = new Inventory(type, title);
        this.inventory.addInventoryCondition(((player, slot, clickType, result) -> {
            result.setCancel(!clickable);

            this.clickIfPreset(slot, player);
        }));
    }

    public SingletonInventory setItem(int slot, ItemStack stack, IClickAction clickAction) {
        this.inventoryItems.put(slot, new ClickableItem(stack, clickAction));
        this.inventory.setItemStack(slot, stack);
        return this;
    }

    public SingletonInventory setItem(int slot, ClickableItem clickableItem) {
        this.inventoryItems.put(slot, clickableItem);
        this.inventory.setItemStack(slot, clickableItem.itemStack());
        return this;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void removeItem(int slot) {
        this.inventory.setItemStack(slot, ItemStack.AIR);
        this.inventoryItems.remove(slot);
    }

    public void setTitle(Component title) {
        this.inventory.setTitle(title);
    }

    private void clickIfPreset(int slot, Player player) {
        if (this.inventoryItems.containsKey(slot)) {
            this.inventoryItems.get(slot).click(player);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Map<Integer, ClickableItem> getInventoryItems() {
        return inventoryItems;
    }
}
