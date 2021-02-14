package com.github.frango28.tpmanager.command;

import com.github.frango28.tpmanager.TPManagerBukkit;
import com.github.frango28.tpmanager.TPPointManager;
import com.github.frango28.tpmanager.TeleportPoint;
import com.github.frango28.tpmanager.util.Prefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class LobbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            TPPointManager mng=TPManagerBukkit.getTPPointMng();

            Optional<TeleportPoint> point=mng.getMap("lobby");
            if(!point.isPresent()){
                sender.sendMessage(Prefix.ERROR+"ロビーが設定されていません");
            }else {
                point.ifPresent(p->{
                    if(p.isLoaded()){
                        ((Player) sender).teleport(p.getLocation());
                        sender.sendMessage(Prefix.SYSTEM+"[lobby]にテレポートしました");
                    }else {
                        sender.sendMessage(Prefix.ERROR+"[lobby]はロードされていません");
                    }
                });
            }
            return true;
        }
        sender.sendMessage(Prefix.ERROR+"このコマンドはプレイヤーのみ使用できます");
        return true;
    }
}
