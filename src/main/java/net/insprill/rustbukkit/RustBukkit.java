package net.insprill.rustbukkit;

import net.insprill.rustbukkit.command.RustCommandHandler;
import net.insprill.rustbukkit.plugin.RustPlugin;
import net.insprill.rustbukkit.plugin.RustPluginManager;
import org.jetbrains.annotations.NotNull;

public final class RustBukkit extends RustPlugin {

    // region Singleton
    private static RustBukkit instance;

    private static void setInstance(RustBukkit instance) {
        RustBukkit.instance = instance;
    }

    public static RustBukkit getInstance() {
        return RustBukkit.instance;
    }
    // endregion

    private RustPluginManager pluginManager;
    private RustCommandHandler commandHandler;

    @Override
    public @NotNull String libraryName() {
        return "rust_bukkit";
    }

    @Override
    public void onEnable() {
        setInstance(this);
        this.pluginManager = new RustPluginManager(this);
        this.commandHandler = new RustCommandHandler();
        super.onEnable();
    }

    public RustPluginManager getPluginManager() {
        return this.pluginManager;
    }

    public RustCommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    @Override
    public native void enable();

}
