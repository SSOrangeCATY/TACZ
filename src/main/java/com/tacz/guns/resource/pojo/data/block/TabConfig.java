package com.tacz.guns.resource.pojo.data.block;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tacz.guns.GunMod;
import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.item.builder.AmmoItemBuilder;
import com.tacz.guns.api.item.builder.AttachmentItemBuilder;
import com.tacz.guns.api.item.builder.GunItemBuilder;
import com.tacz.guns.api.item.component.AccessoryComponents;
import com.tacz.guns.api.item.component.AmmoComponents;
import com.tacz.guns.api.item.component.GunComponents;
import com.tacz.guns.init.ModItems;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record TabConfig(ResourceLocation id, String name, ItemData icon) {
    public static final Codec<TabConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(TabConfig::id),
                    Codec.STRING.fieldOf("name").forGetter(TabConfig::name),
                    ItemData.CODEC.fieldOf("icon").forGetter(TabConfig::icon)
            ).apply(instance, TabConfig::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, TabConfig> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            TabConfig::id,
            ByteBufCodecs.STRING_UTF8,
            TabConfig::name,
            ItemData.STREAM_CODEC,
            TabConfig::icon,
            TabConfig::new
    );

    public static final ResourceLocation TAB_AMMO = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "ammo");
    public static final ResourceLocation TAB_PISTOL = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "pistol");
    public static final ResourceLocation TAB_SNIPER = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "sniper");
    public static final ResourceLocation TAB_RIFLE = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "rifle");
    public static final ResourceLocation TAB_SHOTGUN = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "shotgun");
    public static final ResourceLocation TAB_SMG = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "smg");
    public static final ResourceLocation TAB_RPG = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "rpg");
    public static final ResourceLocation TAB_MG = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "mg");
    public static final ResourceLocation TAB_SCOPE = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "scope");
    public static final ResourceLocation TAB_MUZZLE = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "muzzle");
    public static final ResourceLocation TAB_STOCK = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "stock");
    public static final ResourceLocation TAB_GRIP = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "grip");
    public static final ResourceLocation TAB_EXTENDED_MAG = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "extended_mag");
    public static final ResourceLocation TAB_LASER = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "laser");
    public static final ResourceLocation TAB_MISC = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "misc");
    public static final ResourceLocation TAB_EMPTY = ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "empty");

    public static final List<TabConfig> DEFAULT_TABS = List.of(
            new TabConfig(TabConfig.TAB_AMMO, "tacz.type.ammo.name", new ItemData(AmmoItemBuilder.create().setId(DefaultAssets.DEFAULT_AMMO_ID).build())),
            new TabConfig(TabConfig.TAB_PISTOL, "tacz.type.pistol.name", new ItemData(GunItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "glock_17")).forceBuild())),
            new TabConfig(TabConfig.TAB_SNIPER, "tacz.type.sniper.name", new ItemData(GunItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "ai_awp")).forceBuild())),
            new TabConfig(TabConfig.TAB_RIFLE, "tacz.type.rifle.name", new ItemData(GunItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "ak47")).forceBuild())),
            new TabConfig(TabConfig.TAB_SHOTGUN, "tacz.type.shotgun.name", new ItemData(GunItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "db_short")).forceBuild())),
            new TabConfig(TabConfig.TAB_SMG, "tacz.type.smg.name", new ItemData(GunItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "hk_mp5a5")).forceBuild())),
            new TabConfig(TabConfig.TAB_RPG, "tacz.type.rpg.name", new ItemData(GunItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "rpg7")).forceBuild())),
            new TabConfig(TabConfig.TAB_MG, "tacz.type.mg.name", new ItemData(GunItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "m249")).forceBuild())),
            new TabConfig(TabConfig.TAB_SCOPE, "tacz.type.scope.name",  new ItemData(AttachmentItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "scope_acog_ta31")).build())),
            new TabConfig(TabConfig.TAB_MUZZLE, "tacz.type.muzzle.name", new ItemData(AttachmentItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "muzzle_compensator_trident")).build())),
            new TabConfig(TabConfig.TAB_STOCK, "tacz.type.stock.name", new ItemData(AttachmentItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "stock_militech_b5")).build())),
            new TabConfig(TabConfig.TAB_GRIP, "tacz.type.grip.name", new ItemData(AttachmentItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "grip_magpul_afg_2")).build())),
            new TabConfig(TabConfig.TAB_EXTENDED_MAG, "tacz.type.extended_mag.name", new ItemData(AttachmentItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "extended_mag_3")).build())),
            new TabConfig(TabConfig.TAB_LASER, "tacz.type.laser.name", new ItemData(AttachmentItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath(GunMod.MOD_ID, "laser_compact")).build())),
            new TabConfig(TabConfig.TAB_MISC, "tacz.type.misc.name", new ItemData(ModItems.GUN_SMITH_TABLE.get().getDefaultInstance()))
    );

    public static class Deserializer implements JsonDeserializer<TabConfig> {
        @Override
        public TabConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return CODEC.parse(JsonOps.INSTANCE, json)
                    .getOrThrow(msg -> new JsonParseException("Failed to parse TabConfig: " + msg));
        }
    }

    @NotNull
    public Component getName() {
        return Component.translatable(name==null ? "tacz.type.unknown.name" : name);
    }


    public static class ItemData{
        public static final Map<String,DataComponentType<ResourceLocation>> FIXER = new HashMap<>();
        public static final Codec<ItemData> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("item").forGetter(ItemData::id),
                        DataComponentPatch.CODEC.optionalFieldOf("components").forGetter(ItemData::components),
                        CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(ItemData::nbt)
                ).apply(instance, ItemData::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ItemData> STREAM_CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC,
                ItemData::itemStack,
                ItemData::new
        );

        static{
            FIXER.put("AmmoId", AmmoComponents.AMMO_ID.get());
            FIXER.put("AttachmentId", AccessoryComponents.ACCESSORY_ID.get());
            FIXER.put("GunId", GunComponents.GUN_ID.get());
        }

        private final ItemStack stack;

        public ItemData(ItemStack stack) {
            this.stack = stack;
        }

        public ItemData(ResourceLocation id, Optional<DataComponentPatch> components, Optional<CompoundTag> tag) {
            this.stack = new ItemStack(BuiltInRegistries.ITEM.get(id));
            components.ifPresent(dataComponentPatch -> PatchedDataComponentMap.fromPatch(this.stack.getComponents(), dataComponentPatch));
            if(tag.isPresent()) {
                CompoundTag nbt = tag.get();
                for (Map.Entry<String,DataComponentType<ResourceLocation>> entry : FIXER.entrySet()) {
                    if(nbt.contains(entry.getKey())) {
                        stack.set(entry.getValue(),ResourceLocation.parse(nbt.getString(entry.getKey())));
                    }
                }
            }
        }

        public Optional<CompoundTag> nbt(){
            return Optional.empty();
        }

        public Optional<DataComponentPatch> components(){
            return Optional.of(stack.getComponentsPatch());
        }

        public ResourceLocation id(){
            return BuiltInRegistries.ITEM.getKey(stack.getItem());
        }

        public ItemStack itemStack(){
            return stack;
        }
    }
}
