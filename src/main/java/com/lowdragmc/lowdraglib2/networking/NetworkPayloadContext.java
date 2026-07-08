package com.lowdragmc.lowdraglib2.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class NetworkPayloadContext {
    private final NetworkEvent.Context context;

    public NetworkPayloadContext(NetworkEvent.Context context) {
        this.context = context;
    }

    public Player player() {
        var sender = context.getSender();
        return sender == null ? Minecraft.getInstance().player : sender;
    }

    public NetworkEvent.Context forgeContext() {
        return context;
    }
}
