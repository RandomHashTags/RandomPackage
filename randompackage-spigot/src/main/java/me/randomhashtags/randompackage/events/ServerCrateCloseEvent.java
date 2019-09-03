package me.randomhashtags.randompackage.events;

import me.randomhashtags.randompackage.addons.ServerCrate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ServerCrateCloseEvent extends AbstractEvent {
    public final Player player;
    public final ServerCrate crate;
    public final List<ItemStack> rewards;
    public ServerCrateCloseEvent(Player player, ServerCrate crate, List<ItemStack> rewards) {
        this.player = player;
        this.crate = crate;
        this.rewards = rewards;
    }
}