package me.randomhashtags.randompackage.events.customenchant;

import me.randomhashtags.randompackage.addons.CustomEnchant;
import me.randomhashtags.randompackage.events.AbstractCancellable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerRevealCustomEnchantEvent extends AbstractCancellable {
	public final Player player;
	public final CustomEnchant enchant;
	public final int level;
	public final ItemStack item;
	public PlayerRevealCustomEnchantEvent(Player player, ItemStack item, CustomEnchant enchant, int level) {
		this.player = player;
		this.item = item;
		this.enchant = enchant;
		this.level = level;
	}
}