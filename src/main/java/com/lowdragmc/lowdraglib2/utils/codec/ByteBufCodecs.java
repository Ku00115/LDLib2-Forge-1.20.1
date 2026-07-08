package com.lowdragmc.lowdraglib2.utils.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class ByteBufCodecs {
    public static final StreamCodec<FriendlyByteBuf, Integer> VAR_INT = StreamCodec.of(FriendlyByteBuf::writeVarInt, FriendlyByteBuf::readVarInt);
    public static final StreamCodec<FriendlyByteBuf, Long> VAR_LONG = StreamCodec.of(FriendlyByteBuf::writeVarLong, FriendlyByteBuf::readVarLong);
    public static final StreamCodec<FriendlyByteBuf, Float> FLOAT = StreamCodec.of(FriendlyByteBuf::writeFloat, FriendlyByteBuf::readFloat);
    public static final StreamCodec<FriendlyByteBuf, Double> DOUBLE = StreamCodec.of(FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble);
    public static final StreamCodec<FriendlyByteBuf, Boolean> BOOL = StreamCodec.of(FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean);
    public static final StreamCodec<FriendlyByteBuf, Byte> BYTE = StreamCodec.of((buf, value) -> buf.writeByte(value), FriendlyByteBuf::readByte);
    public static final StreamCodec<FriendlyByteBuf, Short> SHORT = StreamCodec.of((buf, value) -> buf.writeShort(value), FriendlyByteBuf::readShort);
    public static final StreamCodec<FriendlyByteBuf, String> STRING_UTF8 = StreamCodec.of(FriendlyByteBuf::writeUtf, FriendlyByteBuf::readUtf);
    public static final StreamCodec<FriendlyByteBuf, ResourceLocation> RESOURCE_LOCATION = StreamCodec.of(FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::readResourceLocation);
    public static final StreamCodec<FriendlyByteBuf, BlockPos> BLOCK_POS = StreamCodec.of(FriendlyByteBuf::writeBlockPos, FriendlyByteBuf::readBlockPos);
    public static final StreamCodec<FriendlyByteBuf, FluidStack> FLUID_STACK = StreamCodec.of(
            (buf, value) -> value.writeToPacket(buf),
            FluidStack::readFromPacket
    );
    public static final StreamCodec<FriendlyByteBuf, ItemStack> ITEM_STACK = StreamCodec.of(
            FriendlyByteBuf::writeItem,
            FriendlyByteBuf::readItem
    );
    public static final StreamCodec<FriendlyByteBuf, Component> COMPONENT = StreamCodec.of(
            (buf, value) -> buf.writeUtf(Component.Serializer.toJson(value)),
            buf -> {
                var component = Component.Serializer.fromJson(buf.readUtf());
                return component == null ? Component.empty() : component;
            }
    );
    public static final StreamCodec<FriendlyByteBuf, Vector3f> VECTOR3F = StreamCodec.of(
            (buf, value) -> {
                buf.writeFloat(value.x);
                buf.writeFloat(value.y);
                buf.writeFloat(value.z);
            },
            buf -> new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat())
    );
    public static final StreamCodec<FriendlyByteBuf, Vector4f> VECTOR4F = StreamCodec.of(
            (buf, value) -> {
                buf.writeFloat(value.x);
                buf.writeFloat(value.y);
                buf.writeFloat(value.z);
                buf.writeFloat(value.w);
            },
            buf -> new Vector4f(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat())
    );
    public static final StreamCodec<FriendlyByteBuf, Quaternionf> QUATERNIONF = StreamCodec.of(
            (buf, value) -> {
                buf.writeFloat(value.x);
                buf.writeFloat(value.y);
                buf.writeFloat(value.z);
                buf.writeFloat(value.w);
            },
            buf -> new Quaternionf(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat())
    );
    public static final StreamCodec<FriendlyByteBuf, Tag> TRUSTED_TAG = StreamCodec.of(
            (buf, value) -> {
                var tag = new CompoundTag();
                tag.put("value", value);
                buf.writeNbt(tag);
            },
            buf -> {
                var tag = buf.readNbt();
                return tag == null ? new CompoundTag() : tag.get("value");
            }
    );

    private ByteBufCodecs() {
    }

    @SuppressWarnings("unchecked")
    public static <T> StreamCodec<FriendlyByteBuf, T> registry(ResourceKey<? extends Registry<T>> registryKey) {
        return StreamCodec.of(
                (buf, value) -> {
                    Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
                    buf.writeResourceLocation(registry.getKey(value));
                },
                buf -> {
                    Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
                    return registry.get(buf.readResourceLocation());
                }
        );
    }

    public static <T> StreamCodec<FriendlyByteBuf, T> fromCodec(Codec<T> codec) {
        return fromCodec(NbtOps.INSTANCE, codec);
    }

    public static <T> StreamCodec<FriendlyByteBuf, T> fromCodecWithRegistries(Codec<T> codec) {
        return fromCodec(codec);
    }

    public static <T> StreamCodec<FriendlyByteBuf, T> fromCodec(DynamicOps<Tag> ops, Codec<T> codec) {
        return StreamCodec.of(
                (buf, value) -> {
                    Tag encoded = codec.encodeStart(ops, value)
                            .getOrThrow(false, error -> {
                                throw new IllegalArgumentException("Failed to encode stream value: " + error);
                            });
                    var tag = new CompoundTag();
                    tag.put("value", encoded);
                    buf.writeNbt(tag);
                },
                buf -> {
                    var tag = buf.readNbt();
                    if (tag == null || !tag.contains("value")) {
                        throw new IllegalArgumentException("Missing encoded stream value");
                    }
                    DataResult<T> result = codec.parse(ops, tag.get("value"));
                    return result.getOrThrow(false, error -> {
                        throw new IllegalArgumentException("Failed to decode stream value: " + error);
                    });
                }
        );
    }
}
