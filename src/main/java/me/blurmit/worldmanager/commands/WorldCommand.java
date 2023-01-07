package me.blurmit.worldmanager.commands;

import me.blurmit.worldmanager.WorldManagerPlugin;
import me.blurmit.worldmanager.gui.types.GuiMainMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCommand implements CommandExecutor {

    private final WorldManagerPlugin plugin;

    public WorldCommand(WorldManagerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "[!] Only players can execute this command.");
            return true;
        }

        if (!sender.hasPermission("worldmanager.admin")) {
            sender.sendMessage(ChatColor.RED + "✖ " + ChatColor.GRAY + "You do not have permission to execute this command.");
            return true;
        }

        plugin.getGuiManager().displayMenu((Player) sender, new GuiMainMenu(plugin));
        return true;
    }

}
