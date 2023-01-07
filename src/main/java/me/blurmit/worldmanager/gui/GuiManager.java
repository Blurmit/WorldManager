package me.blurmit.worldmanager.gui;

import me.blurmit.worldmanager.WorldManagerPlugin;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class GuiManager implements Listener {

    private final WorldManagerPlugin plugin;
    private final Map<UUID, GuiMenu> menus;

    public GuiManager(WorldManagerPlugin plugin) {
        this.plugin = plugin;
        this.menus = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void displayMenu(HumanEntity player, GuiMenu menu) {
        menus.remove(player.getUniqueId());

        Inventory inventory = menu.getInventory();

        player.openInventory(inventory);
        menu.setInventory(inventory);

        menus.put(player.getUniqueId(), menu);
    }

    public Set<Integer> getGuiBorder(Inventory menu) {
        Set<Integer> slots = new HashSet<>();
        AtomicInteger slot = new AtomicInteger(-1);

        IntStream.range(0, menu.getSize() / 9).forEach(row -> IntStream.range(0, 9).forEach(column -> {
            slot.incrementAndGet();

            if (row != 0 && row != ((menu.getSize() / 9) - 1) && column != 0 && column != 8) {
                return;
            }

            slots.add(slot.get());
        }));

        return slots;
    }

    public Map<UUID, GuiMenu> getMenus() {
        return menus;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isInventory(event)) {
            return;
        }

        event.setCancelled(true);
        callButton(event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!isInventory(event)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (isInventory(event.getPlayer().getInventory(), event.getPlayer().getUniqueId())) {
            menus.remove(event.getPlayer().getUniqueId());
        }
    }

    private void callButton(InventoryClickEvent event) {
        Set<GuiMenu> menusCopy = new HashSet<>(menus.values());

        menusCopy.stream().filter(menu -> menu.getInventory().equals(event.getInventory())).forEach(menu -> {
            for (GuiButton button : menu.getButtons()) {
                if (button.getSlot() == event.getRawSlot()) {
                    menu.callButton(event);
                }
            }
        });
    }

    private boolean isInventory(InventoryInteractEvent event) {
        if (!menus.containsKey(event.getWhoClicked().getUniqueId())) {
            return false;
        }

        return menus.get(event.getWhoClicked().getUniqueId()).getInventory().equals(event.getInventory());
    }

    private boolean isInventory(Inventory inventory, UUID clicker) {
        if (!menus.containsKey(clicker)) {
            return false;
        }

        return menus.get(clicker).getInventory().equals(inventory);
    }

}
