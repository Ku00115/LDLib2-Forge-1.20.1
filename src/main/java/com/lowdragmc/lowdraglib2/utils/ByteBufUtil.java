package com.lowdragmc.lowdraglib2.utils;

import io.netty.buffer.Unpooled;
import lombok.experimental.UtilityClass;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Consumer;

@UtilityClass
public final class ByteBufUtil {
    /**
     * Writes custom data to a {@link FriendlyByteBuf}, then read it for consumer.
     *
     * @param data           Data to write.
     * @param dataWriter     The data reader.
     * @param registryAccess The registry access used by registry dependent writers on the buffer
     */
    public static void readCustomData(byte[] data, Consumer<FriendlyByteBuf> dataWriter, RegistryAccess registryAccess) {
        final FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(data));
        try {
            dataWriter.accept(buf);
        } finally {
            buf.release();
        }
    }

    /**
     * Writes custom data to a {@link FriendlyByteBuf}, then returns the written data as a byte array.
     * This implementation avoids the vanilla byte-array size cap for larger managed sync payloads.
     *
     * @param dataWriter     The data writer.
     * @param registryAccess The registry access used by registry dependent writers on the buffer
     * @return The written data.
     */
    public static byte[] writeCustomData(Consumer<FriendlyByteBuf> dataWriter, RegistryAccess registryAccess) {
        final FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        try {
            dataWriter.accept(buf);
            buf.readerIndex(0);
            final byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            return data;
        } finally {
            buf.release();
        }
    }
}

