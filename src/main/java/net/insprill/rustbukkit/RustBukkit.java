package net.insprill.rustbukkit;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class RustBukkit extends JavaPlugin {

    @Override
    public void onEnable() {
        File lib = new File(getDataFolder(), "rust_bukkit.dll");
        try {
            System.load(lib.getAbsolutePath());
        } catch (UnsatisfiedLinkError e) {
            if (!e.getMessage().contains("already loaded")) {
                getLogger().severe("Failed to load native library.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }
        getLogger().info(hello("Insprill"));
    }

    private static native String hello(String str);

}
