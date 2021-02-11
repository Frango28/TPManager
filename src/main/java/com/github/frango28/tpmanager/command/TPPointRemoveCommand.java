package com.github.frango28.tpmanager.command;

import com.github.frango28.tpmanager.TPManagerBukkit;
import com.github.frango28.tpmanager.TPPointManager;
import com.github.frango28.tpmanager.util.Prefix;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TPPointRemoveCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        TPPointManager mng = TPManagerBukkit.getTPPointMng();

        if (args.length == 2) {
            String name = args[1];
            if (mng.hasData(name)) {
                mng.removeMap(name);
                sender.sendMessage(Prefix.SYSTEM + "[" + name + "]を消去しました");
            } else {
                sender.sendMessage(Prefix.SYSTEM + ChatColor.RED.toString() + "[" + name + "]は登録されていません");
            }
            return true;
        }
        sender.sendMessage(Prefix.ERROR + "/tpm delete_tppoint [TPPointName]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], TPManagerBukkit.getTPPointMng().getNameList(), list);
            Collections.sort(list);
        }
        return list;
    }
}
