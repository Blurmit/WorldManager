package me.blurmit.worldmanager.gui.types;

import me.blurmit.worldmanager.WorldManagerPlugin;
import me.blurmit.worldmanager.gui.GuiButton;
import me.blurmit.worldmanager.gui.GuiMenu;
import me.blurmit.worldmanager.gui.GuiType;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GuiGameruleSelector extends GuiMenu {

    private final WorldManagerPlugin plugin;
    private final World world;

    public GuiGameruleSelector(WorldManagerPlugin plugin, World world, long page) {
        this.plugin = plugin;
        this.world = world;

        setPage(page);

        Arrays.stream(GameRule.values())
                .skip(27L * getPage())
                .limit(28L * (getPage() + 1))
                .forEach(gamerule -> addButton(new GuiButton(ChatColor.AQUA + gamerule.getName() + ChatColor.GRAY + ": " + world.getGameRuleValue(gamerule))
                        .setLore(ChatColor.GRAY + "Left-click to increase value", ChatColor.GRAY + "Right-click to decrease value")
                        .setSlot(-1)
                        .setMaterial(Material.PAPER)));

        addButton(new GuiButton(ChatColor.RED + "Back")
                .setMaterial(Material.BARRIER)
                .setLore(ChatColor.GRAY + "Return to world edit menu")
                .setSlot(49));
    }

    @Override
    public String getName() {
        return "Select Gamerules";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());

        if (event.getRawSlot() == 45) {
            plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiGameruleSelector(plugin, world, getPage() - 1));
            return;
        }

        if (event.getRawSlot() == 49) {
            plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiEditWorld(plugin, world));
            return;
        }

        if (event.getRawSlot() == 53) {
            plugin.getGuiManager().displayMenu(event.getWhoClicked(), new GuiGameruleSelector(plugin, world, getPage() + 1));
            return;
        }

        ItemMeta meta = event.getCurrentItem().getItemMeta();
        String ruleName = name.split(": ")[0];
        String ruleValue = name.split(": ")[1];

        if (ruleValue.equals("true") || ruleValue.equals("false")) {
            String value = String.valueOf(!Boolean.parseBoolean(ruleValue));
            world.setGameRuleValue(ruleName, value);
            meta.setDisplayName(ChatColor.AQUA + ruleName + ChatColor.GRAY + ": " + value);
            event.getCurrentItem().setItemMeta(meta);
            return;
        }

        if (event.isLeftClick()) {
            String value = String.valueOf(Integer.parseInt(ruleValue) + 1);
            world.setGameRuleValue(ruleName, value);
            meta.setDisplayName(ChatColor.AQUA + ruleName + ChatColor.GRAY + ": " + value);
        }

        if (event.isRightClick()) {
            String value = String.valueOf(Integer.parseInt(ruleValue) - 1);
            world.setGameRuleValue(ruleName, value);
            meta.setDisplayName(ChatColor.AQUA + ruleName + ChatColor.GRAY + ": " + value);
        }

        event.getCurrentItem().setItemMeta(meta);
    }

    @Override
    public GuiType getType() {
        return GuiType.PAGED;
    }

}
