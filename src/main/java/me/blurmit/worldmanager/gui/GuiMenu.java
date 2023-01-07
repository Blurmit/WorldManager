package me.blurmit.worldmanager.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class GuiMenu {

    private final Set<GuiButton> buttons;
    private Inventory inventory;
    private long page;

    public GuiMenu() {
        this.buttons = new HashSet<>();
        this.inventory = null;
        this.page = 0;
    }

    public abstract String getName();

    public abstract int getSlots();

    public abstract void callButton(InventoryClickEvent event);

    public abstract GuiType getType();

    public void addButton(GuiButton button) {
        this.buttons.add(button);
    }

    public Set<GuiButton> getButtons() {
        return this.buttons;
    }

    @Nullable
    public GuiButton getButtonByID(UUID uuid) {
        return this.buttons.stream().filter(button -> button.getID().equals(uuid)).findFirst().orElse(null);
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory != null ? inventory : getType().getInventory(this);
    }

    public boolean isFull(Inventory inventory) {
        return Arrays.stream(inventory.getContents()).filter(Objects::nonNull).count() == inventory.getSize();
    }

}
