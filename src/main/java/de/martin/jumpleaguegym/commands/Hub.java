package de.martin.jumpleaguegym.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.martin.jumpleaguegym.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Hub implements CommandExecutor {
    public Hub() {
    }

    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("Lobby");
            p.sendPluginMessage(Main.getPlugin(), "BungeeCord", out.toByteArray());
        }
        return false;
    }
}
