package com.github.frango28.tp_manager.test;

import com.github.frango28.tp_manager.TPManagerBukkit;
import com.github.frango28.tp_manager.TPPointManager;
import com.github.frango28.tp_manager.TeleportPoint;
import com.github.frango28.tp_manager.util.Prefix;
import com.github.frango28.tp_manager.util.TeleportPointUtils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class SelectorMenu implements Listener {
    private final String MENU_NAME = ChatColor.DARK_AQUA+ChatColor.BOLD.toString() + "TP Point";
    private final Player p;
    private Inventory inv;
    private final List<TeleportPoint> pointList;

    private int page;

    public SelectorMenu(Player _p, int page) {
        TPManagerBukkit plugin = TPManagerBukkit.instance();
        this.page = Math.max(0, page);
        this.p = _p;

        TPPointManager m = TPManagerBukkit.getTPPointMng();
        this.pointList = m.getMapList();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.setInvMenu();
        open();
    }

    private void open() {
        p.openInventory(inv);
    }

    private int getPageAmount() {
        int t = this.pointList.size();
        return (t - (t % 45)) / 45 + 1;
    }

    private void setInvMenu() {
//        インベントリーを生成
        inv = Bukkit.createInventory(p, 54, this.MENU_NAME + ChatColor.DARK_GRAY + "    (" + (this.page + 1) + "/" + this.getPageAmount() + ")");

//        ページのアイテムをセット
        if (page < this.getPageAmount()) {
            for (int i = 0; i < 45; i++) {
                if (i + page * 45 == this.pointList.size()) {
                    break;
                }
                inv.setItem(i, TeleportPointUtils.getIconItemStack(this.pointList.get(i + page * 45)));
            }
        }
//        メニューバーを作成
//        前に戻るボタン
        ItemStack beforePage;
        if (0 < this.page) {
            beforePage = new ItemStack(Material.BLUE_WOOL);
            ItemMeta beforePageMeta = beforePage.getItemMeta();
            Objects.requireNonNull(beforePageMeta).setDisplayName(ChatColor.YELLOW + "前のページを開く");
            beforePage.setItemMeta(beforePageMeta);
        } else {
            beforePage = new ItemStack(Material.GRAY_WOOL);
            ItemMeta beforePageMeta = beforePage.getItemMeta();
            Objects.requireNonNull(beforePageMeta).setDisplayName(ChatColor.DARK_GRAY + "---");
            beforePage.setItemMeta(beforePageMeta);
        }
        inv.setItem(45, beforePage);

//        次に進む
        ItemStack afterPage;
        if (this.page < this.getPageAmount() - 1) {
            afterPage = new ItemStack(Material.RED_WOOL);
            ItemMeta afterPageMeta = afterPage.getItemMeta();
            Objects.requireNonNull(afterPageMeta).setDisplayName(ChatColor.YELLOW + "次のページを開く");
            afterPage.setItemMeta(afterPageMeta);
        } else {
            afterPage = new ItemStack(Material.GRAY_WOOL);
            ItemMeta afterPageMeta = beforePage.getItemMeta();
            afterPageMeta.setDisplayName(ChatColor.DARK_GRAY + "---");
            afterPage.setItemMeta(afterPageMeta);
        }
        inv.setItem(53, afterPage);


    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory() == this.inv) {
//            リスナーを解除
            HandlerList.unregisterAll(this);
        }
    }

    //    クリックイベント
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory() == inv) {
            e.setCancelled(true);
            if (e.getClickedInventory() == this.inv) {
                Player p = (Player) e.getWhoClicked();

//                クリックされたアイテムのマップデータを取得
                int slot = e.getSlot();
                if (0 <= slot && slot < 45) {
                    if (slot + this.page * 45 >= pointList.size()) return;
                    TeleportPoint data = this.pointList.get(slot + this.page * 45);
                    if (data == null) return;
//                    クリックの処理
                    if (e.getClick() == ClickType.LEFT) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
                        this.tpPlayer(p, data);
                    }
                } else if (slot == 45) {
                    if (Objects.requireNonNull(e.getCurrentItem()).getType() != Material.GRAY_WOOL) {
                        this.page -= 1;
                        this.setInvMenu();
                        this.open();
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
                    }
                } else if (slot == 53) {
                    if (Objects.requireNonNull(e.getCurrentItem()).getType() != Material.GRAY_WOOL) {
                        this.page += 1;
                        this.setInvMenu();
                        this.open();
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
                    }
                }
            }
        }
    }

    private void tpPlayer(Player p, TeleportPoint data) {
        if (p == null || !p.isOnline()) return;
        if (data == null) throw new NullPointerException("プレイヤーテレポート時のMapDataがNullです");

        p.closeInventory();
        p.teleport(data.getLocation());
        p.spigot().sendMessage(new ComponentBuilder().append(Prefix.SYSTEM.toString())
                .append(p.getName() + "を")
                .append("[" + data.getName() + "]").color(net.md_5.bungee.api.ChatColor.BOLD)
                .append("にテレポートしました").color(net.md_5.bungee.api.ChatColor.RESET)
                .create());
    }
}
