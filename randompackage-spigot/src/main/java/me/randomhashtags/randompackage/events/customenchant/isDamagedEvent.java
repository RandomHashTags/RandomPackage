package me.randomhashtags.randompackage.events.customenchant;

import me.randomhashtags.randompackage.events.AbstractCancellable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class isDamagedEvent extends AbstractCancellable {
    public final Player victim;
    public final LivingEntity damager;
    public double damage;
    public final EntityDamageEvent.DamageCause cause;
    public isDamagedEvent(Player victim, LivingEntity damager, double damage) {
        this.victim = victim;
        this.damager = damager;
        this.damage = damage;
        this.cause = null;
    }
    public isDamagedEvent(Player victim, EntityDamageEvent.DamageCause cause) {
        this.victim = victim;
        this.damager = null;
        this.cause = cause;
    }
}