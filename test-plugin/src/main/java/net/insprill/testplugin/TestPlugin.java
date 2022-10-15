package net.insprill.testplugin;

import net.insprill.rustbukkit.exception.NativeLibraryException;
import net.insprill.rustbukkit.plugin.RustPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class TestPlugin extends RustPlugin {

    @Override
    public @NotNull String libraryName() {
        return "test_plugin";
    }

    @Override
    public native void enable();

    @Override
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

}
