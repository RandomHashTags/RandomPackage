package me.randomhashtags.randompackage.utils.classes.coinflip;

import org.spongepowered.api.item.inventory.ItemStack;

import java.util.HashMap;

public class CoinFlipOption {
    public static HashMap<String, CoinFlipOption> paths;
    public final String path, chosen, selectionColor;
    public final int slot;
    private final ItemStack appear, selection;
    public CoinFlipOption(String path, int slot, String chosen, ItemStack appear, ItemStack selection, String selectionColor) {
        if(paths == null) {
            paths = new HashMap<>();
        }
        this.path = path;
        this.slot = slot;
        this.chosen = chosen;
        this.appear = appear;
        this.selection = selection;
        this.selectionColor = selectionColor;
        paths.put(path, this);
    }
    public ItemStack appear() { return appear.copy(); }
    public ItemStack selection() { return selection.copy(); }
}