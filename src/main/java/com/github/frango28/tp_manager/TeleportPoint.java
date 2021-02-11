package com.github.frango28.tp_manager;

import com.sun.istack.internal.NotNull;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TeleportPoint implements Comparable<TeleportPoint> {
    private final String name;
    private Material icon;
    private Location loc;

//    WorldName/x/y/z/pitch/yaw/Material
    public TeleportPoint(@NotNull String name,@NotNull String locData){
        this.name=name;
        String[] args=locData.split("/");

        double x=0;
        double y=0;
        double z=0;
        String worldName="none";
        float pitch=0;
        float yaw=0;

        switch (args.length){
            default:
            case 7:
                Material icon=Material.getMaterial(args[6],true);
                if(icon!=null)this.icon=icon;
            case 6:
                try {
                    yaw=Float.parseFloat(args[5]);
//                    defaultの値が設定されているため省略
                }catch (NumberFormatException ex){}
            case 5:
                try {
                    pitch=Float.parseFloat(args[4]);
//                    defaultの値が設定されているため省略
                }catch (NumberFormatException ex){}
            case 4:
                try {
                    z=Double.parseDouble(args[3]);
//                    defaultの値が設定されているため省略
                }catch (NumberFormatException ex){}
            case 3:
                try {
                    y=Double.parseDouble(args[2]);
//                    defaultの値が設定されているため省略
                }catch (NumberFormatException ex){}
            case 2:
                try {
                    x=Double.parseDouble(args[1]);
//                    defaultの値が設定されているため省略
                }catch (NumberFormatException ex){}
            case 1:
                worldName=args[0];
            case 0:
        }
        this.loc=new Location(Bukkit.getWorld(worldName),x,y,z,yaw,pitch);
    }

    //    コントラクタ
    public TeleportPoint(@NotNull String name, @NotNull Location loc) {
        this.name = name;
        this.loc = loc;
        this.icon = Material.FILLED_MAP;
    }

    public TeleportPoint(@NotNull String name, @NotNull Location loc, @NotNull Material icon) {
        this.name = name;
        setLoc(loc);
        setIcon(icon);
    }

    //    データを返すメソッド
    public String getName() {
        return this.name;
    }

    //    データをセット
    public Location getLocation() {
        if(!this.loc.isWorldLoaded())return null;
        return this.loc;
    }

    public Material getIcon() {
        if (this.icon == null || this.icon == Material.AIR) {
            this.icon = Material.FILLED_MAP;
        }
        return this.icon;
    }

    public void setIcon(Material icon) {
        if(icon == null || icon == Material.AIR){
            this.icon=Material.FILLED_MAP;
            return;
        }
        this.icon = icon;
    }

    public String getXYZ() {
        return loc.getBlockX() + "," + loc.getBlockX() + "," + loc.getBlockZ();
    }

    public String getWorldName() {
        World w=this.loc.getWorld();
        if(w==null)return "null";
        return w.getName();
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public boolean isLoaded(){
        return this.loc.isWorldLoaded();
    }

    //        ハッシュコード返すメソッド
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    //    等価判定メソッド
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof TeleportPoint)) return false;
        TeleportPoint mapData = (TeleportPoint) obj;
        return mapData.getName().equals(this.name);
    }

    //    Stringを返す
    @Override
    public String toString() {
        return getWorldName()
                +"/"+loc.getX()
                +"/"+loc.getY()
                +"/"+loc.getZ()
                +"/"+loc.getPitch()
                +"/"+loc.getYaw()
                +"/"+this.getIcon().toString();
    }

    //    大小を返すメソッド
    @Override
    public int compareTo(TeleportPoint o) {
        return o.getName().compareTo(this.name);
    }
}
