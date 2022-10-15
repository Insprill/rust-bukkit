package net.insprill.rustbukkit.plugin;

import net.insprill.rustbukkit.RustBukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;
import java.util.logging.Level;

public abstract class RustPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        RustBukkit rustBukkit = RustBukkit.getInstance();
        rustBukkit.getPluginManager().registerPlugin(this);

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
        RustBukkit.getInstance().getPluginManager().unregisterPlugin(this);
    }

    public abstract String libraryName();

    public abstract void enable();

    public void disable() {
    }

    @SuppressWarnings("unchecked")
    private Collection<Command> getCommands() throws ReflectiveOperationException {
        Class<? extends Server> serverClass = getServer().getClass();
        Field commandMap = serverClass.getDeclaredField("commandMap");
        commandMap.setAccessible(true);
        SimpleCommandMap simpleCommandMap = (SimpleCommandMap) commandMap.get(getServer());
        return simpleCommandMap.getCommands();
    }

}
