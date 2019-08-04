package me.randomhashtags.randompackage.addons;

import me.randomhashtags.randompackage.addons.utils.Itemable;

import java.util.TreeMap;

public interface Pet extends Itemable {
    TreeMap<Integer, Long> getCooldowns();
    TreeMap<Integer, Long> getRequiredXp();
    int getCooldownSlot();
    int getExpSlot();
}