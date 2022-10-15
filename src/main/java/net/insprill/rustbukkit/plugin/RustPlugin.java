package net.insprill.rustbukkit.plugin;

import net.insprill.rustbukkit.RustBukkit;
import net.insprill.rustbukkit.exception.NativeLibraryException;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;

public abstract class RustPlugin extends JavaPlugin {

    private RustBukkit rustBukkit;

    @Override
    public void onEnable() {
        this.rustBukkit = new RustBukkit(this);
        this.rustBukkit.getPluginManager().registerPlugin(this);

        try {
            getCommands().parallelStream()
                    .map(cmd -> getCommand(cmd.getName()))
                    .filter(Objects::nonNull)
                    .forEach(cmd -> cmd.setExecutor(rustBukkit.getCommandHandler()));
        } catch (ReflectiveOperationException e) {
            getLogger().log(Level.SEVERE, "Failed to register commands", e);
        }
    }

    @Override
    public void onDisable() {
        this.rustBukkit.getPluginManager().unregisterPlugin(this);
    }

    public abstract @NotNull String libraryName();

    public abstract void enable();

    public void disable() {
    }

    public void loadNativeLibrary(@NotNull File libFile) {
        try {
            System.load(libFile.getAbsolutePath());
            getLogger().info("Loaded native library.");
        } catch (UnsatisfiedLinkError e) {
            if (!e.getMessage().contains("already loaded")) {
                throw new NativeLibraryException("Failed to load library", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private @NotNull Collection<Command> getCommands() throws ReflectiveOperationException {
        Class<? extends Server> serverClass = getServer().getClass();
        Field commandMap = serverClass.getDeclaredField("commandMap");
        commandMap.setAccessible(true);
        SimpleCommandMap simpleCommandMap = (SimpleCommandMap) commandMap.get(getServer());
        return simpleCommandMap.getCommands();
    }

}
