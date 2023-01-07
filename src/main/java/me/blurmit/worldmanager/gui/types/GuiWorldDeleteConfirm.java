package me.blurmit.worldmanager.gui.types;

import me.blurmit.worldmanager.WorldManagerPlugin;
import me.blurmit.worldmanager.gui.GuiButton;
import me.blurmit.worldmanager.gui.GuiMenu;
import me.blurmit.worldmanager.gui.GuiType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiWorldDeleteConfirm extends GuiMenu {

    private final WorldManagerPlugin plugin;
    private final String name;

    public GuiWorldDeleteConfirm(WorldManagerPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;

        addButton(new GuiButton(ChatColor.GREEN + "Confirm")
                .setMaterial(Material.GREEN_WOOL)
                .setLore(ChatColor.GRAY + "I understand that this action is irreversible", ChatColor.GRAY + "Delete " + name)
                .setSlot(2));

        addButton(new GuiButton(ChatColor.RED + "Cancel")
                .setMaterial(Material.RED_WOOL)
                .setLore(ChatColor.GRAY + "Do not delete " + name)
                .setSlot(6));
    }

    @Override
    public String getName() {
        return ChatColor.DARK_GRAY + "Delete " + name + "?";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        switch (event.getRawSlot()) {
            case 2: {
                plugin.getWorldManager().deleteWorld(name);
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiWorldList(plugin, 0));
                break;
            }
            case 6: {
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiWorldList(plugin, 0));
            }
        }
    }

    @Override
    public GuiType getType() {
        return GuiType.FILLED;
    }

}
