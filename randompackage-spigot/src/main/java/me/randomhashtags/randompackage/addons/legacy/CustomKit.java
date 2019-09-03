package me.randomhashtags.randompackage.addons.legacy;

import me.randomhashtags.randompackage.addons.FallenHero;
import me.randomhashtags.randompackage.addons.Kits;
import me.randomhashtags.randompackage.addons.utils.Itemable;
import me.randomhashtags.randompackage.addons.objects.KitItem;
import me.randomhashtags.randompackage.utils.addons.RPAddon;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomKit extends RPAddon implements Itemable {
    public abstract Kits getKitClass();
    public abstract int getSlot();
    public abstract int getMaxLevel();
    public abstract long getCooldown();
    public abstract List<KitItem> getItems();
    public abstract FallenHero getFallenHero();
    public String getFallenHeroName() {
        final FallenHero f = getFallenHero();
        return f != null ? f.getSpawnItem().getItemMeta().getDisplayName().replace("{NAME}", getItem().getItemMeta().getDisplayName()) : null;
    }
    public ItemStack getFallenHeroSpawnItem(CustomKit kit) {
        final FallenHero f = getFallenHero();
        return f != null ? get(kit, f.getSpawnItem()) : null;
    }
    public ItemStack getFallenHeroGemItem(CustomKit kit) {
        final FallenHero f = getFallenHero();
        return f != null ? get(kit, f.getGem()) : null;
    }
    private ItemStack get(CustomKit kit, ItemStack is) {
        final String n = kit.getItem().getItemMeta().getDisplayName();
        final ItemMeta m = is.getItemMeta();
        m.setDisplayName(m.getDisplayName().replace("{NAME}", n));
        final List<String> l = new ArrayList<>();
        for(String s : m.getLore()) l.add(s.replace("{NAME}", n));
        m.setLore(l);
        is.setItemMeta(m);
        return is;
    }
}