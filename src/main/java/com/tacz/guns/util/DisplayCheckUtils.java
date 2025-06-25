package com.tacz.guns.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;

public class DisplayCheckUtils {

    public static ParticleOptions readParticle(StringReader reader) throws CommandSyntaxException {
        ParticleType<?> particleType = BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.read(reader));
        if (particleType == null) throw new RuntimeException("particle type is null");
        CompoundTag compoundtag;
        if (reader.canRead() && reader.peek() == '{') {
            compoundtag = new TagParser(reader).readStruct();
        } else {
            compoundtag = new CompoundTag();
        }
        return particleType.codec().codec().parse(NbtOps.INSTANCE, compoundtag).getOrThrow(ParticleArgument.ERROR_INVALID_OPTIONS::create);
    }
}
