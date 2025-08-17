package me.blurmit.worldmanager.world;

import me.blurmit.worldmanager.WorldManagerPlugin;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;

public class WorldManager implements Listener {

    private final WorldManagerPlugin plugin;

    public WorldManager(WorldManagerPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    /**
     * Create and return a world using the provided creator.
     * Returns null if creation fails.
     */
    public World createWorld(WorldCreator worldCreator) {
        try {
            World world = Bukkit.createWorld(worldCreator);
            if (world == null) {
                plugin.getLogger().severe("Failed to create world: " + worldCreator.name());
            }
            return world;
        } catch (Exception ex) {
            plugin.getLogger().log(Level.SEVERE, "Exception while creating world: " + worldCreator.name(), ex);
            return null;
        }
    }

    /**
     * Delete a world directory after ensuring it is unloaded.
     * Will not (re)create a world just to delete it.
     */
    public void deleteWorld(String name) {
        if (name == null || name.isEmpty()) {
            plugin.getLogger().warning("deleteWorld called with empty name.");
            return;
        }

        // Unload if currently loaded
        World loaded = Bukkit.getWorld(name);
        if (loaded != null) {
            unloadWorld(loaded);
        }

        // Resolve the folder under the server's world container
        File container = Bukkit.getWorldContainer();
        if (container == null || !container.isDirectory()) {
            plugin.getLogger().severe("World container not available; cannot delete " + name);
            return;
        }

        File worldFolder = new File(container, name);
        if (!worldFolder.exists()) {
            plugin.getLogger().info("World '" + name + "' does not exist on disk; nothing to delete.");
            return;
        }

        // Best-effort delete with clear logging
        try {
            // Apache Commons IO is fine; Files.walk could also be used.
            FileUtils.deleteDirectory(worldFolder);
            plugin.getLogger().info("Deleted world folder: " + name);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to delete world folder for " + name + ". Is it locked by another process?", e);
        }
    }

    /**
     * Unload by World reference (safer). Saves chunks by default.
     */
    public void unloadWorld(World world) {
        if (world == null) {
            return;
        }

        // Choose a safe destination world that is not the one we are unloading.
        World fallback = Bukkit.getWorlds().stream()
                .filter(w -> !w.equals(world))
                .findFirst()
                .orElse(null);

        if (fallback == null) {
            plugin.getLogger().severe("No fallback world available to evacuate players before unloading " + world.getName());
            return;
        }

        // Defensive copy to avoid concurrent modification if plugins move players during teleport
        List<Player> players = new ArrayList<>(world.getPlayers());
        players.forEach(p -> p.teleport(fallback.getSpawnLocation()));

        boolean ok = Bukkit.unloadWorld(world, true);
        if (!ok) {
            plugin.getLogger().severe("Bukkit.unloadWorld returned false for " + world.getName());
        } else {
            plugin.getLogger().info("Unloaded world: " + world.getName());
        }
    }

    /**
     * Convenience: unload by name.
     */
    public void unloadWorld(String name) {
        World world = Bukkit.getWorld(name);
        if (world != null) {
            unloadWorld(world);
        }
    }

    /**
     * Enumerate world folders under the container that look like valid worlds (contain level.dat).
     */
    public Set<File> getWorlds() {
        Set<File> worlds = new HashSet<>();

        File container = Bukkit.getWorldContainer();
        if (container == null || !container.isDirectory()) {
            return worlds;
        }

        File[] entries = container.listFiles();
        if (entries == null) {
            return worlds;
        }

        for (File worldDir : entries) {
            if (!worldDir.isDirectory()) {
                continue;
            }

            File[] child = worldDir.listFiles();
            if (child == null) {
                continue;
            }

            boolean hasLevelDat = Arrays.stream(child)
                    .anyMatch(f -> f != null && "level.dat".equalsIgnoreCase(f.getName()));
            if (hasLevelDat) {
                worlds.add(worldDir);
            }
        }

        return worlds;
    }

    /**
     * True if a world folder with the given name exists under the container.
     */
    public boolean doesWorldExist(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        return getWorlds().stream().anyMatch(world -> world.getName().equalsIgnoreCase(name));
    }

    /**
     * Load all world folders that aren't already loaded.
     */
    private void load() {
        plugin.getLogger().info("Loading worlds...");

        // Names of already loaded worlds
        Set<String> loadedNames = new HashSet<>();
        for (World w : Bukkit.getWorlds()) {
            loadedNames.add(w.getName().toLowerCase(Locale.ROOT));
        }

        for (File worldDir : getWorlds()) {
            String name = worldDir.getName();
            if (loadedNames.contains(name.toLowerCase(Locale.ROOT))) {
                continue; // skip already loaded
            }

            try {
                Bukkit.createWorld(new WorldCreator(name));
                plugin.getLogger().info("Loaded world: " + name);
            } catch (Exception ex) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load world: " + name, ex);
            }
        }

        plugin.getLogger().info("Total loaded worlds: " + Bukkit.getWorlds().size());
    }

}
