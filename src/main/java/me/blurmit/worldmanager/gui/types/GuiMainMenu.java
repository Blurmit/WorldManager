package me.blurmit.worldmanager.gui.types;

import me.blurmit.worldmanager.WorldManagerPlugin;
import me.blurmit.worldmanager.gui.GuiButton;
import me.blurmit.worldmanager.gui.GuiMenu;
import me.blurmit.worldmanager.gui.GuiType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiMainMenu extends GuiMenu {

    private final WorldManagerPlugin plugin;

    public GuiMainMenu(WorldManagerPlugin plugin) {
        this.plugin = plugin;

        addButton(new GuiButton(ChatColor.GREEN + "Create World")
                .setMaterial(Material.GRASS_BLOCK)
                .setLore(ChatColor.GRAY + "Click to create a new world!")
                .setSlot(11));
        addButton(new GuiButton(ChatColor.YELLOW + "Created Worlds")
                .setMaterial(Material.MAP)
                .setLore(ChatColor.GRAY + "Click to view the list of created worlds!")
                .setSlot(15));
        addButton(new GuiButton(ChatColor.RED + "Close")
                .setMaterial(Material.BARRIER)
                .setLore(ChatColor.GRAY + "Close the world management menu")
                .setSlot(31));
    }

    @Override
    public String getName() {
        return ChatColor.DARK_GRAY + "World Management";
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        switch (event.getRawSlot()) {
            case 11: {
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiCreateWorld(plugin, new WorldCreator("New World")));
                break;
            }
            case 15: {
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiWorldList(plugin, 0));
                break;
            }
            case 31: {
                plugin.getGuiManager().getMenus().remove(event.getWhoClicked().getUniqueId());
                event.getWhoClicked().closeInventory();
            }
        }
    }

    @Override
    public GuiType getType() {
        return GuiType.FILLED;
    }

}
