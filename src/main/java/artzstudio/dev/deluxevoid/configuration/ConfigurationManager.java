/*
 *
 *  Copyright (c) 2026 Ruben_Artz and Artz Studio. All rights reserved.
 *
 *  This code is proprietary software. It is strictly prohibited to
 *  copy, modify, distribute, or use this code for any purpose
 *  without the express written permission of the owner.
 *
 *  Project: Deluxe Void World
 *
 */

package artzstudio.dev.deluxevoid.configuration;

import artzstudio.dev.deluxevoid.DeluxeVoidWorld;
import dev.dejvokep.boostedyaml.YamlDocument;

import java.util.EnumMap;
import java.util.Map;

public class ConfigurationManager {

    private final DeluxeVoidWorld plugin;
    private final Map<ConfigType, YamlConfig> configs;

    public ConfigurationManager(DeluxeVoidWorld plugin) {
        this.plugin = plugin;
        this.configs = new EnumMap<>(ConfigType.class);
    }

    public void loadAll() {
        for (ConfigType type : ConfigType.values()) {
            configs.put(type, new YamlConfig(plugin, type));
        }
    }

    public YamlDocument get(ConfigType type) {
        if (!configs.containsKey(type)) {
            configs.put(type, new YamlConfig(plugin, type));
        }
        return configs.get(type).getDocument();
    }

    public void reloadAll() {
        configs.values().forEach(YamlConfig::reload);
    }

    public void saveAll() {
        configs.values().forEach(YamlConfig::save);
    }
}