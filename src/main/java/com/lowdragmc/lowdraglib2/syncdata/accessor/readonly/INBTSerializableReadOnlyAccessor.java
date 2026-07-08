package com.lowdragmc.lowdraglib2.syncdata.accessor.readonly;

import com.lowdragmc.lowdraglib2.core.mixins.accessor.DelegatingOpsAccessor;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class INBTSerializableReadOnlyAccessor implements IReadOnlyAccessor<INBTSerializable<?>> {

    @Override
    public boolean test(Class<?> type) {
        return INBTSerializable.class.isAssignableFrom(type);
    }

    @Override
    public <T> T readReadOnlyValue(DynamicOps<T> op, @NotNull INBTSerializable<?> value) {
        var tag = value.serializeNBT();
        return (op == NbtOps.INSTANCE || op instanceof DelegatingOpsAccessor<?> accessor && accessor.getDelegate() == NbtOps.INSTANCE) ? (T) tag : NbtOps.INSTANCE.convertTo(op, tag);
    }

    @Override
    public <T> void writeReadOnlyValue(DynamicOps<T> op, INBTSerializable<?> value, T payload) {
        ((INBTSerializable)value).deserializeNBT(
                (op == NbtOps.INSTANCE || op instanceof DelegatingOpsAccessor<?> accessor && accessor.getDelegate() == NbtOps.INSTANCE) ?
                        (Tag) payload : op.convertTo(NbtOps.INSTANCE, payload));
    }

    @Override
    public void readReadOnlyValueToStream(FriendlyByteBuf buffer, @NotNull INBTSerializable<?> value) {
        var tag = new CompoundTag();
        tag.put("value", value.serializeNBT());
        buffer.writeNbt(tag);
    }

    @Override
    public void writeReadOnlyValueFromStream(FriendlyByteBuf buffer, @NotNull INBTSerializable<?> value) {
        var wrapper = buffer.readNbt(NbtAccounter.UNLIMITED);
        var nbt = wrapper == null ? null : wrapper.get("value");
        if (nbt != null) {
            ((INBTSerializable)value).deserializeNBT(nbt);
        }
    }

}

