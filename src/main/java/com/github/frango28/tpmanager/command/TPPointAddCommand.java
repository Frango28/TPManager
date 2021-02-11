package com.github.frango28.tpmanager.command;

import com.github.frango28.tpmanager.TPManagerBukkit;
import com.github.frango28.tpmanager.TPPointManager;
import com.github.frango28.tpmanager.TeleportPoint;
import com.github.frango28.tpmanager.util.Prefix;
import com.github.frango28.tpmanager.util.TeleportPointUtils;
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

        if (args.length == 2) {
            if (sender instanceof Player) {
                setTPPoint((Player) sender, args[1], ((Player) sender).getLocation(), Material.FILLED_MAP);
                return true;
            }
            sender.sendMessage(Prefix.SYSTEM + ChatColor.RED.toString() + "プレイヤーのみこのコマンドは使用できます");
        }
        if (args.length == 3) {
            if (sender instanceof Player) {
                Material icon;
                if (args[2].equals("hand")) {
                    icon = ((Player) sender).getInventory().getItemInMainHand().getType();
                } else {
                    icon = Material.getMaterial(args[2]);
                }
                setTPPoint((Player) sender, args[1], ((Player) sender).getLocation(), icon);
                return true;
            }
            sender.sendMessage(Prefix.SYSTEM + ChatColor.RED.toString() + "プレイヤーのみこのコマンドは使用できます");
        }
        sender.sendMessage(Prefix.ERROR + "/tpm set_tppoint [新しいTPPointの名前] [アイコンにするアイテム名]");
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

    private void setTPPoint(Player p, String name, Location loc, Material icon) {
        TPPointManager mng = TPManagerBukkit.getTPPointMng();
        if (mng.hasData(name)) {
            p.sendMessage(Prefix.SYSTEM + ChatColor.RED.toString() + "既に[" + name + "]という名前で座標が登録されています");
        } else {
            mng.addMap(new TeleportPoint(name, loc, icon));
            p.sendMessage(Prefix.SYSTEM + "[" + name + "]を登録しました");
        }
    }
}
