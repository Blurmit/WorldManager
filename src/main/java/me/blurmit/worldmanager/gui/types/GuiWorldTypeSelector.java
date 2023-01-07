package me.blurmit.worldmanager.gui.types;

import me.blurmit.worldmanager.WorldManagerPlugin;
import me.blurmit.worldmanager.gui.GuiButton;
import me.blurmit.worldmanager.gui.GuiMenu;
import me.blurmit.worldmanager.gui.GuiType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiWorldTypeSelector extends GuiMenu {

    private final WorldManagerPlugin plugin;
    private final WorldCreator creator;

    public GuiWorldTypeSelector(WorldManagerPlugin plugin, WorldCreator creator) {
        this.plugin = plugin;
        this.creator = creator;

        addButton(new GuiButton(ChatColor.GREEN + "Normal")
                .setMaterial(Material.PODZOL)
                .setLore(ChatColor.GRAY + "A normally generated Minecraft world")
                .setSlot(2));
        addButton(new GuiButton(ChatColor.GOLD + "Flat")
                .setMaterial(Material.DIRT)
                .setLore(ChatColor.GRAY + "A flat Minecraft world")
                .setSlot(4));
        addButton(new GuiButton(ChatColor.YELLOW + "Amplified")
                .setMaterial(Material.MYCELIUM)
                .setLore(ChatColor.GRAY + "A Minecraft world with amplified terrain")
                .setSlot(6));
    }

    @Override
    public String getName() {
        return "Select World Environment";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        switch (event.getRawSlot()) {
            case 2: {
                creator.type(WorldType.NORMAL);
                break;
            }
            case 4: {
                creator.type(WorldType.FLAT);
                break;
            }
            case 6: {
                creator.type(WorldType.AMPLIFIED);
            }
        }

        plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiCreateWorld(plugin, creator));
    }

    @Override
    public GuiType getType() {
        return GuiType.FILLED;
    }

}
