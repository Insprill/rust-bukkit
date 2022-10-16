package net.insprill.rustbukkit.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RustEventHandler implements Listener {

    private static final EventPriority[] PRIORITIES = EventPriority.values();

    private final JavaPlugin plugin;

    public RustEventHandler(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unchecked")
    public void registerEvent(int id, @NotNull String eventName, byte priority, boolean ignoreCancelled) throws ClassNotFoundException {
        Class<? extends Event> clazz = (Class<? extends Event>) Class.forName(eventName);
        Bukkit.getPluginManager().registerEvent(clazz, new Listener() {
        }, PRIORITIES[priority], (listener, event) -> {
            execute(id, event);
        }, plugin, ignoreCancelled);
    }

    private native void execute(int id, Event event);

}
