package me.randomhashtags.randompackage.utils.supported;

import me.randomhashtags.randompackage.RandomPackage;
import me.randomhashtags.randompackage.api.nearFinished.FactionUpgrades;
import me.randomhashtags.randompackage.utils.supported.regional.ASky;
import me.randomhashtags.randompackage.utils.supported.regional.EpicSky;
import me.randomhashtags.randompackage.utils.supported.regional.FactionsUUID;
import me.randomhashtags.randompackage.utils.supported.regional.SuperiorSky;
import me.randomhashtags.randompackage.utils.universal.UVersion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RegionalAPI extends UVersion {
    private static RegionalAPI instance;
    public static RegionalAPI getRegionalAPI() {
        if(instance == null) instance = new RegionalAPI();
        return instance;
    }

    private FileConfiguration config;
    private static boolean factionsUUID, askyblock, superiorskyblock, epicskyblock;

    protected static FactionsUUID factions;
    protected static ASky asky;
    protected static SuperiorSky ssky;
    protected static EpicSky esky;

    private boolean isTrue(String path) { return config.getBoolean(path); }
    public void setup(RandomPackage randompackage) {
        this.config = randompackage.config;
        trySupportingFactions();
        trySupportingASkyblock();
        trySupportingSuperiorSkyblock();
        trySupportingEpicSkyblock();
    }
    public void trySupportingFactions() {
        factionsUUID = isTrue( "supported plugins.regional.FactionsUUID") && pluginmanager.isPluginEnabled("Factions");
        if(factionsUUID) {
            factions = FactionsUUID.getFactionsUUID();
            factions.enable();
            hooked("FactionsUUID");
            FactionUpgrades.getFactionUpgrades().enable();
        }
    }
    public void trySupportingASkyblock() {
        askyblock = isTrue("supported plugins.regional.ASkyblock") && pluginmanager.isPluginEnabled("ASkyblock");
        if(askyblock) {
            asky = ASky.getASkyblock();
            asky.enable();
            hooked("ASkyblock");
        }
    }
    public void trySupportingSuperiorSkyblock() {
        superiorskyblock = isTrue("supported plugins.regional.SuperiorSkyblock") && pluginmanager.isPluginEnabled("SuperiorSkyblock");
        if(superiorskyblock) {
            ssky = SuperiorSky.getSuperiorSkyblock();
            ssky.enable();
            hooked("SuperiorSkyblock");
        }
    }
    public void trySupportingEpicSkyblock() {
        epicskyblock = isTrue("supported plugins.regional.EpicSkyblock") && pluginmanager.isPluginEnabled("EpicSkyblock");
        if(epicskyblock) {
            esky = EpicSky.getEpicSkyblock();
            esky.enable();
            hooked("EpicSkyblock");
        }
    }
    private void hooked(String plugin) { sendConsoleMessage("&6[RandomPackage] &aHooked Regional Plugin: " + plugin); }
    public boolean hookedFactionsUUID() { return factionsUUID; }
    public boolean hookedASkyblock() { return askyblock; }
    public boolean hookedSuperiorSkyblock() { return superiorskyblock; }
    public boolean hookedEpicSkyblock() { return epicskyblock; }

    public HashMap<Regional, String> getRegionalIdentifiersAt(Location l) {
        final HashMap<Regional, String> a = new HashMap<>();
        if(hookedFactionsUUID()) {
            a.put(factions, factions.getRegionalIdentifierAt(l));
        }
        if(hookedASkyblock()) {
            a.put(asky, asky.getRegionalIdentifierAt(l));
        }
        if(hookedSuperiorSkyblock()) {
            a.put(ssky, ssky.getRegionalIdentifierAt(l));
        }
        if(hookedEpicSkyblock()) {
            a.put(esky, esky.getRegionalIdentifierAt(l));
        }
        return a;
    }
    public boolean isPvPZone(Location l) {
        return isPvPZone(l, null);
    }
    public boolean isPvPZone(Location l, List<String> exceptions) {
        final HashMap<Regional, String> ids = getRegionalIdentifiersAt(l);
        final boolean e = exceptions != null;
        for(Regional r : ids.keySet()) {
            String id = ids.get(r);
            if(id != null) {
                id = ChatColor.stripColor(id);
                if(e && exceptions.contains(id)) return false;
                switch (id) {
                    case "Safezone":
                        return !(r instanceof FactionsUUID);
                    case "spawn":
                        return !(r instanceof ASky);
                    case "":
                        return false;
                    default:
                        return true;
                }
            }
        }
        return false;
    }

    public HashMap<Regional, String> getChatModes(UUID player) {
        final HashMap<Regional, String> m = new HashMap<>();
        if(hookedFactionsUUID()) {
            m.put(factions, factions.getChatMode(player));
        }
        if(hookedASkyblock()) {
            m.put(asky, asky.getChatMode(player));
        }
        if(hookedSuperiorSkyblock()) {
            m.put(ssky, ssky.getChatMode(player));
        }
        if(hookedEpicSkyblock()) {
            m.put(esky, ssky.getChatMode(player));
        }
        return m;
    }
    public List<Player> getReceivingPlayers(UUID player, HashMap<Regional, String> chatModes) {
        final List<Player> a = new ArrayList<>();
        for(Regional r : chatModes.keySet()) {
            final List<Player> o = r.getOnlineAssociates(player);
            if(o != null) a.addAll(o);
        }
        return a;
    }

    private List<UUID> getRelation(UUID player, int type) {
        final List<UUID> a = new ArrayList<>();
        if(factions != null) add(player, type, factions, a);
        if(asky != null) add(player, type, asky, a);
        if(ssky != null) add(player, type, ssky, a);
        return a;
    }
    private void add(UUID player, int type, Regional plugin, List<UUID> list) {
        List<UUID> l = null;
        switch(type) {
            case 0:
                l = plugin.getAssociates(player);
                if(l != null) list.addAll(l);
                return;
            case 1:
                l = plugin.getNeutrals(player);
                if(l != null) list.addAll(l);
                break;
            case 2:
                l = plugin.getAllies(player);
                if(l != null) list.addAll(l);
                break;
            case 3:
                l = plugin.getTruces(player);
                if(l != null) list.addAll(l);
                break;
            case 4:
                l = plugin.getEnemies(player);
                if(l != null) list.addAll(l);
                break;
            default:
                break;
        }
    }

    public List<UUID> getAssociates(UUID player) { return getRelation(player, 0); }
    public List<UUID> getNeutrals(UUID player) { return getRelation(player, 1); }
    public List<UUID> getAllies(UUID player) { return getRelation(player, 2); }
    public List<UUID> getTruces(UUID player) { return getRelation(player, 3); }
    public List<UUID> getEnemies(UUID player) { return getRelation(player, 4); }

    public String getFactionTagAt(Location l) { return factionsUUID ? factions.getRegionalIdentifierAt(l) : null; }
    public String getFactionTag(UUID player) { return factionsUUID ? factions.getRegionalIdentifier(player) : null; }
    public String getFactionTag(OfflinePlayer player) { return getFactionTag(player.getUniqueId()); }
    public List<UUID> getFactionMembers(UUID player) { return factionsUUID ? factions.getAssociates(player) : null; }
}