package me.blurmit.worldmanager.worlds;

import me.blurmit.worldmanager.WorldManagerPlugin;
import org.apache.commons.io.FileUtils;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class WorldManager implements Listener {

    private final WorldManagerPlugin plugin;

    public WorldManager(WorldManagerPlugin plugin) {
        this.plugin = plugin;

        load();
    }

    public World createWorld(WorldCreator worldCreator) {
        World world = plugin.getServer().createWorld(worldCreator);
        world.save();

        return world;
    }

    public void deleteWorld(String name) {
        try {
            if (plugin.getServer().getWorld(name) == null) {
                plugin.getServer().createWorld(new WorldCreator(name));
            }

            File file = plugin.getServer().getWorld(name).getWorldFolder();
            unloadWorld(name);

            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "An error occurred whilst attempting to delete " + name, e);
        }
    }

    public void unloadWorld(String name) {
        if (plugin.getServer().getWorld(name) == null) {
            return;
        }

        plugin.getServer().getWorld(name).getPlayers().forEach(player -> player.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation()));
        plugin.getServer().unloadWorld(name, true);
    }

    public Set<File> getWorlds() {
        Set<File> worlds = new HashSet<>();

        for (File worldFile : plugin.getServer().getWorldContainer().listFiles()) {
            if (!worldFile.isDirectory()) {
                continue;
            }

            if (Arrays.stream(worldFile.listFiles()).map(File::getName).anyMatch(name -> name.equals("level.dat"))) {
                worlds.add(worldFile);
            }
        }

        return worlds;
    }

    public boolean doesWorldExist(String name) {
        return getWorlds().stream().filter(world -> world.getName().equals(name)).findAny().orElse(null) == null;
    }

    private void load() {
        plugin.getLogger().info("Loading worlds...");

        getWorlds().forEach(world -> plugin.getServer().createWorld(new WorldCreator(world.getName())));

        plugin.getLogger().info("Loaded " + plugin.getServer().getWorlds().size() + " worlds.");
    }

}
