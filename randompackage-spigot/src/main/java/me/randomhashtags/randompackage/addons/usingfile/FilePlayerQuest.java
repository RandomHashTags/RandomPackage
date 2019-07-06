package me.randomhashtags.randompackage.addons.usingfile;

import me.randomhashtags.randompackage.addons.PlayerQuest;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.List;

public class FilePlayerQuest extends PlayerQuest {

    public FilePlayerQuest(File f) {
        load(f);
        addPlayerQuest(getIdentifier(), this);
    }
    public String getIdentifier() { return getYamlName(); }

    public boolean isEnabled() { return yml.getBoolean("settings.enabled"); }
    public String getName() { return ChatColor.translateAlternateColorCodes('&', yml.getString("settings.name")); }
    public long getExpiration() { return yml.getLong("settings.expiration"); }
    public String getCompletion() { return yml.getString("settings.completion"); }
    public boolean isTimeBased() {
        final String c = getCompletion().toLowerCase();
        return c.contains("d") || c.contains("h") || c.contains("m") || c.contains("s");
    }
    public List<String> getLore() { return api.colorizeListString(yml.getStringList("lore")); }
    public List<String> getRewards(){ return yml.getStringList("rewards"); }
    public List<String> getTrigger() { return yml.getStringList("trigger"); }
}
