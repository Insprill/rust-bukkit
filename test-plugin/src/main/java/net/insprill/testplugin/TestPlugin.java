package net.insprill.testplugin;

import net.insprill.rustbukkit.plugin.RustPlugin;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class TestPlugin extends RustPlugin {

    @Override
    public String libraryName() {
        return "test_plugin";
    }

    @Override
    public void onEnable() {
        super.onEnable();
        getLogger().log(Level.INFO, "{}", hello(Bukkit.getName()));
    }

    private static native String hello(String str);

}
