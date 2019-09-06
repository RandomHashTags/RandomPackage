package me.randomhashtags.randompackage.attributes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import java.util.HashMap;

public class Damage extends AbstractEventAttribute {
    @Override
    public void execute(Event event, HashMap<Entity, String> recipientValues) {
        for(Entity e : recipientValues.keySet()) {
            final LivingEntity l = e instanceof LivingEntity ? (LivingEntity) e : null;
            if(l != null) {
                l.damage(evaluate(recipientValues.get(e)));
            }
        }
    }
}
