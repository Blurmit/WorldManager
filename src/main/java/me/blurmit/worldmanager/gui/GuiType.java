package me.blurmit.worldmanager.gui;

import me.blurmit.worldmanager.WorldManagerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public enum GuiType {

    FILLED {
        @Override
        public Inventory getInventory(GuiMenu menu) {
            Inventory gui = Bukkit.createInventory(null, menu.getSlots(), menu.getName());

            for (int i = 0; i < menu.getSlots(); i++) {
                gui.setItem(i, fillerItem);
            }

            for (GuiButton button : menu.getButtons()) {
                gui.setItem(button.getSlot(), button.build());
            }

            return gui;
        }
    },
    PAGED {
        @Override
        public Inventory getInventory(GuiMenu menu) {
            Inventory gui = Bukkit.createInventory(null, menu.getSlots(), menu.getName());

            plugin.getGuiManager().getGuiBorder(gui).forEach(slot -> gui.setItem(slot, fillerItem));

            for (GuiButton button : menu.getButtons()) {
                if (button.getSlot() == -1) {
                    gui.addItem(button.build());
                    button.setSlot(gui.first(button.build()));
                    continue;
                }

                gui.setItem(button.getSlot(), button.build());
            }

            if (menu.isFull(gui)) {
                menu.addButton(nextPageItem);
                gui.setItem(nextPageItem.getSlot(), nextPageItem.build());
            }

            if (menu.getPage() != 0) {
                menu.addButton(previousPageItem);
                gui.setItem(previousPageItem.getSlot(), previousPageItem.build());
            }

            return gui;
        }
    };

    final WorldManagerPlugin plugin;
    final GuiButton nextPageItem;
    final GuiButton previousPageItem;
    final ItemStack fillerItem;

    GuiType() {
        plugin = JavaPlugin.getPlugin(WorldManagerPlugin.class);

       fillerItem = new GuiButton(ChatColor.RESET + "")
               .setMaterial(Material.GRAY_STAINED_GLASS_PANE)
               .build();

       nextPageItem = new GuiButton(ChatColor.GREEN + "Next Page")
               .setMaterial(Material.ARROW)
               .setLore(ChatColor.GRAY + "Click to go to the next page.")
               .setSlot(53);

       previousPageItem = new GuiButton(ChatColor.GREEN + "Previous Page")
               .setMaterial(Material.ARROW)
               .setLore(ChatColor.GRAY + "Click to go to the previous page.")
               .setSlot(45);
    }

    public abstract Inventory getInventory(GuiMenu menu);

}
