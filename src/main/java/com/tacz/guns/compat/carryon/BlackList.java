package com.tacz.guns.compat.carryon;

import com.tacz.guns.GunMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.InterModComms;

public class BlackList {
    private static final String CARRY_ON_ID = "carryon";

    public static void addBlackList() {
        BuiltInRegistries.BLOCK.stream()
                .filter(BlackList::isFromGunMod)
                .map(block -> BuiltInRegistries.BLOCK.getKey(block).toString())
                .forEach(blockId -> InterModComms.sendTo(CARRY_ON_ID, "blacklistBlock", () -> blockId));
    }

    private static boolean isFromGunMod(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(GunMod.MOD_ID);
    }
}
