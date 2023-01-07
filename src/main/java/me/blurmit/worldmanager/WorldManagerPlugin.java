package me.blurmit.worldmanager;

import me.blurmit.worldmanager.commands.WorldCommand;
import me.blurmit.worldmanager.gui.GuiManager;
import me.blurmit.worldmanager.worlds.WorldManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class WorldManagerPlugin extends JavaPlugin {

    private WorldManager worldManager;
    private GuiManager guiManager;
    private boolean isPaper;

    @Override
    public void onEnable() {
        this.worldManager = new WorldManager(this);
        this.guiManager = new GuiManager(this);
        this.isPaper = false;

        checkPaper();

        getCommand("worlds").setExecutor(new WorldCommand(this));

        getLogger().info("WorldManager has been successfully enabled.");
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public boolean isPaper() {
        return isPaper;
    }

    private void checkPaper() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            isPaper = true;
        } catch (ClassNotFoundException ignored) {}
    }

}
