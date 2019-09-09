package me.randomhashtags.randompackage.events;

import org.bukkit.entity.Player;

public class PlayerExpGainEvent extends RPEventCancellable {
    private int amount;
    public PlayerExpGainEvent(Player player, int amount) {
        super(player);
        this.amount = amount;
    }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
}
