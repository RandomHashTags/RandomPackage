package me.randomhashtags.randompackage.utils;

import me.randomhashtags.randompackage.RandomPackage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandManager extends Reflect {
    private static CommandManager instance;
    public static CommandManager getCommandManager(RandomPackage randompackage) {
        if(instance == null) {
            instance = new CommandManager();
            instance.config = randompackage.config;
        }
        return instance;
    }

    private SimpleCommandMap commandMap;
    private HashMap<String, Command> knownCommands;

    private HashMap<String, RPFeature> features;
    private static HashMap<String, PluginCommand> actualCmds;
    private Object dispatcher, nodes;

    private FileConfiguration config;
    private ConsoleCommandSender console;

    public String getIdentifier() { return "RP_COMMAND_MANAGER"; }
    protected RPFeature getFeature() { return getCommandManager(randompackage); }
    private CommandManager() {
        actualCmds = new HashMap<>();
        try {
            if(!isLegacy) {
                final Field o = getPrivateField(Class.forName("com.mojang.brigadier.tree.CommandNode"), "children");
                o.setAccessible(true);
                if(v.contains("1.13")) {
                    final com.mojang.brigadier.CommandDispatcher d = net.minecraft.server.v1_13_R2.MinecraftServer.getServer().commandDispatcher.a();
                    dispatcher = d;
                    nodes = o.get(d.getRoot());
                } else {
                    final com.mojang.brigadier.CommandDispatcher d = net.minecraft.server.v1_14_R1.MinecraftServer.getServer().commandDispatcher.a();
                    dispatcher = d;
                    nodes = o.get(d.getRoot());
                }
                o.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        console = Bukkit.getConsoleSender();
        features = new HashMap<>();
        try {
            commandMap = (SimpleCommandMap) getPrivateField(Bukkit.getServer().getPluginManager(), "commandMap");
            knownCommands = (HashMap<String, Command>) getPrivateField(commandMap, "knownCommands", !isLegacy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {}
    public void unload() {}

    public void tryLoadingg(RPFeature f, List<String> baseCmds, boolean enabled) {
        final HashMap<String, String> cmds = new HashMap<>();
        if(baseCmds != null) {
            for(String s : baseCmds) {
                cmds.put(s, s);
            }
        }
        tryLoading(f, cmds, enabled);
    }
    public void tryLoading(RPFeature f, HashMap<String, String> baseCmds, boolean enabled) {
        try {
            if(baseCmds != null && !baseCmds.isEmpty()) {
                for(String base : baseCmds.keySet()) {
                    final String path = baseCmds.get(base);
                    enabled = config.getBoolean(path + ".enabled");
                    if(!knownCommands.containsKey(base)) {
                        final PluginCommand cmd = actualCmds.get(base);
                        commandMap.register(base, cmd);
                        knownCommands.put(base, cmd);
                        knownCommands.put("randompackage:" + base, cmd);
                    }
                    final PluginCommand baseCmd = (PluginCommand) knownCommands.get("randompackage:" + base);
                    baseCmd.setExecutor((CommandExecutor) f);
                    if(!actualCmds.containsKey(base)) actualCmds.put(base, baseCmd);
                    if(enabled) {
                        final List<String> cmds = config.getStringList(path + ".cmds");
                        if(!cmds.isEmpty()) {
                            final String first = cmds.get(0);
                            baseCmd.unregister(commandMap);
                            if(!first.equalsIgnoreCase(base)) {
                                baseCmd.setName(first);
                            }
                            cmds.remove(first);
                            baseCmd.setAliases(cmds);
                            for(String s : cmds) {
                                commandMap.register(s, baseCmd);
                                knownCommands.put("randompackage:" + s, baseCmd);
                            }
                            baseCmd.register(commandMap);
                            updateBrigadierCmd(baseCmd, false);
                        }
                    } else {
                        unregisterPluginCommand(baseCmd);
                    }
                }
            }
            if(enabled) {
                f.enable();
                features.put(f.getIdentifier(), f);
            }
        } catch (Exception e) {
            console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[RandomPackage &cERROR&6] &c&lError trying to load feature:&r &f" + f.getIdentifier()));
            e.printStackTrace();
        }
    }
    public void disable() {
        for(RPFeature f : features.values()) {
            disable(f);
        }
    }
    public void disable(RPFeature f) {
        if(f != null) {
            f.disable();
        }
    }

    private void updateBrigadierCmd(PluginCommand cmd, boolean unregister) {
        if(isLegacy) return;
        final String s = cmd.getName();
        final Map<String, com.mojang.brigadier.tree.CommandNode<?>> o = (Map<String, com.mojang.brigadier.tree.CommandNode<?>>) nodes;
        if(unregister) {
            o.remove("randompackage:" + s);
            o.remove(s);
        } else {
            final com.mojang.brigadier.tree.CommandNode<?> c = o.get(s);
            final com.mojang.brigadier.CommandDispatcher w = (com.mojang.brigadier.CommandDispatcher) dispatcher;
            for(String a : cmd.getAliases()) {
                w.register(com.mojang.brigadier.builder.LiteralArgumentBuilder.literal(a));
            }
        }
    }

    private void unregisterPluginCommand(PluginCommand cmd) {
        final String c = cmd.getName();
        knownCommands.remove("randompackage:" + c);
        cmd.unregister(commandMap);
        boolean hasOtherCmd = false;
        final Set<String> keys = knownCommands.keySet();
        for(int i = 0; i < keys.size(); i++) {
            final String otherCmd = (String) keys.toArray()[i];
            if(!otherCmd.startsWith("RandomPackage:") && otherCmd.split(":")[otherCmd.split(":").length-1].equals(c)) { // gives the last plugin that has the cmd.getName() the command priority
                final PluginCommand pc = (PluginCommand) knownCommands.values().toArray()[i];
                if(!pc.getPlugin().equals(randompackage)) {
                    hasOtherCmd = true;
                    knownCommands.replace(c, cmd, pc);
                    break;
                }
            }
        }
        if(!hasOtherCmd) { // removes the command completely
            knownCommands.remove(c);
            updateBrigadierCmd(cmd, true);
        }
    }
}