package net.insprill.rustbukkit;

import net.insprill.rustbukkit.command.RustCommandHandler;
import net.insprill.rustbukkit.plugin.RustPluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RustBukkit {

    private final RustPluginManager pluginManager;
    private final RustCommandHandler commandHandler;

    public RustBukkit(JavaPlugin plugin) {
        this.pluginManager = new RustPluginManager(plugin);
        this.commandHandler = new RustCommandHandler();
    }

    public RustPluginManager getPluginManager() {
        return this.pluginManager;
    }

    public RustCommandHandler getCommandHandler() {
        return this.commandHandler;
    }

}
