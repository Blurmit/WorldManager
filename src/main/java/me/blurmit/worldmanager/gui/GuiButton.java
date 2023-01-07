package me.blurmit.worldmanager.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GuiButton {

    private String name;
    private Material material;
    private List<String> lore;
    private int slot;
    private UUID uuid;

    public GuiButton(String name) {
        this.name = name;
        this.material = Material.STONE;
        this.lore = new ArrayList<>();
        this.slot = 0;
        this.uuid = UUID.randomUUID();
    }

    public GuiButton setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public Material getMaterial() {
        return this.material;
    }

    public GuiButton setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public GuiButton setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public int getSlot() {
        return this.slot;
    }

    public GuiButton setLore(String... lore) {
        this.lore = Arrays.asList(lore);
        return this;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public UUID getID() {
        return this.uuid;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
//        itemMeta.getPersistentDataContainer().set(NamespacedKey.fromString("button:" + uuid.toString()), PersistentDataType.BYTE, (byte) 0);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }


}
