package me.theditor.spigot.api.listeners;

import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class CoreListener<T extends Event> implements EventExecutor, Listener {

    private Consumer<T> callback;
    private final Class<T> event;

    public CoreListener(Class<T> event, JavaPlugin plugin) {
        this(event, plugin, null);
    }

    public CoreListener(Class<T> event, JavaPlugin plugin, Consumer<T> callback) {
        plugin.getServer().getPluginManager().registerEvent(event, this, EventPriority.NORMAL,this, plugin);
        this.callback = callback;
        this.event = event;
    }

    public void setCallback(Consumer<T> callback) {
        this.callback = callback;
    }

    @Override
    public void execute(Listener listener, Event e) throws EventException {
        if(event.isInstance(e)) {
            this.callback.accept(event.cast(e));
        }
    }
}
