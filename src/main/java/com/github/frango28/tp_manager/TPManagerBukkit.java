package com.github.frango28.tp_manager;

import com.github.frango28.tp_manager.command.CommandMng;
import org.bukkit.plugin.java.JavaPlugin;

public final class TPManagerBukkit extends JavaPlugin {
    private static TPManagerBukkit plugin;
    private TPPointManager mng;

    @Override
    public void onEnable() {
//        インスタンスなどの初期化
        plugin=this;
        mng=new TPPointManager();

        getCommand("tpmanager").setExecutor(new CommandMng());

        for(TeleportPoint p:mng.getMapList()){
            getLogger().info(p.getName()+"=="+p.toString());
        }
    }

    @Override
    public void onDisable() {
        mng.save();
    }

    public static TPManagerBukkit instance(){
        return plugin;
    }

    public static TPPointManager getTPPointMng(){
        return instance().mng;
    }
}
