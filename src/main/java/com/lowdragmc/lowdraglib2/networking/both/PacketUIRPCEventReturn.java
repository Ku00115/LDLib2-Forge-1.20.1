package com.lowdragmc.lowdraglib2.networking.both;

import com.lowdragmc.lowdraglib2.gui.sync.IUISyncManagerHolder;
import com.lowdragmc.lowdraglib2.networking.NetworkPayloadContext;
import com.lowdragmc.lowdraglib2.utils.ByteBufUtil;
import lombok.NoArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import com.lowdragmc.lowdraglib2.utils.codec.StreamCodec;

@NoArgsConstructor
public class PacketUIRPCEventReturn {
    public static final StreamCodec<FriendlyByteBuf, PacketUIRPCEventReturn> CODEC = StreamCodec.ofMember(PacketUIRPCEventReturn::write, PacketUIRPCEventReturn::decode);

    public byte[] returnData;

    public PacketUIRPCEventReturn(byte[] returnData) {
        this.returnData = returnData;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeByteArray(returnData);
    }

    public static PacketUIRPCEventReturn decode(FriendlyByteBuf buf) {
        var returnData = buf.readByteArray();
        return new PacketUIRPCEventReturn(returnData);
    }

    public static void execute(PacketUIRPCEventReturn packet, NetworkPayloadContext context) {
        var player = context.player();
        if (player.containerMenu instanceof IUISyncManagerHolder syncManagerHolder) {
            var syncManager = syncManagerHolder.getSyncManager();
            if (syncManager == null) return;
            ByteBufUtil.readCustomData(packet.returnData,
                    syncManager::handEventReturn,
                    context.player().level().registryAccess());
        }
    }

}

