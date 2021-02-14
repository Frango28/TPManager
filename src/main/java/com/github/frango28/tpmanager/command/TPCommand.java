package com.github.frango28.tpmanager.command;

import com.github.frango28.tpmanager.TPManagerBukkit;
import com.github.frango28.tpmanager.TeleportPoint;
import com.github.frango28.tpmanager.util.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class TPCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==3){
            Collection<? extends Player> target=getTarget(sender,args[1]);

            Optional<TeleportPoint> teleportPointOptional= TPManagerBukkit.getTPPointMng().getMap(args[2]);
            if(!teleportPointOptional.isPresent()){
                sender.sendMessage(Prefix.ERROR+"["+args[2]+"]は登録されていません");
                return true;
            }
            TeleportPoint tppoint= teleportPointOptional.get();
            if(!tppoint.isLoaded()){
                sender.sendMessage(Prefix.ERROR+"["+args[2]+"]はロードされていません");
                return true;
            }

            if(target.isEmpty()){
                sender.sendMessage(Prefix.ERROR+"TPするプレイヤーが見つかりませんでした");
                return true;
            }
            if(target.size()==1){
                target.forEach(p->{
                    if(!p.getName().equals(sender.getName())){
                        p.sendMessage(Prefix.SYSTEM+"["+tppoint.getName()+"]に"+p.getName()+"をTPさせました");
                    }
                    sender.sendMessage(Prefix.SYSTEM+"["+tppoint.getName()+"]に"+p.getName()+"をTPさせました");
                    p.teleport(tppoint.getLocation());
                });
            } else {
                String msg=Prefix.SYSTEM+"["+tppoint.getName()+"]に"+target.size()+"人のプレイヤーをTPさせました";
                target.forEach(p->{
                    if(!p.getName().equals(sender.getName())){
                        p.sendMessage(msg);
                    }
                    p.teleport(tppoint.getLocation());
                });
                sender.sendMessage(msg);
            }
            return true;

        }
        sender.sendMessage(Prefix.SYSTEM+"/tpm tp [TPするプレイヤー] [TPする地点]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list=new ArrayList<>();
        if(args.length==2){
            List<String> nameList= new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(p->nameList.add(p.getName()));
            nameList.addAll(Arrays.asList("@a","@r","@s"));
            StringUtil.copyPartialMatches(args[1],nameList, list);
            Collections.sort(list);
            return list;
        }
        if(args.length==3){
            StringUtil.copyPartialMatches(args[2], TPManagerBukkit.getTPPointMng().getNameList(), list);
            Collections.sort(list);
            return list;
        }
        return null;
    }

    private Collection<?extends Player> getTarget(CommandSender sender,String arg){
        if(arg.equalsIgnoreCase("@a")){
            return Bukkit.getOnlinePlayers();
        }
        if(arg.equalsIgnoreCase("@r")){
            List<Player> list=new ArrayList<>(Bukkit.getOnlinePlayers());
            return Collections.singletonList(list.get(new Random().nextInt(list.size())));
        }
        if(arg.equalsIgnoreCase("@s")){
            if(sender instanceof Player){
                return Collections.singletonList(((Player) sender));
            }
            return new ArrayList<>();
        }

        Player target=Bukkit.getPlayer(arg);
        if(target!=null&&target.isOnline()){
            if(target.getName().equalsIgnoreCase(arg)){
                return Collections.singletonList(target);
            }
        }
        return new ArrayList<>();
    }
}
