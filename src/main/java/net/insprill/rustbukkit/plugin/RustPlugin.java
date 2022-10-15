package net.insprill.rustbukkit.plugin;

import net.insprill.rustbukkit.RustBukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RustPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        RustBukkit.getPluginManager().registerPlugin(this);
    }

    @Override
    public void onDisable() {
        RustBukkit.getInstance().getPluginManager().unregisterPlugin(this);
    }

    public abstract String libraryName();

    public void enable() {
    }

    public void disable() {
    }

}
