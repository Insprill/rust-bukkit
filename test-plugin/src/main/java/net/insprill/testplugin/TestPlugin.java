package net.insprill.testplugin;

import net.insprill.rustbukkit.plugin.RustPlugin;

public class TestPlugin extends RustPlugin {

    @Override
    public String libraryName() {
        return "test_plugin";
    }

    @Override
    public native void enable();

}
