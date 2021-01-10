package me.theditor.spigot.api.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SubCommand {

    private final String name;
    private final CoreCommand parent;
    private final List<String> aliases;

    private final String permission;
    private final BiConsumer<CommandSender, List<String>> exec;

    public SubCommand(String name, List<String> aliases, CoreCommand parent, BiConsumer<CommandSender, List<String>> exec) {
        this(name, "", aliases, parent, exec);
    }

    public SubCommand(String name, String permission, CoreCommand parent, BiConsumer<CommandSender, List<String>> exec) {
        this(name, permission, new ArrayList<>(), parent, exec);
    }

    public SubCommand(String name, CoreCommand parent, BiConsumer<CommandSender, List<String>> exec) {
        this(name, "", new ArrayList<>(), parent, exec);
    }

    public SubCommand(String name, String permission, List<String> aliases, CoreCommand parent, BiConsumer<CommandSender, List<String>> exec) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
        this.parent = parent;
        this.exec = exec;
    }

    public String getName() {
        return name;
    }

    public CoreCommand getParent() {
        return parent;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getPermission() {
        return permission;
    }

    public BiConsumer<CommandSender, List<String>> getExec() {
        return exec;
    }
}
