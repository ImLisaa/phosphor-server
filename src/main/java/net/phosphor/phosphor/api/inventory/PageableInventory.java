package net.phosphor.phosphor.api.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.phosphor.phosphor.api.item.ClickableItem;

import java.util.List;

public abstract class PageableInventory<T> extends SingletonInventory {

    private final List<T> elements;
    private final int[] possibleSlots;
    private final ItemStack nextPageItem;
    private final ItemStack previousPageItem;
    private final ClickableItem nextPageClickableItem;
    private final ClickableItem previousPageClickableItem;

    private int currentPage = 1;

    public PageableInventory(InventoryType type, Component title, boolean clickable, int[] possibleSlots, List<T> values, ItemStack nextPageItem, ItemStack previousPageItem) {
        super(type, clickable, title);

        this.elements = values;
        this.possibleSlots = possibleSlots;
        this.nextPageItem = nextPageItem;
        this.previousPageItem = previousPageItem;
        this.nextPageClickableItem = new ClickableItem(nextPageItem, player -> buildPage(++currentPage));
        this.previousPageClickableItem = new ClickableItem(previousPageItem, player -> buildPage(--currentPage));
        buildPage(1);
    }

    public int calculateNextPageSlot() {
        return this.getInventory().getSize() - 1;
    }

    public int calculatePreviousPageSlot() {
        return this.getInventory().getSize() - 9;
    }

    public int getMaximalPage() {
        return elements.size() / possibleSlots.length;
    }

    public void buildPage(int id) {
        this.currentPage = id;
        this.clear();

        if (currentPage > 1) {
            setItem(calculatePreviousPageSlot(), previousPageClickableItem);
        } else {
            removeItem(calculatePreviousPageSlot());
        }

        if (elements.size() == possibleSlots.length) {
            removeItem(calculateNextPageSlot());
        } else if (currentPage < getMaximalPage()) {
            setItem(calculateNextPageSlot(), nextPageClickableItem);
        } else {
            removeItem(calculateNextPageSlot());
        }

        int stepId = 0;
        for (T element : elements.subList(possibleSlots.length * (currentPage - 1), Math.min(elements.size(), possibleSlots.length * (currentPage - 1) + possibleSlots.length))) {
            setItem(possibleSlots[stepId], constructItem(element));
            stepId++;
        }
        onPageChange(this);
    }

    public void clear() {
        for (int slot : possibleSlots) {
            getInventory().setItemStack(slot, ItemStack.AIR);
        }
    }

    public abstract ClickableItem constructItem(T value);

    public abstract void onPageChange(PageableInventory<T> inventory);
}
