package com.tacz.guns.compat.carryon;

import com.tacz.guns.GunMod;

public class BlackList {
    private static final String CARRY_ON_ID = "carryon";

    public static void addBlackList() {
        ForgeRegistries.BLOCKS.getKeys().stream().filter(id -> id.getNamespace().equals(GunMod.MOD_ID))
                .forEach(id -> InterModComms.sendTo(CARRY_ON_ID, "blacklistBlock", id::toString));
    }
}
