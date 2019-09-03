package me.randomhashtags.randompackage.api.unfinished;

import me.randomhashtags.randompackage.utils.RPFeature;
import org.bukkit.configuration.file.YamlConfiguration;

public class WaterLavaMechanics extends RPFeature {
    private static WaterLavaMechanics instance;
    public static WaterLavaMechanics getWaterLavaMechanics() {
        if(instance == null) instance = new WaterLavaMechanics();
        return instance;
    }

    public YamlConfiguration config;

    public String getIdentifier() { return "WATER/LAVA_MECHANICS"; }
    protected RPFeature getFeature() { return getWaterLavaMechanics(); }
    public void load() {
        final long started = System.currentTimeMillis();
        sendConsoleMessage("&6[RandomPackage] &aLoaded Water/Lava Mechanics &e(took " + (System.currentTimeMillis()-started) + "ms)");
    }
    public void unload() {
    }
}
