package me.randomhashtags.randompackage.events.customenchant;

import me.randomhashtags.randompackage.addons.CustomEnchant;
import me.randomhashtags.randompackage.events.RPEvent;
import org.bukkit.entity.Player;

public class CustomEnchantApplyEvent extends RPEvent {
	public final CustomEnchant enchant;
	public final int level, success, destroy;
	public final String result;
	public CustomEnchantApplyEvent(Player player, CustomEnchant enchant, int level, int success, int destroy, String result) {
		super(player);
		this.enchant = enchant;
		this.level = level;
		this.success = success;
		this.destroy = destroy;
		this.result = result;
	}
}