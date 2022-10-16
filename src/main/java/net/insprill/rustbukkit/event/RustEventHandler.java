package net.insprill.rustbukkit.event;

import net.insprill.rustbukkit.exception.EventException;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class RustEventHandler implements Listener {

    private final JavaPlugin plugin;

    public RustEventHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerEvent(int id, String eventName, String priority, boolean ignoreCancelled) {
        Class<? extends Event> clazz;
        try {
            clazz = (Class<? extends Event>) Class.forName(eventName);
        } catch (ClassNotFoundException e) {
            throw new EventException("Failed to find event " + eventName, e);
        }
        Bukkit.getPluginManager().registerEvent(clazz, new Listener() {
        }, EventPriority.valueOf(priority), (listener, event) -> {
            execute(id, event);
        }, plugin, ignoreCancelled);
    }

    private native void execute(int id, Event event);

}
