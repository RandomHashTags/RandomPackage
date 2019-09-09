package me.randomhashtags.randompackage.events;

import me.randomhashtags.randompackage.addons.ServerCrate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ServerCrateCloseEvent extends RPEvent {
    public final ServerCrate crate;
    public final List<ItemStack> rewards;
    public ServerCrateCloseEvent(Player player, ServerCrate crate, List<ItemStack> rewards) {
        super(player);
        this.crate = crate;
        this.rewards = rewards;
    }
}
