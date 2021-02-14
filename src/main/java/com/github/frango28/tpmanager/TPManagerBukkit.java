package com.github.frango28.tpmanager;

import com.github.frango28.tpmanager.command.CommandMng;
import com.github.frango28.tpmanager.command.LobbyCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TPManagerBukkit extends JavaPlugin {
    private static TPManagerBukkit plugin;
    private TPPointManager mng;

    public static TPManagerBukkit instance() {
        return plugin;
    }

    public static TPPointManager getTPPointMng() {
        return instance().mng;
    }

    @Override
    public void onEnable() {
//        インスタンスなどの初期化
        plugin = this;
        mng = new TPPointManager();

        Objects.requireNonNull(getCommand("tpmanager")).setExecutor(new CommandMng());
        Objects.requireNonNull(getCommand("lobby")).setExecutor(new LobbyCommand());

//        for (TeleportPoint p : mng.getMapList()) {
//            getLogger().info(p.getName() + "==" + p.toString());
//        }
    }

    @Override
    public void onDisable() {
        mng.save();
    }
}
