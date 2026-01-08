package artzstudio.dev.deluxevoid.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConfigType {

    // --- LANG ---
    LANG_EN("en_US.yml", "lang", "version"),
    LANG_ES("es_ES.yml", "lang", "version"),

    // --- GLOBAL SETTINGS ---
    CONFIG("config.yml", null, "version"),
    GENERATED("generated.yml", null, "version"),
    WORLD("worlds.yml", null, "version"),

    // --- MENUS ---
    ICONS("icons.yml", "menus", "version"),
    HOME("home.yml", "menus", "version"),
    BOOLEAN("boolean.yml", "menus", "version"),
    CREATE("create.yml", "menus", "version");

    private final String fileName;
    private final String subFolder;
    private final String versionRoute;
}
