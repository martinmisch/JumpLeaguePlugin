package de.martin.jumpleaguegym.commands;

import de.martin.jumpleaguegym.game.ModulSchwierigkeit;
import de.martin.jumpleaguegym.main.Main;
import de.martin.jumpleaguegym.utils.ChestItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChestItemsCommand implements CommandExecutor {
    private ChestItemManager chM;

    public ChestItemsCommand() {
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        } else if (!sender.hasPermission("lol")) {
            return false;
        } else {
            Player p = (Player)sender;
            if (args.length == 1) {
                ModulSchwierigkeit ms = ModulSchwierigkeit.valueOf(args[0].toUpperCase());
                Main.getPlugin().getGame().getChI().openInv(p, ms);
            } else {
                p.sendMessage("§e[JLG] §f/items [leicht / mittel / schwer]");
            }

            return true;
        }
    }
}
