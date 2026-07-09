package com.lowdragmc.lowdraglib2.gui.factory;

import com.lowdragmc.lowdraglib2.LDLib2;
import com.lowdragmc.lowdraglib2.gui.event.ContainerMenuEvent;
import com.lowdragmc.lowdraglib2.gui.sync.IUISyncManagerHolder;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.function.Consumer;

public final class LDMenuNetworkHooks {
    private static Field playChannelField;
    private static Constructor<?> openContainerConstructor;

    private LDMenuNetworkHooks() {
    }

    public static boolean openScreen(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraDataWriter) {
        if (player.level().isClientSide) return false;

        player.doCloseContainer();
        player.nextContainerCounter();
        int containerId = player.containerCounter;

        FriendlyByteBuf extraData = new FriendlyByteBuf(Unpooled.buffer());
        extraDataWriter.accept(extraData);

        AbstractContainerMenu menu = provider.createMenu(containerId, player.getInventory(), player);
        if (menu == null) {
            extraData.release();
            return false;
        }

        MinecraftForge.EVENT_BUS.post(new ContainerMenuEvent.Create(player, menu));
        if (menu instanceof IUISyncManagerHolder syncManagerHolder) {
            syncManagerHolder.writeInitialData(extraData);
        }
        extraData.readerIndex(0);

        FriendlyByteBuf output = new FriendlyByteBuf(Unpooled.buffer());
        output.writeVarInt(extraData.readableBytes());
        output.writeBytes(extraData);
        extraData.release();

        if (output.readableBytes() > 32600 || output.readableBytes() < 1) {
            output.release();
            throw new IllegalArgumentException("Invalid PacketBuffer for openGui, found " + output.readableBytes() + " bytes");
        }

        sendOpenContainer(menu.getType(), containerId, provider.getDisplayName(), output, player);
        player.containerMenu = menu;
        player.initMenu(menu);
        MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, menu));
        return true;
    }

    private static void sendOpenContainer(MenuType<?> menuType, int containerId, Component title, FriendlyByteBuf data, ServerPlayer player) {
        try {
            var message = openContainerConstructor().newInstance(menuType, containerId, title, data);
            playChannel().sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        } catch (ReflectiveOperationException e) {
            data.release();
            LDLib2.LOGGER.error("Failed to open LDLib2 menu {}", menuType, e);
            throw new IllegalStateException("Could not access Forge open-container networking", e);
        }
    }

    private static SimpleChannel playChannel() throws ReflectiveOperationException {
        if (playChannelField == null) {
            var field = Class.forName("net.minecraftforge.network.NetworkConstants").getDeclaredField("playChannel");
            field.setAccessible(true);
            playChannelField = field;
        }
        return (SimpleChannel) playChannelField.get(null);
    }

    private static Constructor<?> openContainerConstructor() throws ReflectiveOperationException {
        if (openContainerConstructor == null) {
            var constructor = Class.forName("net.minecraftforge.network.PlayMessages$OpenContainer")
                    .getDeclaredConstructor(MenuType.class, int.class, Component.class, FriendlyByteBuf.class);
            constructor.setAccessible(true);
            openContainerConstructor = constructor;
        }
        return openContainerConstructor;
    }
}
