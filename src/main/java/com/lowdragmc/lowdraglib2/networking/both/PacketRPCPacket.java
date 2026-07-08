package com.lowdragmc.lowdraglib2.networking.both;

import com.lowdragmc.lowdraglib2.LDLib2;
import com.lowdragmc.lowdraglib2.networking.NetworkPayloadContext;
import com.lowdragmc.lowdraglib2.networking.rpc.RPCPacketDistributor;
import com.lowdragmc.lowdraglib2.syncdata.rpc.RPCSender;
import lombok.NoArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import com.lowdragmc.lowdraglib2.utils.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

/**
 * a packet that contains payload for managed fields
 */
@NoArgsConstructor
public class PacketRPCPacket {
    public static final StreamCodec<FriendlyByteBuf, PacketRPCPacket> CODEC = StreamCodec.ofMember(PacketRPCPacket::write, PacketRPCPacket::decode);

    private String packetID;

    private byte[] data;

    public PacketRPCPacket(String packetID, byte[] data) {
        this.packetID = packetID;
        this.data = data;
    }

    public static PacketRPCPacket of(String packetID, byte[] data) {
        return new PacketRPCPacket(packetID, data);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(packetID);
        buf.writeByteArray(data);
    }

    public static PacketRPCPacket decode(FriendlyByteBuf buffer) {
        var packetID = buffer.readUtf();
        var data = buffer.readByteArray();
        return new PacketRPCPacket(packetID, data);
    }

    public static void execute(PacketRPCPacket packet, NetworkPayloadContext context) {
        if (context.player() instanceof ServerPlayer) {
            executeServer(packet, context);
        } else {
            executeClient(packet, context);
        }
    }

    public static void executeClient(PacketRPCPacket packet, NetworkPayloadContext context) {
        if (context.player().level() == null) {
            return;
        }
        var handler = RPCPacketDistributor.getPacketHandler(packet.packetID);
        if (handler == null) {
            LDLib2.LOGGER.warn("Received rpc payload packet from server of a non registered handler: {}, which is an inconsistency between client and server.",
                    packet.packetID);
            return;
        }
        var sender = RPCSender.ofServer();
        var data = handler.bytes2Args(packet.data);
        handler.handler(sender, data);
    }

    public static void executeServer(PacketRPCPacket packet, NetworkPayloadContext context) {
        var player = context.player();
        if (!(player instanceof ServerPlayer serverPlayer)) {
            LDLib2.LOGGER.error("Received rpc payload packet from client with no server player!");
            return;
        }

        var handler = RPCPacketDistributor.getPacketHandler(packet.packetID);
        if (handler == null) {
            LDLib2.LOGGER.warn("Received rpc payload packet from client sender {} of a non registered handler: {}, which may be an inconsistency between client and server, or even a potential attack!",
                    serverPlayer, packet.packetID);
            return;
        }
        var sender = RPCSender.ofClient(serverPlayer);
        var data = handler.bytes2Args(packet.data);
        handler.handler(sender, data);
    }

}

