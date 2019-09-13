package me.randomhashtags.randompackage.addon;

import me.randomhashtags.randompackage.addon.obj.KitItem;
import me.randomhashtags.randompackage.addon.util.Itemable;
import me.randomhashtags.randompackage.addon.util.MaxLevelable;
import me.randomhashtags.randompackage.addon.util.Slotable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public interface CustomKit extends Itemable, MaxLevelable, Slotable {
    Kits getKitClass();
    long getCooldown();
    List<KitItem> getItems();
    FallenHero getFallenHero();
    default String getFallenHeroName() {
        final FallenHero f = getFallenHero();
        return f != null ? f.getSpawnItem().getItemMeta().getDisplayName().replace("{NAME}", getItem().getItemMeta().getDisplayName()) : null;
    }
    default ItemStack getFallenHeroItem(CustomKit kit, boolean isSpawnItem) {
        final FallenHero f = kit.getFallenHero();
        final ItemStack is = f != null ? isSpawnItem ? f.getSpawnItem() : f.getGem() : null;
        if(is != null) {
            final String n = kit.getItem().getItemMeta().getDisplayName();
            final ItemMeta m = is.getItemMeta();
            m.setDisplayName(m.getDisplayName().replace("{NAME}", n));
            final List<String> l = new ArrayList<>();
            for(String s : m.getLore()) l.add(s.replace("{NAME}", n));
            m.setLore(l);
            is.setItemMeta(m);
        }
        return is;
    }
}