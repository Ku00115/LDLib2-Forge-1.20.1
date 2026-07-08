package com.lowdragmc.lowdraglib2.networking;

import com.lowdragmc.lowdraglib2.LDLib2;
import com.lowdragmc.lowdraglib2.networking.both.PacketModularUISync;
import com.lowdragmc.lowdraglib2.networking.both.PacketRPCBlockEntity;
import com.lowdragmc.lowdraglib2.networking.both.PacketRPCPacket;
import com.lowdragmc.lowdraglib2.networking.both.PacketUIRPCEvent;
import com.lowdragmc.lowdraglib2.networking.both.PacketUIRPCEventReturn;
import com.lowdragmc.lowdraglib2.networking.s2c.SPacketAutoSyncBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Author: KilaBash
 * Date: 2022/04/27
 * Description:
 */
public class LDLNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(LDLib2.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int packetId;

    public static void init() {
        register(SPacketAutoSyncBlockEntity.class, SPacketAutoSyncBlockEntity::write, SPacketAutoSyncBlockEntity::decode, SPacketAutoSyncBlockEntity::execute, NetworkDirection.PLAY_TO_CLIENT);

        register(PacketUIRPCEvent.class, PacketUIRPCEvent::write, PacketUIRPCEvent::decode, PacketUIRPCEvent::execute, null);
        register(PacketUIRPCEventReturn.class, PacketUIRPCEventReturn::write, PacketUIRPCEventReturn::decode, PacketUIRPCEventReturn::execute, null);
        register(PacketRPCBlockEntity.class, PacketRPCBlockEntity::write, PacketRPCBlockEntity::decode, PacketRPCBlockEntity::execute, null);
        register(PacketModularUISync.class, PacketModularUISync::write, PacketModularUISync::decode, PacketModularUISync::execute, null);
        register(PacketRPCPacket.class, PacketRPCPacket::write, PacketRPCPacket::decode, PacketRPCPacket::execute, null);
    }

    private static <T> void register(Class<T> type,
                                     BiConsumer<T, FriendlyByteBuf> encoder,
                                     Function<FriendlyByteBuf, T> decoder,
                                     BiConsumer<T, NetworkPayloadContext> handler,
                                     NetworkDirection direction) {
        var builder = direction == null
                ? CHANNEL.messageBuilder(type, packetId++)
                : CHANNEL.messageBuilder(type, packetId++, direction);
        builder.encoder(encoder)
                .decoder(decoder)
                .consumerMainThread((packet, contextSupplier) -> handle(packet, contextSupplier, handler))
                .add();
    }

    private static <T> void handle(T packet, Supplier<NetworkEvent.Context> contextSupplier, BiConsumer<T, NetworkPayloadContext> handler) {
        var context = contextSupplier.get();
        handler.accept(packet, new NetworkPayloadContext(context));
        context.setPacketHandled(true);
    }

    public static void sendToServer(Object packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToPlayer(Object packet, net.minecraft.server.level.ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static void sendToAll(Object packet) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }

    public static void sendToTrackingChunk(Object packet, net.minecraft.server.level.ServerLevel level, net.minecraft.world.level.ChunkPos chunkPos) {
        var chunk = level.getChunk(chunkPos.x, chunkPos.z);
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), packet);
    }

}
