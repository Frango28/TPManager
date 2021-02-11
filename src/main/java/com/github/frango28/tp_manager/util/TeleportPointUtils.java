package com.github.frango28.tp_manager.util;

import com.github.frango28.tp_manager.TeleportPoint;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TeleportPointUtils {
    public static List<String> materialList=new ArrayList<>();
    static {
        Arrays.stream(Material.values()).forEach(material -> materialList.add(material.toString()));
    }

    public static ItemStack getIconItemStack(TeleportPoint p){
        ItemStack item = new ItemStack(p.getIcon());

        ItemMeta meta = item.getItemMeta();


        if(p.isLoaded()){
            Objects.requireNonNull(meta).setDisplayName(ChatColor.YELLOW + p.getName());

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + Objects.requireNonNull(p.getLocation().getWorld()).getName());
            lore.add(ChatColor.GRAY + p.getXYZ());
            meta.setLore(lore);
        }else{
            Objects.requireNonNull(meta).setDisplayName(ChatColor.RED + p.getName());

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "unknown");
            lore.add(ChatColor.GRAY + p.getXYZ());
            meta.setLore(lore);
        }


        meta.addItemFlags(ItemFlag.values());

        item.setItemMeta(meta);
        return item;
    }

    public static List<String> getMaterialList() {
        return materialList;
    }
}
