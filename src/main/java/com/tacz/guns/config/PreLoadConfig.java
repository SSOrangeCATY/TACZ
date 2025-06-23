package com.tacz.guns.config;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Path;

public class PreLoadConfig {
    private static ModConfigSpec spec;
    public static ModConfigSpec.BooleanValue override;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("gunpack");
        builder.comment("When enabled, the mod will not try to overwrite the default pack under .minecraft/tacz\n" +
                "Since 1.0.4, the overwriting will only run when you start client or a dedicated server");
        override = builder.define("DefaultPackDebug", false);
        builder.pop();
        spec = builder.build();
    }

    public static ModConfigSpec getSpec() {
        return spec;
    }
}
