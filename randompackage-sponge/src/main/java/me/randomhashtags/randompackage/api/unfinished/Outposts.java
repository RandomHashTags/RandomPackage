package me.randomhashtags.randompackage.api.unfinished;

import me.randomhashtags.randompackage.RandomPackageAPI;
import me.randomhashtags.randompackage.utils.classes.Outpost;
import me.randomhashtags.randompackage.utils.enums.OutpostStatus;
import me.randomhashtags.randompackage.utils.universal.UInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Outposts extends RandomPackageAPI implements Listener, CommandExecutor {

    private static Outposts instance;
    public static final Outposts getOutposts() {
        if(instance == null) instance = new Outposts();
        return instance;
    }

    public boolean isEnabled = false;
    public YamlConfiguration config;

    private UInventory gui;
    private ItemStack background;
    public static HashMap<String, String> statuses;

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        final Player player = sender instanceof Player ? (Player) sender : null;
        final int l = args.length;
        if(l == 0) {
            viewStatus(player);
        } else {
            final String a = args[0];
            if(a.equals("help")) viewHelp(sender);
            else if(a.equals("warp")) {
                if(l == 1 && player != null) view(player);
            }
        }
        return true;
    }

    public void enable() {
        final long started = System.currentTimeMillis();
        if(isEnabled) return;
        save(null, "outposts.yml");
        pluginmanager.registerEvents(this, randompackage);
        isEnabled = true;

        if(!otherdata.getBoolean("saved default outposts")) {
            final String[] o = new String[]{"HERO", "SERVONAUT", "TRAINEE", "VANILLA"};
            for(String s : o) save("outposts", s + ".yml");
            otherdata.set("saved default outposts", true);
            saveOtherData();
        }

        config = YamlConfiguration.loadConfiguration(new File(rpd, "outposts.yml"));

        gui = new UInventory(null, config.getInt("gui.size"), ChatColor.translateAlternateColorCodes('&', config.getString("gui.title")));
        final Inventory gi = gui.getInventory();
        statuses = new HashMap<>();
        for(String s : config.getConfigurationSection("status").getKeys(false)) {
            statuses.put(s.toUpperCase().replace(" ", "_"), ChatColor.translateAlternateColorCodes('&', config.getString("status." + s)));
        }

        for(File f : new File(rpd + separator + "outposts").listFiles()) {
            final Outpost o = new Outpost(f);
            o.setOutpostStatus(OutpostStatus.UNCONTESTED);
            gi.setItem(o.getSlot(), o.getDisplay());
        }
        background = d(config, "gui.background");
        int i = 0;
        for(ItemStack is : gi.getContents()) {
            if(is == null) gi.setItem(i, background);
            i++;
        }
        final TreeMap<String, Outpost> O = Outpost.outposts;
        sendConsoleMessage("&6[RandomPackage] &aLoaded " + (O != null ? O.size() : 0) + " Outposts &e(took " + (System.currentTimeMillis()-started) + "ms)");
    }
    public void disable() {
        if(!isEnabled) return;
        isEnabled = false;
        config = null;
        gui = null;
        background = null;
        statuses = null;
        Outpost.deleteAll();
        HandlerList.unregisterAll(this);
    }


    public void viewStatus(CommandSender sender) {
        if(hasPermission(sender, "RandomPackage.outpost", true)) {
            final List<String> msg = colorizeListString(config.getStringList("messages.view current"));
            for(String s : msg) {
                if(s.contains("{OUTPOST}")) {
                    for(Outpost o : Outpost.outposts.values()) {
                        sender.sendMessage(s.replace("{OUTPOST}", o.getName()).replace("{STATUS}", o.getStatus()));
                    }
                } else {
                    sender.sendMessage(s);
                }
            }
        }
    }
    public void viewHelp(CommandSender sender) {
        if(hasPermission(sender, "RandomPackage.outpost.help", true)) {
            sendStringListMessage(sender, config.getStringList("messages.help"), null);
        }
    }
    public void view(Player player) {
        if(hasPermission(player, "RandomPackage.outpost.view", true)) {
            player.closeInventory();

            final int size = gui.getSize();
            player.openInventory(Bukkit.createInventory(null, size, gui.getTitle()));
            final Inventory top = player.getOpenInventory().getTopInventory();
            top.setContents(gui.getInventory().getContents());
            final HashMap<Integer, Outpost> slots = Outpost.slots;
            for(int i = 0; i < size; i++) {
                item = top.getItem(i);
                final Outpost o = slots.getOrDefault(i, null);
                if(o != null) {
                    final String cap = Double.toString(round(o.getControlPercent(), 4)), attacking = o.getAttackingFaction(), controlling = o.getControllingFaction(), status = o.getStatus();
                    itemMeta = item.getItemMeta(); lore.clear();
                    for(String s : itemMeta.getLore()) {
                        if(s.contains("{CAP%}") && controlling == null || s.contains("{ATTACKING}") && attacking == null || s.contains("{CONTROLLING}") && controlling == null) s = null;
                        if(s != null) lore.add(s.replace("{STATUS}", status).replace("{CAP%}", cap).replace("{ATTACKING}", attacking != null ? attacking : "N/A").replace("{CONTROLLING}", controlling != null ? controlling : "N/A"));
                    }
                    itemMeta.setLore(lore); lore.clear();
                    item.setItemMeta(itemMeta);
                }
            }
            player.updateInventory();
        }
    }
    public void tryTeleportingTo(Player player, Outpost outpost) {
        if(hasPermission(player, "RandomPackage.outpost.warp.*", false) || hasPermission(player, "RandomPackage.outpost.warp." + outpost.getYamlName(), true)) {
            try {
                player.teleport(outpost.getWarpLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            } catch(Exception e) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[RandomPackage] &cThis Outpost's world doesn't exist!"));
            }
        }
    }

    @EventHandler
    private void inventoryClickEvent(InventoryClickEvent event) {
        if(!event.isCancelled() && event.getView().getTitle().equals(gui.getTitle())) {
            final Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            player.updateInventory();
            final int r = event.getRawSlot();
            if(r < 0 || r >= player.getOpenInventory().getTopInventory().getSize()) return;
            final Outpost o = Outpost.slots.getOrDefault(r, null);
            if(o != null) {
                tryTeleportingTo(player, o);
            }
        }
    }
}