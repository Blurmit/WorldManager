package me.blurmit.worldmanager.gui.types;

import me.blurmit.worldmanager.WorldManagerPlugin;
import me.blurmit.worldmanager.gui.GuiButton;
import me.blurmit.worldmanager.gui.GuiMenu;
import me.blurmit.worldmanager.gui.GuiType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.File;
import java.util.Comparator;

public class GuiWorldList extends GuiMenu {

    private final WorldManagerPlugin plugin;

    public GuiWorldList(WorldManagerPlugin plugin, long page) {
        this.plugin = plugin;

        setPage(page);

        plugin.getWorldManager().getWorlds().stream()
                .skip(27L * getPage())
                .limit(28L * (getPage() + 1))
                .sorted(Comparator.comparing(File::getTotalSpace))
                .forEach(world -> addButton(new GuiButton(ChatColor.GREEN + world.getName())
                .setMaterial(Material.GRASS_BLOCK)
                .setLore(ChatColor.YELLOW + "Left-click to teleport", ChatColor.YELLOW + "Right-click to edit", ChatColor.RED + "Shift-click to delete")
                .setSlot(-1)));

        addButton(new GuiButton(ChatColor.RED + "Back")
                .setMaterial(Material.BARRIER)
                .setLore(ChatColor.GRAY + "Return to main world creation menu")
                .setSlot(49));
    }

    @Override
    public String getName() {
        return ChatColor.DARK_GRAY + "Created Worlds";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());

        if (event.getRawSlot() == 45) {
            plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiWorldList(plugin, getPage() - 1));
            return;
        }

        if (event.getRawSlot() == 49) {
            plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiMainMenu(plugin));
            return;
        }

        if (event.getRawSlot() == 53) {
            plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiWorldList(plugin, getPage() + 1));
            return;
        }

        if (event.getClick().isLeftClick() && !event.getClick().isShiftClick()) {
            if (plugin.getServer().getWorld(name) == null) {
                plugin.getServer().createWorld(new WorldCreator(name));
            }

            event.getWhoClicked().teleport(plugin.getServer().getWorld(name).getSpawnLocation());
            return;
        }

        if (event.getClick().isRightClick() && !event.getClick().isShiftClick()) {
            plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiEditWorld(plugin,
                    plugin.getServer().getWorld(name) != null ? plugin.getServer().getWorld(name) : plugin.getServer().createWorld(new WorldCreator(name))));
            return;
        }

        if (event.getClick().isShiftClick()) {
            plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiWorldDeleteConfirm(plugin, name));
        }
    }

    @Override
    public GuiType getType() {
        return GuiType.PAGED;
    }

}
