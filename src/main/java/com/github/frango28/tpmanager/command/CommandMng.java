package com.github.frango28.tpmanager.command;

import com.github.frango28.tpmanager.test.SelectorMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public final class CommandMng implements TabExecutor {
    private final Map<String, TabExecutor> subCommandArg = new HashMap<>();
    private final Map<String, String> subPermission = new HashMap<>();

    public CommandMng() {
//        コマンド実装
        addCommand(new ListCommand(), "list", "tpmanager.settppoint");
        addCommand(new TPPointAddCommand(), "set_tppoint", "tpmanager.set_tppoint");
        addCommand(new TPPointRemoveCommand(), "delete_tppoint", "tpmanager.delete_tppoint");
    }

    private void addCommand(TabExecutor _ext, String _arg, String _permission) {
        subCommandArg.put(_arg, _ext);
        subPermission.put(_arg, _permission);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                return openMenu(sender);
            }
        }
        if (args.length != 0) {
            String subArg = args[0];
            TabExecutor subExecutor = subCommandArg.get(subArg);
            if (subExecutor != null) {
                if (sender.hasPermission(subPermission.get(subArg))) {
                    return subExecutor.onCommand(sender, command, label, args);
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], subPermission.keySet(), list);
            Collections.sort(list);
            list.removeIf(arg -> !sender.hasPermission(subPermission.get(arg)));
        } else if (args.length > 1) {
            String subArg = args[0];
            if (!subPermission.containsKey(subArg)) return null;
            if (sender.hasPermission(this.subPermission.get(subArg)))
                return this.subCommandArg.get(subArg).onTabComplete(sender, command, alias, args);
        }
        return list;
    }

    private boolean openMenu(CommandSender sender) {
        Player p = (Player) sender;
        new SelectorMenu(p, 0);
        return true;
    }
}
