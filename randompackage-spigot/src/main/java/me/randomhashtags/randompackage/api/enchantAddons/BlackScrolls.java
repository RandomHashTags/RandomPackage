package me.randomhashtags.randompackage.api.enchantAddons;

import me.randomhashtags.randompackage.addons.BlackScroll;
import me.randomhashtags.randompackage.addons.CustomEnchant;
import me.randomhashtags.randompackage.addons.EnchantRarity;
import me.randomhashtags.randompackage.addons.usingpath.PathBlackScroll;
import me.randomhashtags.randompackage.api.CustomEnchants;
import me.randomhashtags.randompackage.utils.CustomEnchantUtils;
import me.randomhashtags.randompackage.utils.Feature;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BlackScrolls extends CustomEnchantUtils {
    private static BlackScrolls instance;
    public static BlackScrolls getBlackScrolls() {
        if(instance == null) instance = new BlackScrolls();
        return instance;
    }

    public YamlConfiguration config;
    public void load() {
        final long started = System.currentTimeMillis();
        save("custom enchants", "black scrolls.yml");
        config = YamlConfiguration.loadConfiguration(new File(rpd + separator + "custom enchants" + separator, "black scrolls.yml"));
        final ConfigurationSection cs = config.getConfigurationSection("scrolls");
        if(cs != null) {
            PathBlackScroll.blackscrollsyml = config;
            for(String s : cs.getKeys(false)) new PathBlackScroll(s);
        }
        sendConsoleMessage("&6[RandomPackage] &aLoaded " + (blackscrolls != null ? blackscrolls.size() : 0) + " Black Scrolls &e(took " + (System.currentTimeMillis()-started) + "ms)");
    }
    public void unload() {
        config = null;
        deleteAll(Feature.BLACK_SCROLLS);
    }


    public ItemStack applyBlackScroll(ItemStack is, ItemStack blackscroll, BlackScroll bs) {
        item = null;
        final HashMap<CustomEnchant, Integer> enchants = getEnchants(is);
        if(is != null && enchants.size() > 0) {
            final Set<CustomEnchant> key = enchants.keySet();
            CustomEnchant enchant = (CustomEnchant) key.toArray()[random.nextInt(key.size())];
            final List<EnchantRarity> a = bs.getAppliesTo();
            int successP = -1;
            for(String string : blackscroll.getItemMeta().getLore()) if(getRemainingInt(string) != -1) successP = getRemainingInt(string);
            for(int f = 1; f <= 5; f++) {
                final EnchantRarity r = EnchantRarity.valueOf(enchant);
                if(a.contains(r)) {
                    int enchantlevel = enchants.get(enchant);
                    itemMeta = is.getItemMeta(); lore.clear(); lore.addAll(itemMeta.getLore());
                    int enchantslot = -1;
                    final String ap = r.getApplyColors();
                    for(int i = 0; i < lore.size(); i++) if(lore.get(i).equals(ap + enchant.getName() + " " + toRoman(enchantlevel))) enchantslot = i;
                    if(enchantslot == -1) return null;
                    lore.remove(enchantslot);
                    itemMeta.setLore(lore); lore.clear();
                    is.setItemMeta(itemMeta);
                    return CustomEnchants.getCustomEnchants().getRevealedItem(enchant, enchantlevel, successP, 100, true, true).clone();
                } else enchant = (CustomEnchant) key.toArray()[random.nextInt(key.size())];
            }
        }
        return item;
    }


    @EventHandler(priority = EventPriority.HIGH)
    private void inventoryClickEvent(InventoryClickEvent event) {
        final ItemStack cursor = event.getCursor(), current = event.getCurrentItem();
        if(!event.isCancelled() && current != null && !current.getType().equals(Material.AIR) && cursor != null && cursor.hasItemMeta() && cursor.getItemMeta().hasDisplayName() && cursor.getItemMeta().hasLore()) {
            final Player player = (Player) event.getWhoClicked();
            int enchantcount = -1;
            item = current; itemMeta = current.getItemMeta(); lore.clear();
            HashMap<CustomEnchant, Integer> enchantmentsonitem = null;
            if(current.hasItemMeta() && current.getItemMeta().hasLore()) enchantmentsonitem = getEnchants(current);

            final BlackScroll bs = BlackScroll.valueOf(cursor);
            if(bs != null && item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && !enchantmentsonitem.isEmpty()) {
                enchantcount = enchantmentsonitem.size();
                giveItem(player, applyBlackScroll(current, cursor, bs));
                item = current; itemMeta = item.getItemMeta();
                if(itemMeta.hasDisplayName()) {
                    final String d = itemMeta.getDisplayName(), l = TRANSMOG.replace("{LORE_COUNT}", Integer.toString(enchantcount));
                    if(d.contains(l)) {
                        itemMeta.setDisplayName(d.replace(l, TRANSMOG.replace("{LORE_COUNT}", Integer.toString(enchantcount-1))));
                    }
                }
                //playSuccess((Player) event.getWhoClicked());
                item.setItemMeta(itemMeta);
                event.setCancelled(true);
                event.setCurrentItem(item);
                final int a = cursor.getAmount();
                if(a == 1) event.setCursor(new ItemStack(Material.AIR));
                else       cursor.setAmount(a-1);
                player.updateInventory();
            }
        }
    }
}
