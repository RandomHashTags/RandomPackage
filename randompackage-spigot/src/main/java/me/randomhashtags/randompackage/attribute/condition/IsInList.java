package me.randomhashtags.randompackage.attribute.condition;

import me.randomhashtags.randompackage.attribute.AbstractEventCondition;
import me.randomhashtags.randompackage.attribute.Listable;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class IsInList extends AbstractEventCondition implements Listable {
    @Override
    public boolean check(Entity entity, String value) {
        final UUID u = entity.getUniqueId();
        final String[] values = value.split(":");
        final boolean bool = values.length == 1, contains = list.containsKey(u) && list.get(u).contains(values[0]);
        return contains == (bool || Boolean.parseBoolean(values[1]));
    }
}
