package net.insprill.testplugin;

import net.insprill.rustbukkit.plugin.RustPlugin;
import org.jetbrains.annotations.NotNull;

public class TestPlugin extends RustPlugin {

    @Override
    public @NotNull String libraryName() {
        return "test_plugin";
    }

    @Override
    public native void enable();

}
