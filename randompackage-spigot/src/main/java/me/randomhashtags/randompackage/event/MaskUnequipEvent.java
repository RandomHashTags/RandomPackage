package me.randomhashtags.randompackage.event;

import me.randomhashtags.randompackage.addon.Mask;
import me.randomhashtags.randompackage.event.armor.ArmorEventReason;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MaskUnequipEvent extends RPEventCancellable {
    public final Mask mask;
    public final ItemStack helmet;
    public final ArmorEventReason reason;
    public MaskUnequipEvent(Player player, Mask mask, ItemStack helmet, ArmorEventReason reason) {
        super(player);
        this.mask = mask;
        this.helmet = helmet;
        this.reason = reason;
    }
}