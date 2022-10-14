package net.insprill.rustbukkit;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.Level;

public final class RustBukkit extends JavaPlugin {

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void onEnable() {
        String ext = getPlatformLibExtension();
        File libFile = new File(getDataFolder(), "rust_bukkit." + ext);

        try (InputStream platformLib = getResource("rust_bukkit." + ext)) {
            if (!libFile.exists()) {
                libFile.getParentFile().mkdirs();
                libFile.createNewFile();
            }
            byte[] buffer = ByteStreams.toByteArray(platformLib);
            Files.write(buffer, libFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to write platform native library!", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            System.load(libFile.getAbsolutePath());
        } catch (UnsatisfiedLinkError e) {
            if (!e.getMessage().contains("already loaded")) {
                getLogger().log(Level.SEVERE, "Failed to load native library", e);
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }
        getLogger().info(hello("Insprill"));
    }

    private String getPlatformLibExtension() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String extension = "dll";
        if (os.contains("windows")) {
            extension = "dll";
        } else if (os.contains("mac")) {
            extension = "dylib";
        } else if (os.contains("linux")) {
            extension = "so";
        }
        return extension;
    }

    private static native String hello(String str);

}
