package com.github.frango28.tpmanager.command;

import com.github.frango28.tpmanager.TPManagerBukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class ListCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        TPManagerBukkit.getTPPointMng().getMapList().forEach(point -> sender.sendMessage(point.getName() + ":" + point.toString()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
