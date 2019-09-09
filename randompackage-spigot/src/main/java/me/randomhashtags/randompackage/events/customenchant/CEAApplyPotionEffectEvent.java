package me.randomhashtags.randompackage.events.customenchant;

import me.randomhashtags.randompackage.addons.CustomEnchant;
import me.randomhashtags.randompackage.events.RPEventCancellable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;

public class CEAApplyPotionEffectEvent extends RPEventCancellable {
	public final Event event;
	public final LivingEntity appliedto;
	public final CustomEnchant enchant;
	public final PotionEffect potioneffect;
	public final int enchantlevel;
	public CEAApplyPotionEffectEvent(Event event, Player player, LivingEntity appliedto, CustomEnchant enchant, int enchantlevel, PotionEffect potioneffect) {
		super(player);
		this.event = event;
		this.appliedto = appliedto;
		this.enchant = enchant;
		this.enchantlevel = enchantlevel;
		this.potioneffect = potioneffect;
	}
}