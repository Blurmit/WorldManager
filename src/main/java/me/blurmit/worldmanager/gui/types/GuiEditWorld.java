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

public class GuiEditWorld extends GuiMenu {

    private final WorldManagerPlugin plugin;
    private final World world;

    public GuiEditWorld(WorldManagerPlugin plugin, World world) {
        this.plugin = plugin;
        this.world = world;

        addButton(new GuiButton(ChatColor.YELLOW + "Difficulty: " + ChatColor.GRAY + world.getDifficulty().name())
                .setMaterial(world.getDifficulty().equals(Difficulty.PEACEFUL) ? Material.LIME_BANNER : world.getDifficulty().equals(Difficulty.EASY) ? Material.GREEN_BANNER : world.getDifficulty().equals(Difficulty.NORMAL) ? Material.YELLOW_BANNER : Material.RED_BANNER)
                .setLore(ChatColor.GRAY + "Sets the world difficulty")
                .setSlot(11));
        addButton(new GuiButton(ChatColor.BLUE + "Gamerules")
                .setMaterial(Material.COMMAND_BLOCK)
                .setLore(ChatColor.GRAY + "Manage the world gamerules")
                .setSlot(15));
        addButton(new GuiButton(ChatColor.GOLD + "Allow Animals: " + ChatColor.GRAY + world.getAllowAnimals())
                .setMaterial(Material.PIG_SPAWN_EGG)
                .setLore(ChatColor.GRAY + "Toggles animal spawning in the world")
                .setSlot(28));
        addButton(new GuiButton(ChatColor.GREEN + "Allow Monsters: " + ChatColor.GRAY + world.getAllowMonsters())
                .setMaterial(Material.ZOMBIE_SPAWN_EGG)
                .setLore(ChatColor.GRAY + "Toggles monsters spawning in the world")
                .setSlot(30));
        addButton(new GuiButton(ChatColor.YELLOW + "Hardcore: " + ChatColor.GRAY + world.isHardcore())
                .setMaterial(world.isHardcore() ? Material.ENCHANTED_GOLDEN_APPLE : Material.GOLDEN_APPLE)
                .setLore(ChatColor.GRAY + "Sets if this world is hardcore")
                .setSlot(32));
        addButton(new GuiButton(ChatColor.RED + "Allow PvP: " + ChatColor.GRAY + world.getPVP())
                .setMaterial(world.getPVP() ? Material.DIAMOND_SWORD : Material.WOODEN_SWORD)
                .setLore(ChatColor.GRAY + "Toggles PvP in the world")
                .setSlot(34));
        addButton(new GuiButton(ChatColor.RED + "Back")
                .setMaterial(Material.BARRIER)
                .setLore(ChatColor.GRAY + "Return to world list menu")
                .setSlot(49));
    }

    @Override
    public String getName() {
        return ChatColor.DARK_GRAY + "Editing " + world.getName();
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        switch (event.getRawSlot()) {
            case 11: {
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiDifficultySelector(plugin, world));
                break;
            }
            case 15: {
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiGameruleSelector(plugin, world, 0));
                break;
            }
            case 28: {
                world.setSpawnFlags(world.getAllowMonsters(), !world.getAllowAnimals());
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiEditWorld(plugin, world));
                break;
            }
            case 30: {
                world.setSpawnFlags(!world.getAllowMonsters(), world.getAllowAnimals());
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiEditWorld(plugin, world));
                break;
            }
            case 32: {
                world.setHardcore(!world.isHardcore());
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiEditWorld(plugin, world));
                break;
            }
            case 34: {
                world.setPVP(!world.getPVP());
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiEditWorld(plugin, world));
                break;
            }
            case 49: {
                plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiWorldList(plugin, 0));
            }
        }
    }

    @Override
    public GuiType getType() {
        return GuiType.FILLED;
    }

}
