package oljek.hd.gui;

import com.oljek.main.util.StringUtil;
import com.oljek.spigot.util.ItemUtil;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import oljek.cl.util.TeamUtil;
import oljek.hd.Hard;
import oljek.hd.date.AnimateDate;
import oljek.hd.object.ArrayAnimateLine;
import oljek.hd.object.Setting;
import oljek.hd.util.RankUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChangeAnimatePageGUI implements Listener {

    private Player p;
    private Inventory inv;
    private int page;
    private List<AnimateItem> animateItems;
    private List<ArrayAnimateLine> allAnimates;
    private Setting setting;
    private Hard hard;

    public ChangeAnimatePageGUI(Hard hard, Player p) {
        this.p = p;
        this.hard = hard;

        page = 1;
        inv = Bukkit.createInventory(null, 54, "§0Выбор анимации");
        animateItems = new ArrayList<>();
        allAnimates = new ArrayList<>();

        Bukkit.getPluginManager().registerEvents(this, hard);
    }

    public void open() {
        inv.clear();

        String group = RankUtil.getGroup(p.getName());
        AnimateDate date = hard.getAnimateStorage().getAnimatedPrefix().get(group.toLowerCase());
        setting = hard.getHardManager().getSetting(p.getName());

        double changeTime = (date == null ? 0.2 : date.getChangeTime());

        if (date != null) {
            if (allAnimates.isEmpty()) {
                for (ArrayAnimateLine animateLine : date.getAnimateLines())
                    allAnimates.add(animateLine);

                //36
            }
        }

        while (allAnimates.size() < page * 36 - 36)
            page--;

        int startSlot = (page * 36) - 36 + 18;
        int finalValue = page * 36 + 18;
        int cursorItem = page * 36 - 36;

        for (int i = startSlot; i < finalValue; i++) {
            if (allAnimates.size() - 1 < cursorItem) {
                inv.setItem(i, ItemUtil.create(Material.BARRIER)
                        .setName("&3Отсутствует")
                        .getStack());
                continue;
            }

            ArrayAnimateLine animateLine = allAnimates.get(cursorItem);

            ItemStack stack = ItemUtil.create(Material.WATCH)
                    .setName("&fАнимация: &c" + animateLine.getName())
                    .getStack();

            AnimateItem itemScheduler = new AnimateItem(animateLine.getLines(), stack, inv, i, p, false);
            itemScheduler.runTaskTimer(hard, 0, (long) (20 * changeTime));

            net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
            NBTTagCompound stackCompound = (nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound());

            stackCompound.setString("animationName", animateLine.getName());
            nmsStack.setTag(stackCompound);

            itemScheduler.setStack(CraftItemStack.asBukkitCopy(nmsStack));
            animateItems.add(itemScheduler);
            cursorItem++;
        }

        for (int i = 9; i < 18; i++)
            inv.setItem(i, ItemUtil.create(Material.STAINED_GLASS_PANE)
                    .setDurabillity(10)
                    .setName("&5Ы")
                    .getStack());

        inv.setItem(0, ItemUtil.create(Material.BARRIER)
                .setName("&cУбрать анимацию")
                .addLore("&7>> Нажмите ПКМ/ЛКМ для действий",
                        "",
                        "&7Нажмите, что-бы снять анимацию")
                .getStack());

        if (date != null) {
            String realityAnimation = setting.getNameAnimation();

            ItemStack realityAnimationItem = ItemUtil.create(Material.WATCH)
                    .setName("&bАнимация префикса в реалиях")
                    .getStack();

            Optional<ArrayAnimateLine> optionAnimateLine = date.getAnimateLines().stream().filter((s) -> s.getName().equals(realityAnimation)).findAny();

            if (realityAnimation.isEmpty() || !optionAnimateLine.isPresent()) {
                AnimateItem itemScheduler = new AnimateItem(new ArrayList<>(), realityAnimationItem, inv, 1, p, true);
                itemScheduler.runTaskTimer(hard, 0, (long) (20 * date.getChangeTime()));

                animateItems.add(itemScheduler);
            } else {
                AnimateItem itemScheduler = new AnimateItem(optionAnimateLine.get().getLines(), realityAnimationItem, inv, 1, p, true);
                itemScheduler.runTaskTimer(hard, 0, (long) (20 * date.getChangeTime()));

                animateItems.add(itemScheduler);
            }
        }

        if (allAnimates.size() > finalValue)
            inv.setItem(2, ItemUtil.create(Material.SPECTRAL_ARROW)
                    .setName("&aНа страницу назад")
                    .getStack());

        if (page > 1)
            inv.setItem(8, ItemUtil.create(Material.SPECTRAL_ARROW)
                    .setName("&aНа страницу вперед")
                    .getStack());

        p.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            Inventory inv = e.getClickedInventory();

            if (inv == null)
                return;

            if (!p.equals(this.p))
                return;

            if (!inv.equals(this.inv)) {
                if (p.getOpenInventory().getTopInventory() != null) {
                    if (p.getOpenInventory().getBottomInventory().getType() == InventoryType.PLAYER) {
                        if (p.getOpenInventory().getTopInventory().equals(inv)) {
                            if (e.getClick() == ClickType.DOUBLE_CLICK || e.getClick().isShiftClick()) {
                                e.setCancelled(true);
                                e.setResult(Event.Result.DENY);
                                return;
                            }
                        }
                    }
                }
                return;
            }

            ItemStack curr = e.getCurrentItem();

            if (curr == null || curr.getType() == Material.AIR)
                return;

            if (e.getClick().isShiftClick() || e.getClick().isKeyboardClick()
                    || e.getSlot() >= 9 && e.getSlot() <= 17
                    || curr.getType() == Material.BARRIER && e.getSlot() != 0
                    || e.getClick() == ClickType.DOUBLE_CLICK) {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
                return;
            }

            if (e.getSlot() == 8) {
                page++;
                open();
                return;
            }

            if (e.getSlot() == 2) {
                page--;
                open();
                return;
            }

            if (e.getSlot() == 0) {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);

                if (!setting.isEnableDynamicPrefix() || setting.getNameAnimation().isEmpty()) {
                    p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fУ Вас уже отключена анимация!"));
                    return;
                }

                setting.setEnableDynamicPrefix(false);
                setting.setNameAnimation("");

                TeamUtil.setPrefix(p);

                p.closeInventory();
                p.sendMessage(StringUtil.inColor("&c&lHARD &7>> &fВы убрали анимацию!"));
                return;
            }

            if (e.getSlot() == 1) {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);

                p.closeInventory();
                return;
            }

            if (e.getSlot() >= 18 && e.getSlot() <= 44) {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);

                net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(curr);
                NBTTagCompound stackCompound = (nmsStack.hasTag() ? nmsStack.getTag() : new NBTTagCompound());

                String animationName = stackCompound.getString("animationName");

                setting.setNameAnimation(animationName);
                setting.setEnableDynamicPrefix(true);

                p.kickPlayer("§fВы применили анимацию префикса таба: §6" + animationName + "\n§cПерезайдите!");
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();

        if (inv != null) {
            if (inv.equals(this.inv) && p.equals(this.p)) {
                p = null;
                inv = null;

                for (AnimateItem item : animateItems)
                    item.cancel();

                animateItems = null;

                HandlerList.unregisterAll(this);
            }
        }
    }

    private static class AnimateItem extends BukkitRunnable {

        private List<String> lines;
        private int cursor;
        private ItemStack stack;
        private Inventory inv;
        private int slot;
        private Player p;
        private boolean isDemonstration;

        public AnimateItem(List<String> lines, ItemStack stack, Inventory inv, int slot, Player p, boolean isDemonstration) {
            this.lines = lines;
            this.stack = stack;
            this.inv = inv;
            this.slot = slot;
            this.p = p;
            this.isDemonstration = isDemonstration;
        }

        @Override
        public void run() {
            if (cursor >= lines.size() - 1)
                cursor = 0;

            String animateLine = (lines.size() == 0 ? "&cNONE" : StringUtil.inColor(lines.get(cursor) + " " + p.getName()));
            List<String> lore = new ArrayList<>();
            ItemMeta meta = stack.getItemMeta();

            if (!isDemonstration) {
                lore.add("§7>> Нажмите ЛКМ/ПКМ для действий");
                lore.add("");
                lore.add("§fВ табе Вы будете выглядить");
                lore.add("§fвот так: " + animateLine);
            } else {
                if (!lines.isEmpty()) {
                    lore.add("§7>> Нажмите ЛКМ/ПКМ что-бы закрыть");
                    lore.add("");
                    lore.add("§fНа данный момент в табе");
                    lore.add("§fВы выглядите вот так: " + animateLine);
                } else {
                    lore.add("§7>> Нажмите ЛКМ/ПКМ что-бы закрыть");
                    lore.add("");
                    lore.add("§fНа данный момент в табе");
                    lore.add("§fВы выглядите вот так: §cNONE");
                    lore.add("");
                    lore.add("§7На данный момент у Вас анимация отключена!");
                }
            }

            meta.setLore(lore);
            stack.setItemMeta(meta);

            inv.setItem(slot, stack);
            p.updateInventory();
            cursor++;
        }

        public void setStack(ItemStack stack) {
            this.stack = stack;
        }

    }

}
