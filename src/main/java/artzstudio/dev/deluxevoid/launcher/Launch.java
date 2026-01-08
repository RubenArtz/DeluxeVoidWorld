package artzstudio.dev.deluxevoid.launcher;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;

public interface Launch {
    void launch(DeluxeVoidWorld plugin);

    void shutdown();
}
