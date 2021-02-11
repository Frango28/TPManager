package com.github.frango28.tp_manager.command;

import com.github.frango28.tp_manager.TPManagerBukkit;
import com.github.frango28.tp_manager.TPPointManager;
import com.github.frango28.tp_manager.TeleportPoint;
import com.github.frango28.tp_manager.util.Prefix;
import com.github.frango28.tp_manager.util.TeleportPointUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TPPointAddCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        TPPointManager mng= TPManagerBukkit.getTPPointMng();
        if(args.length==3){
            if(sender instanceof Player){
                String name=args[1];
                Location loc=((Player) sender).getLocation();
                Material icon;
                if(args[2].equals("hand")){
                    icon=((Player) sender).getInventory().getItemInMainHand().getType();
                }else {
                    icon=Material.getMaterial(args[2]);
                }


                if(mng.hasData(name)){
                    sender.sendMessage(Prefix.SYSTEM+ChatColor.RED.toString()+"既に["+name+"]という名前で座標が登録されています");
                }else {
                    mng.addMap(new TeleportPoint(name,loc,icon));
                    sender.sendMessage(Prefix.SYSTEM+"["+name+"]を登録しました");
                }

                return true;
            }
            sender.sendMessage(Prefix.SYSTEM+ ChatColor.RED.toString()+"プレイヤーのみこのコマンドは使用できます");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 3) {
            StringUtil.copyPartialMatches(args[2], TeleportPointUtils.getMaterialList(), list);
            list.add("hand");
            Collections.sort(list);
        }
        return list;
    }
}
