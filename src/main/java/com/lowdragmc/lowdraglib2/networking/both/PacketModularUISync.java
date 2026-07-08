package com.lowdragmc.lowdraglib2.networking.both;

import com.lowdragmc.lowdraglib2.gui.sync.IUISyncManagerHolder;
import com.lowdragmc.lowdraglib2.networking.NetworkPayloadContext;
import com.lowdragmc.lowdraglib2.utils.ByteBufUtil;
import lombok.NoArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import com.lowdragmc.lowdraglib2.utils.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

/**
 * a packet that contains payload for managed fields
 */
@NoArgsConstructor
public class PacketModularUISync {
    public static final StreamCodec<FriendlyByteBuf, PacketModularUISync> CODEC = StreamCodec.ofMember(PacketModularUISync::write, PacketModularUISync::decode);

    private byte[] data;

    public PacketModularUISync(byte[] data) {
        this.data = data;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeByteArray(data);
    }

    public static PacketModularUISync decode(FriendlyByteBuf buffer) {
        var data = buffer.readByteArray();
        return new PacketModularUISync(data);
    }

    public static void execute(PacketModularUISync packet, NetworkPayloadContext context) {
        if (context.player() instanceof ServerPlayer) {
            executeServer(packet, context);
        } else {
            executeClient(packet, context);
        }
    }

    public static void executeClient(PacketModularUISync packet, NetworkPayloadContext context) {
        var player = context.player();
        if (player.containerMenu instanceof IUISyncManagerHolder syncManagerHolder) {
            var syncManager = syncManagerHolder.getSyncManager();
            if (syncManager == null) return;
            ByteBufUtil.readCustomData(packet.data,
                    syncManager::handleSyncPacket,
                    context.player().level().registryAccess());
        }
    }

    public static void executeServer(PacketModularUISync packet, NetworkPayloadContext context) {
        var player = context.player();
        if (player.containerMenu instanceof IUISyncManagerHolder syncManagerHolder) {
            var syncManager = syncManagerHolder.getSyncManager();
            if (syncManager == null) return;
            ByteBufUtil.readCustomData(packet.data,
                    syncManager::handleSyncPacket,
                    context.player().level().registryAccess());
        }
    }

}

