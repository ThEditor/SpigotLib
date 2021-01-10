package me.theditor.spigot.api.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoreCommand implements CommandExecutor {

    private final String name;
    private List<SubCommand> subcommands;
    private TetraConsumer<CommandSender, Command, String, String[]> exec;

    public CoreCommand(JavaPlugin plugin, String name) {
        this(plugin, name, new ArrayList<>(), null);
    }

    public CoreCommand(JavaPlugin plugin, String name, TetraConsumer<CommandSender, Command, String, String[]> exec) {
        this(plugin, name, new ArrayList<>(), exec);
    }

    public CoreCommand(JavaPlugin plugin, String name, List<SubCommand> subcommands, TetraConsumer<CommandSender, Command, String, String[]> exec) {
        this.name = name;
        this.subcommands = subcommands;
        plugin.getCommand(this.name).setExecutor(this);
        this.exec = exec;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && subcommands.size() > 0) {
            List<SubCommand> matching = subcommands.stream()
                    .filter(subCommand -> subCommand.getName().equalsIgnoreCase(args[0])
                    || subCommand.getAliases().contains(args[0].toLowerCase())).collect(Collectors.toList());
            if(matching.size() < 1) {
                sender.sendMessage(ChatColor.RED + "Invalid subcommand.");
            } else {
                SubCommand sub = matching.get(0);
                if(sender.hasPermission(sub.getPermission())) {
                    List<String> newArgs = new ArrayList<>(Arrays.asList(args));
                    newArgs.remove(0);
                    matching.get(0).getExec().accept(sender, newArgs);
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not enough permissions to run this command.");
                }
            }
        } else {
            if(exec != null)
                this.exec.accept(sender, command, label, args);
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public TetraConsumer<CommandSender, Command, String, String[]> getExec() {
        return exec;
    }

    public void setExec(TetraConsumer<CommandSender, Command, String, String[]> exec) {
        this.exec = exec;
    }

    public void setSubcommands(List<SubCommand> subcommands) {
        this.subcommands = subcommands;
    }
}
