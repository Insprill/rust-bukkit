package net.insprill.rustbukkit;

import net.insprill.rustbukkit.plugin.RustPlugin;
import net.insprill.rustbukkit.plugin.RustPluginManager;

public final class RustBukkit extends RustPlugin {

    private static RustPluginManager pluginManager;

    @Override
    public String libraryName() {
        return "rust_bukkit";
    }

    @Override
    public void onEnable() {
        setPluginManager(new RustPluginManager(this));
        super.onEnable();
    }

    public static RustPluginManager getPluginManager() {
        return RustBukkit.pluginManager;
    }

    private static void setPluginManager(RustPluginManager pluginManager) {
        RustBukkit.pluginManager = pluginManager;
    }

    @Override
    public native void enable();

}
