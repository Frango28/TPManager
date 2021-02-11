package com.github.frango28.tpmanager;

import com.github.frango28.tpmanager.util.ConfigLoader;
import com.sun.istack.internal.NotNull;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class TPPointManager {
    private final String FILE_NAME = "location.yml";
    private final TPManagerBukkit plugin;
    private final Set<TeleportPoint> tpPointSet = new HashSet<>();
    private ConfigLoader loader;

    public TPPointManager() {
        this.plugin = TPManagerBukkit.instance();

        this.load();
    }

    //    コンフィグをロード
    private void load() {
        this.tpPointSet.clear();

        this.loader = new ConfigLoader(this.plugin, this.FILE_NAME);
        this.loader.saveDefaultConfig();
        this.loader.reloadConfig();
        FileConfiguration c = this.loader.getConfig();

        for (String name : c.getKeys(false)) {
            tpPointSet.add(new TeleportPoint(name, c.getString(name, "world/")));
        }
    }

    //    コンフィグをセーブ
    public void save() {
        this.loader.saveDefaultConfig();
        this.loader.reloadConfig();
        FileConfiguration c = this.loader.getConfig();

        c.getKeys(false).forEach(key -> c.set(key, null));

        for (TeleportPoint point : tpPointSet) {
            c.set(point.getName(), point.toString());
        }

        this.loader.saveConfig();
    }


    //    マップを追加
    public void addMap(@NotNull TeleportPoint point) {
        if (point == null) {
            throw new NullPointerException();
        }
        if (hasData(point.getName())) {
            throw new IllegalArgumentException();
        }

        this.tpPointSet.add(point);
    }

    //    マップを取得
    public Optional<TeleportPoint> getMap(String name) {
        if (name == null || name.isEmpty()) return Optional.empty();
        return this.tpPointSet.stream().filter(map -> map.getName().equals(name)).findFirst();
    }

    @NotNull
    public List<TeleportPoint> getMapList() {
        List<TeleportPoint> list = new ArrayList<>(tpPointSet);
        list.sort(Collections.reverseOrder());
        return list;
    }

    public List<String> getNameList() {
        List<String> list = new ArrayList<>();
        this.tpPointSet.forEach(p -> list.add(p.getName()));
        list.sort(Collections.reverseOrder());
        return list;
    }

    //    マップを消去
    public void removeMap(String name) {
        if (name == null || name.isEmpty()) return;

        Optional<TeleportPoint> tp = this.tpPointSet.stream().filter(map -> map.getName().equals(name)).findFirst();
        tp.ifPresent(this.tpPointSet::remove);
    }

    public boolean hasData(String name) {
        if (name == null || name.isEmpty()) return false;
        return getMap(name).isPresent();
    }
}
