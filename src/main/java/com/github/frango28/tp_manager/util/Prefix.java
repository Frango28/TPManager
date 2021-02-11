package com.github.frango28.tp_manager.util;

import org.bukkit.ChatColor;

public enum Prefix {
    SYSTEM(ChatColor.WHITE+"["+ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "TPManager"+ChatColor.WHITE+"] " + ChatColor.RESET),
    ERROR(ChatColor.WHITE+"["+ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "TPManager"+ChatColor.WHITE+"] " + ChatColor.RED);
    private final String name;

    Prefix(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
