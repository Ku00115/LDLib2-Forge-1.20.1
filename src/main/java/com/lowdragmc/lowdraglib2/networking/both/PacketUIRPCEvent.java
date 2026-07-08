package com.lowdragmc.lowdraglib2.networking.both;

import com.lowdragmc.lowdraglib2.gui.sync.IUISyncManagerHolder;
import com.lowdragmc.lowdraglib2.networking.NetworkPayloadContext;
import com.lowdragmc.lowdraglib2.utils.ByteBufUtil;
import lombok.NoArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import com.lowdragmc.lowdraglib2.utils.codec.StreamCodec;

@NoArgsConstructor
public class PacketUIRPCEvent {
    public static final StreamCodec<FriendlyByteBuf, PacketUIRPCEvent> CODEC = StreamCodec.ofMember(PacketUIRPCEvent::write, PacketUIRPCEvent::decode);
    public byte[] eventData;

    public PacketUIRPCEvent(byte[] eventData) {
        this.eventData = eventData;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeByteArray(eventData);
    }

    public static PacketUIRPCEvent decode(FriendlyByteBuf buf) {
        var eventData = buf.readByteArray();
        return new PacketUIRPCEvent(eventData);
    }

    public static void execute(PacketUIRPCEvent packet, NetworkPayloadContext context) {
        var player = context.player();
        if (player.containerMenu instanceof IUISyncManagerHolder syncManagerHolder) {
            var syncManager = syncManagerHolder.getSyncManager();
            if (syncManager == null) return;
            ByteBufUtil.readCustomData(packet.eventData,
                    syncManager::handEvent,
                    context.player().level().registryAccess());
        }
    }

}

