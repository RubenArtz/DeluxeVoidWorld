package ruben_artz.world.main;

import io.github.slimjar.logging.ProcessLogger;

import java.text.MessageFormat;

public class SlimJarLogger implements ProcessLogger {
    private final DeluxeVoidWorld plugin;

    public SlimJarLogger(DeluxeVoidWorld plugin) {
        this.plugin = plugin;
    }

    @Override
    public void log(String message, Object... args) {

        plugin.getLogger().info(MessageFormat.format(message, args));
    }

    @Override
    public void debug(String message, Object... args) {
        ProcessLogger.super.debug(message, args);
    }
}