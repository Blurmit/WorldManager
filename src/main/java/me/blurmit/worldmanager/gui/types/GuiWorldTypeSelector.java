package me.blurmit.worldmanager.gui.types;

import me.blurmit.worldmanager.WorldManagerPlugin;
import me.blurmit.worldmanager.gui.GuiButton;
import me.blurmit.worldmanager.gui.GuiMenu;
import me.blurmit.worldmanager.gui.GuiType;
import me.blurmit.worldmanager.world.EmptyChunkGenerator;
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

        addButton(new GuiButton(ChatColor.RED + "Void")
                .setMaterial(Material.PODZOL)
                .setLore(ChatColor.GRAY + "A completely empty Minecraft world")
                .setSlot(1));
        addButton(new GuiButton(ChatColor.GREEN + "Normal")
                .setMaterial(Material.PODZOL)
                .setLore(ChatColor.GRAY + "A normally generated Minecraft world")
                .setSlot(3));
        addButton(new GuiButton(ChatColor.GOLD + "Flat")
                .setMaterial(Material.DIRT)
                .setLore(ChatColor.GRAY + "A flat Minecraft world")
                .setSlot(5));
        addButton(new GuiButton(ChatColor.YELLOW + "Amplified")
                .setMaterial(Material.MYCELIUM)
                .setLore(ChatColor.GRAY + "A Minecraft world with amplified terrain")
                .setSlot(7));
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
            case 1: {
                creator.generator(new EmptyChunkGenerator());
                break;
            }
            case 3: {
                creator.type(WorldType.NORMAL);
                break;
            }
            case 5: {
                creator.type(WorldType.FLAT);
                break;
            }
            case 7: {
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
