package me.randomhashtags.randompackage.attribute.condition;

import me.randomhashtags.randompackage.addon.RarityGem;
import me.randomhashtags.randompackage.attribute.AbstractEventCondition;
import me.randomhashtags.randompackage.util.RPPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DepleteRarityGem extends AbstractEventCondition {
    @Override
    public boolean check(Entity entity, String value) {
        if(entity instanceof Player) {
            final String[] values = value.split(":");
            final RarityGem gem = getRarityGem(values[0]);
            if(gem != null) {
                final RPPlayer pdata = RPPlayer.get(entity.getUniqueId());
                if(pdata.hasActiveRarityGem(gem)) {
                    final Player player = (Player) entity;
                    final ItemStack rarityGem = getRarityGem(gem, player);
                    if(rarityGem != null) {
                        ItemMeta itemMeta = rarityGem.getItemMeta();
                        final int amount = getRemainingInt(itemMeta.getDisplayName());
                        int depleteAmount = (int) evaluate(values[1]);

                        /*
                        final FactionUpgrades fu = FactionUpgrades.getFactionUpgrades();
                        if(fu.isEnabled() && hookedFactionsUUID()) {
                            depleteAmount -= depleteAmount*fu.getDecreaseRarityGemPercent(factions.getRegionalIdentifier(u), gem);
                        }*/

                        if(amount-depleteAmount <= 0) {
                            depleteAmount = amount;
                            pdata.toggleRarityGem(gem, gem.getToggleOffRanOutMsg());
                        }
                        itemMeta = rarityGem.getItemMeta();
                        itemMeta.setDisplayName(gem.getItem().getItemMeta().getDisplayName().replace("{SOULS}", Integer.toString(amount - depleteAmount)));
                        rarityGem.setItemMeta(itemMeta);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
