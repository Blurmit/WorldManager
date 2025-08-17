package me.blurmit.worldmanager.gui.types;

import me.blurmit.worldmanager.WorldManagerPlugin;
import me.blurmit.worldmanager.gui.GuiButton;
import me.blurmit.worldmanager.gui.GuiMenu;
import me.blurmit.worldmanager.gui.GuiType;
import me.blurmit.worldmanager.world.EmptyChunkGenerator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GuiCreateWorld extends GuiMenu implements Listener {

    private final WorldManagerPlugin plugin;
    private final WorldCreator creator;
    private final Map<UUID, String> responsePlayers;

    public GuiCreateWorld(WorldManagerPlugin plugin, WorldCreator creator) {
        this.plugin = plugin;
        this.creator = creator;
        this.responsePlayers = new HashMap<>();

        addButton(new GuiButton(ChatColor.YELLOW + "Name: " + ChatColor.GRAY + creator.name())
                .setMaterial(Material.PAPER)
                .setLore(ChatColor.GRAY + "Sets the name of the world")
                .setSlot(11));
        addButton(new GuiButton(ChatColor.AQUA + "Seed: " + ChatColor.GRAY + creator.seed())
                .setMaterial(Material.MAP)
                .setLore(ChatColor.GRAY + "Sets the world seed")
                .setSlot(15));
        addButton(new GuiButton(ChatColor.GREEN + "Environment: " + ChatColor.GRAY + creator.environment().name())
                .setMaterial(creator.environment().equals(World.Environment.NORMAL) ? Material.GRASS_BLOCK : creator.environment().equals(World.Environment.THE_END) ? Material.END_STONE : Material.NETHERRACK)
                .setLore(ChatColor.GRAY + "Sets the world environment")
                .setSlot(28));
        addButton(new GuiButton(ChatColor.GOLD + "Type: " + ChatColor.GRAY + creator.type().name())
                .setMaterial(creator.type().equals(WorldType.NORMAL) ? Material.PODZOL : creator.type().equals(WorldType.FLAT) ? Material.DIRT : Material.MYCELIUM)
                .setLore(ChatColor.GRAY + "Sets the world type")
                .setSlot(30));
        addButton(new GuiButton(ChatColor.YELLOW + "Hardcore: " + ChatColor.GRAY + creator.hardcore())
                .setMaterial(creator.hardcore() ? Material.ENCHANTED_GOLDEN_APPLE : Material.GOLDEN_APPLE)
                .setLore(ChatColor.GRAY + "Sets if this world is hardcore")
                .setSlot(32));
        addButton(new GuiButton(ChatColor.AQUA + "Generate Structures: " + ChatColor.GRAY + creator.generateStructures())
                .setMaterial(creator.generateStructures() ? Material.REDSTONE_TORCH : Material.TORCH)
                .setLore(ChatColor.GRAY + "Click to toggle structure generation")
                .setSlot(34));
        addButton(new GuiButton(ChatColor.GREEN + "Create")
                .setMaterial(Material.LIME_STAINED_GLASS)
                .setLore(ChatColor.GRAY + "Click the start the world creation")
                .setSlot(48));
        addButton(new GuiButton(ChatColor.RED + "Back")
                .setMaterial(Material.BARRIER)
                .setLore(ChatColor.GRAY + "Return to main world creation menu")
                .setSlot(50));

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getName() {
        return ChatColor.DARK_GRAY + "Create World";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void callButton(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        switch (event.getRawSlot()) {
            case 11: {
                player.closeInventory();
                player.sendTitle(ChatColor.AQUA + "Name World", ChatColor.GRAY + "Type a world name in chat", 0, 30 * 20, 0);

                responsePlayers.put(player.getUniqueId(), "Name");
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> responsePlayers.remove(player.getUniqueId()), 20 * 30L);
                break;
            }
            case 15: {
                player.closeInventory();
                player.sendTitle(ChatColor.AQUA + "Set Seed", ChatColor.GRAY + "Type a world seed in chat", 0, 30 * 20, 0);

                responsePlayers.put(player.getUniqueId(), "Seed");
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> responsePlayers.remove(player.getUniqueId()), 20 * 30L);
                break;
            }
            case 28: {
                plugin.getGuiManager().displayMenu(player, new GuiWorldEnvironmentSelector(plugin, creator));
                break;
            }
            case 30: {
                plugin.getGuiManager().displayMenu(player, new GuiWorldTypeSelector(plugin, creator));
                break;
            }
            case 32: {
                creator.hardcore(!creator.hardcore());
                plugin.getGuiManager().displayMenu(player, new GuiCreateWorld(plugin, creator));
                break;
            }
            case 34: {
                creator.generateStructures(!creator.generateStructures());
                plugin.getGuiManager().displayMenu(player, new GuiCreateWorld(plugin, creator));
                break;
            }
            case 48: {
                if (!plugin.getWorldManager().doesWorldExist(creator.name())) {
                    player.closeInventory();
                    player.sendTitle(
                            ChatColor.AQUA + "Creating world...",
                            ChatColor.GRAY + "Please wait a few seconds",
                            0,
                            20 * 300,
                            0
                    );

                    World world = plugin.getWorldManager().createWorld(creator);
                    player.sendTitle("", "", 0, 0, 0);

                    if (creator.generator() instanceof EmptyChunkGenerator) {
                        Chunk chunk = world.getChunkAt(0, 0);
                        Block block = chunk.getBlock(0, 64, 0);
                        block.setType(Material.STONE);

                        Location blockLocation = block.getLocation();
                        blockLocation.setY(blockLocation.getY() + 1);
                        player.teleport(blockLocation);
                        return;
                    }

                    player.teleport(world.getSpawnLocation());
                }
                break;
            }
            case 50: {
                plugin.getGuiManager().displayMenu(player, new GuiMainMenu(plugin));
            }
        }
    }

    @Override
    public GuiType getType() {
        return GuiType.FILLED;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!responsePlayers.containsKey(event.getPlayer().getUniqueId())) {
            return;
        }

        String type = responsePlayers.get(event.getPlayer().getUniqueId());
        WorldCreator creator = null;

        if (type.equals("Name")) {
            creator = WorldCreator.name(event.getMessage()).copy(this.creator);
        }

        if (type.equals("Seed")) {
            try {
                creator = this.creator.seed(Long.parseLong(event.getMessage()));
            } catch (IllegalArgumentException e) {
                creator = this.creator.seed(event.getMessage().hashCode());
            }
        }

        WorldCreator finalCreator = creator;
        Bukkit.getScheduler().runTask(plugin, () -> {
            event.getPlayer().sendTitle("", "", 0, 0, 0);
            responsePlayers.remove(event.getPlayer().getUniqueId());
            plugin.getGuiManager().displayMenu(event.getPlayer(), new GuiCreateWorld(plugin, finalCreator));
        });

        event.setCancelled(true);
        HandlerList.unregisterAll(this);
    }

}
