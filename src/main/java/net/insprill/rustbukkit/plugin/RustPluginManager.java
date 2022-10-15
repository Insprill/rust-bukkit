package net.insprill.rustbukkit.plugin;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import net.insprill.rustbukkit.exception.FileException;
import net.insprill.rustbukkit.exception.NativeLibraryException;
import net.insprill.rustbukkit.exception.PluginException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class RustPluginManager {

    private final File cacheDir;
    private final JavaPlugin rootPlugin;
    private final Set<RustPlugin> plugins = new HashSet<>();

    public RustPluginManager(JavaPlugin rootPlugin) {
        this.rootPlugin = rootPlugin;
        this.cacheDir = new File(rootPlugin.getDataFolder(), "cache");
        if (this.cacheDir.exists() && !this.cacheDir.isDirectory()) {
            throw new FileException("Cache directory already exists as a file!");
        } else {
            this.cacheDir.mkdirs();
        }
    }

    public void registerPlugin(RustPlugin plugin) {
        if (isPluginRegistered(plugin)) {
            throw new PluginException("Already loaded plugin " + plugin.getName() + "!");
        }
        loadPluginLib(plugin);
        plugin.enable();
        plugins.add(plugin);
    }

    public boolean isPluginRegistered(RustPlugin plugin) {
        return plugins.contains(plugin);
    }

    public void unregisterPlugin(RustPlugin plugin) {
        if (!isPluginRegistered(plugin)) {
            throw new PluginException("Plugin " + plugin.getName() + " was never registered!");
        }
        plugin.disable();
        plugins.remove(plugin);
    }

    private void loadPluginLib(RustPlugin plugin) {
        try (InputStream resource = plugin.getResource(System.mapLibraryName(plugin.libraryName()))) {
            if (resource == null) {
                throw new NativeLibraryException("Failed to find native library for " + plugin.getName() + ". Does it support this platform?");
            }
            File cacheFile = cacheNativeLibrary(resource, plugin);
            loadNativeLibrary(cacheFile, plugin);
        } catch (Exception e) {
            throw new PluginException("Failed to load library for " + plugin.getName() + "!", e);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private File cacheNativeLibrary(InputStream library, RustPlugin plugin) {
        try {
            File dest = new File(this.cacheDir, System.mapLibraryName(plugin.getName()));
            if (!dest.exists() && !dest.createNewFile()) {
                throw new FileException("Failed to create cache file");
            }
            byte[] buffer = ByteStreams.toByteArray(library);
            if (compareHash(dest, buffer))
                return dest;
            Files.write(buffer, dest);
            rootPlugin.getLogger().log(Level.INFO, "Cached native library for {0} to {1}", new Object[]{ plugin.getName(), dest });
            return dest;
        } catch (IOException e) {
            throw new FileException("Failed to cache platform native library!", e);
        }
    }

    private void loadNativeLibrary(File libFile, RustPlugin plugin) {
        try {
            System.load(libFile.getAbsolutePath());
            rootPlugin.getLogger().info("Loaded native library for " + plugin.getName() + ".");
        } catch (UnsatisfiedLinkError e) {
            if (!e.getMessage().contains("already loaded")) {
                throw new NativeLibraryException("Failed to load library for " + plugin.getName(), e);
            }
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private boolean compareHash(File a, byte[] b) throws IOException {
        byte[] fileBytes = Files.asByteSource(a).read();
        HashCode fileHash = Hashing.crc32().hashBytes(fileBytes);
        HashCode streamHash = Hashing.crc32().hashBytes(b);
        return fileHash.equals(streamHash);
    }

}
