package me.randomhashtags.randompackage.utils.classes.kits;

import me.randomhashtags.randompackage.api.CustomEnchants;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MasteryKit {
    public static HashMap<String, MasteryKit> kits;
    private static CustomEnchants customenchants;
    private YamlConfiguration yml;
    private String ymlName, name;
    private int slot, antiCrystalPercentSlot;
    private long cooldown;
    private ItemStack item, redeem, shard, antiCrystal;
    private LinkedHashMap<Object, Integer> requiredKits;
    private boolean losesKitsUponUnlocking;
    private List<KitItem> items;
    private List<String> antiCrystalNegatedEnchants, antiCrystalAddedLore;
    public MasteryKit(File f) {
        if(kits == null) {
            kits = new HashMap<>();
            customenchants = CustomEnchants.getCustomEnchants();
        }
        this.yml = YamlConfiguration.loadConfiguration(f);
        this.ymlName = f.getName().split("\\.yml")[0];
        losesKitsUponUnlocking = yml.getBoolean("settings.loses required kits");
        slot = yml.getInt("gui settings.slot");
        kits.put(ymlName, this);
    }
    public YamlConfiguration getYaml() { return yml; }
    public String getYamlName() { return ymlName; }
    public int getSlot() { return slot; }
    public long getCooldown() {
        if(cooldown == 0) cooldown = yml.getLong("settings.cooldown");
        return cooldown;
    }
    public String getName() {
        if(name == null) name = ChatColor.translateAlternateColorCodes('&', yml.getString("settings.name"));
        return name;
    }
    public ItemStack getItem() {
        if(item == null) {
            item = customenchants.d(yml, "gui settings");
            final ItemMeta m = item.getItemMeta();
            final List<String> l = new ArrayList<>();
            int req = 0;
            final HashMap<Object, Integer> re = getRequiredKits();
            for(String s : m.getLore()) {
                if(s.contains("{REQUIREMENT}")) {
                    final Object kit = re.keySet().toArray()[req];
                    final GlobalKit gkit = kit instanceof GlobalKit ? (GlobalKit) kit : null;
                    final EvolutionKit vkit = gkit == null && kit instanceof EvolutionKit ? (EvolutionKit) kit : null;
                    final MasteryKit mkit = vkit == null && kit instanceof MasteryKit ? (MasteryKit) kit : null;
                    final String name = (gkit != null ? gkit.getItem() : vkit != null ? vkit.getItem() : mkit.getItem()).getItemMeta().getDisplayName();
                    s = s.replace("{REQUIREMENT}", name).replace("{TIER}", customenchants.toRoman(re.get(kit)));
                    req++;
                }
                l.add(s);
                m.setLore(l);
                item.setItemMeta(m);
            }
        }
        return item.clone();
    }
    public ItemStack getRedeem() {
        if(redeem == null) {
            redeem = customenchants.d(yml, "redeem");
            if(redeem != null) {
                final ItemMeta m = redeem.getItemMeta();
                final List<String> l = new ArrayList<>();
                int req = 0;
                final HashMap<Object, Integer> re = getRequiredKits();
                for(String s : m.getLore()) {
                    if(s.contains("{REQUIREMENT}")) {
                        final Object kit = re.keySet().toArray()[req];
                        final GlobalKit gkit = kit instanceof GlobalKit ? (GlobalKit) kit : null;
                        final EvolutionKit vkit = gkit == null && kit instanceof EvolutionKit ? (EvolutionKit) kit : null;
                        final MasteryKit mkit = vkit == null && kit instanceof MasteryKit ? (MasteryKit) kit : null;
                        final String name = (gkit != null ? gkit.getItem() : vkit != null ? vkit.getItem() : mkit.getItem()).getItemMeta().getDisplayName();
                        s = s.replace("{REQUIREMENT}", name).replace("{TIER}", customenchants.toRoman(re.get(kit)));
                        req++;
                    }
                    l.add(s);
                    m.setLore(l);
                    redeem.setItemMeta(m);
                }
            }
        }
        return redeem != null ? redeem.clone() : null;
    }
    public List<KitItem> getItems() {
        if(items == null) {
            items = new ArrayList<>();
            for(String i : yml.getConfigurationSection("items").getKeys(false)) {
                final String t = yml.getString("items." + i + ".item");
                if(t != null) {
                    final int chance = yml.get("items." + i + ".chance") != null ? yml.getInt("items." + i + ".chance") : 100;
                    items.add(new KitItem(this, i, yml.getString("items." + i + ".item"), yml.getString("items." + i + ".name"), yml.getStringList("items." + i + ".lore"), chance, "1", false, yml.getInt("items." + i + ".reqlevel")));
                }
            }
        }
        return items;
    }
    public LinkedHashMap<Object, Integer> getRequiredKits() {
        if(requiredKits == null) {
            requiredKits = new LinkedHashMap<>();
            final List<String> R = yml.getStringList("required kits");
            for(String s : R) {
                final String[] a = s.split(";");
                final String K = a[0].toLowerCase(), ki = a[1];
                Object kit = null;
                if(K.equals("gkit")) kit = GlobalKit.kits.get(ki);
                else if(K.equals("vkit")) kit = EvolutionKit.kits.get(ki);
                else if(K.equals("mkit")) kit = MasteryKit.kits.get(ki);
                requiredKits.put(kit, Integer.parseInt(a[2]));
            }
        }
        return requiredKits;
    }
    public boolean losesKitsUponUnlocking() { return losesKitsUponUnlocking; }
    public ItemStack getShard() {
        if(shard == null) shard = customenchants.d(yml, "shard");
        return shard.clone();
    }
    public ItemStack getAntiCrystal() {
        if(antiCrystal == null) {
            antiCrystal = customenchants.d(yml, "anti crystal");
            int i = 0;
            for(String s : antiCrystal.getItemMeta().getLore()) {
                if(s.contains("{PERCENT}")) antiCrystalPercentSlot = i;
                i++;
            }
        }
        return antiCrystal.clone();
    }
    public int getAntiCrystalPercentSlot() {
        if(antiCrystal == null) getAntiCrystal();
        return antiCrystalPercentSlot;
    }
    public ItemStack getAntiCrystal(int percent) {
        final ItemStack i = getAntiCrystal();
        final ItemMeta m = i.getItemMeta();
        final List<String> l = m.getLore();
        for(String s : m.getLore()) l.add(s.replace("{PERCENT}", Integer.toString(percent)));
        m.setLore(l);
        i.setItemMeta(m);
        return i;
    }
    public List<String> getAntiCrystalNegatedEnchants() {
        if(antiCrystalNegatedEnchants == null) antiCrystalNegatedEnchants = yml.getStringList("anti crystal.negate enchants");
        return antiCrystalNegatedEnchants;
    }
    public List<String> getAntiCrystalAddedLore() {
        if(antiCrystalAddedLore == null) antiCrystalAddedLore = customenchants.colorizeListString(yml.getStringList("anti crystal.added lore"));
        return antiCrystalAddedLore;
    }

    public static MasteryKit valueOf(int slot) {
        if(kits != null) {
            for(MasteryKit m : kits.values()) {
                if(m.getSlot() == slot) {
                    return m;
                }
            }
        }
        return null;
    }
    public static MasteryKit valueOfAntiCrystal(ItemStack is) {
        if(kits != null && is != null && is.hasItemMeta() && is.getItemMeta().hasLore()) {
            final List<String> l = is.getItemMeta().getLore();
            for(MasteryKit m : kits.values()) {
                final int p = customenchants.getRemainingInt(l.get(m.antiCrystalPercentSlot));
                if(is.isSimilar(m.getAntiCrystal(p))) {
                    return m;
                }
            }
        }
        return null;
    }
    public static MasteryKit valueOfRedeem(ItemStack redeem) {
        if(kits != null && redeem != null) {
            for(MasteryKit m : kits.values()) {
                final ItemStack r = m.getRedeem();
                if(r != null && r.isSimilar(redeem)) {
                    return m;
                }
            }
        }
        return null;
    }
    public static MasteryKit hasAntiCrystal(ItemStack is) {
        if(kits != null && is != null && is.hasItemMeta() && is.getItemMeta().hasLore()) {
            final List<String> l = is.getItemMeta().getLore();
            for(MasteryKit m : kits.values()) {
                if(l.containsAll(m.getAntiCrystalAddedLore())) {
                    return m;
                }
            }
        }
        return null;
    }

    public static void deleteAll() {
        kits = null;
        customenchants = null;
    }
}