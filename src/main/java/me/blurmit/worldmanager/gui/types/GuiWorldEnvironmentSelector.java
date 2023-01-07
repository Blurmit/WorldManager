package me.blurmit.worldmanager.gui.types;

import me.blurmit.worldmanager.WorldManagerPlugin;
import me.blurmit.worldmanager.gui.GuiButton;
import me.blurmit.worldmanager.gui.GuiMenu;
import me.blurmit.worldmanager.gui.GuiType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiWorldEnvironmentSelector extends GuiMenu {

    private final WorldManagerPlugin plugin;
    private final WorldCreator creator;

    public GuiWorldEnvironmentSelector(WorldManagerPlugin plugin, WorldCreator creator) {
        this.plugin = plugin;
        this.creator = creator;

        addButton(new GuiButton(ChatColor.GREEN + "Overworld")
                .setMaterial(Material.GRASS_BLOCK)
                .setLore(ChatColor.GRAY + "Set the world environment to overworld")
                .setSlot(2));
        addButton(new GuiButton(ChatColor.RED + "Nether")
                .setMaterial(Material.NETHERRACK)
                .setLore(ChatColor.GRAY + "Set the world environment to nether")
                .setSlot(4));
        addButton(new GuiButton(ChatColor.YELLOW + "End")
                .setMaterial(Material.END_STONE)
                .setLore(ChatColor.GRAY + "Set the world environment to end")
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
                creator.environment(World.Environment.NORMAL);
                break;
            }
            case 4: {
                creator.environment(World.Environment.NETHER);
                break;
            }
            case 6: {
                creator.environment(World.Environment.THE_END);
            }
        }

        plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiCreateWorld(plugin, creator));
    }

    @Override
    public GuiType getType() {
        return GuiType.FILLED;
    }

}
