package me.blurmit.worldmanager.gui.types;

import me.blurmit.worldmanager.WorldManagerPlugin;
import me.blurmit.worldmanager.gui.GuiButton;
import me.blurmit.worldmanager.gui.GuiMenu;
import me.blurmit.worldmanager.gui.GuiType;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiDifficultySelector extends GuiMenu {

    private final WorldManagerPlugin plugin;
    private final World world;

    public GuiDifficultySelector(WorldManagerPlugin plugin, World world) {
        this.plugin = plugin;
        this.world = world;

        addButton(new GuiButton(ChatColor.GREEN + "Peaceful")
                .setMaterial(Material.LIME_BANNER)
                .setLore(ChatColor.GRAY + "Sets the world difficulty to peaceful")
                .setSlot(1));
        addButton(new GuiButton(ChatColor.DARK_GREEN + "Easy")
                .setMaterial(Material.GREEN_BANNER)
                .setLore(ChatColor.GRAY + "Sets the world difficulty to easy")
                .setSlot(3));
        addButton(new GuiButton(ChatColor.YELLOW + "Normal")
                .setMaterial(Material.YELLOW_BANNER)
                .setLore(ChatColor.GRAY + "Sets the world difficulty to normal")
                .setSlot(5));
        addButton(new GuiButton(ChatColor.RED + "Hard")
                .setMaterial(Material.RED_BANNER)
                .setLore(ChatColor.GRAY + "Sets the world difficulty to hard")
                .setSlot(7));
    }

    @Override
    public String getName() {
        return "Select Difficulty";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        switch (event.getRawSlot()) {
            case 1: {
                world.setDifficulty(Difficulty.PEACEFUL);
                break;
            }
            case 3: {
                world.setDifficulty(Difficulty.EASY);
                break;
            }
            case 5: {
                world.setDifficulty(Difficulty.NORMAL);
                break;
            }
            case 7: {
                world.setDifficulty(Difficulty.HARD);
            }
        }

        plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiEditWorld(plugin, world));
    }

    @Override
    public GuiType getType() {
        return GuiType.FILLED;
    }

}
