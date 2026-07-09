package com.lowdragmc.lowdraglib2.core.mixins.ui;

import com.lowdragmc.lowdraglib2.gui.event.ContainerMenuEvent;
import com.lowdragmc.lowdraglib2.gui.holder.IModularUIHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MenuType.class)
public abstract class MenuTypeMixin<T extends AbstractContainerMenu> {

    @Unique
    private static final ThreadLocal<Boolean> ldlib2$delegatedCreate = ThreadLocal.withInitial(() -> false);
    @Unique
    private static final ThreadLocal<Boolean> ldlib2$creatingWithExtraData = ThreadLocal.withInitial(() -> false);

    @Inject(method = "create(ILnet/minecraft/world/entity/player/Inventory;)Lnet/minecraft/world/inventory/AbstractContainerMenu;",
            at = @At(value = "RETURN"))
    private void ldlib2$create1(int containerId, Inventory playerInventory, CallbackInfoReturnable<T> cir) {
        var menu = cir.getReturnValue();
        if (menu != null) {
            if (ldlib2$creatingWithExtraData.get()) {
                ldlib2$delegatedCreate.set(true);
            }
            MinecraftForge.EVENT_BUS.post(new ContainerMenuEvent.Create(playerInventory.player, menu));
        }
    }


    @Inject(method = "create(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/network/FriendlyByteBuf;)Lnet/minecraft/world/inventory/AbstractContainerMenu;",
            at = @At(value = "HEAD"))
    private void ldlib2$create2$head(int containerId, Inventory playerInventory, FriendlyByteBuf extraData, CallbackInfoReturnable<T> cir) {
        ldlib2$creatingWithExtraData.set(true);
        ldlib2$delegatedCreate.set(false);
    }

    @Inject(method = "create(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/network/FriendlyByteBuf;)Lnet/minecraft/world/inventory/AbstractContainerMenu;",
            at = @At(value = "RETURN"))
    private void ldlib2$create2$return(int containerId, Inventory playerInventory, FriendlyByteBuf extraData, CallbackInfoReturnable<T> cir) {
        var menu = cir.getReturnValue();
        if (menu != null && !ldlib2$delegatedCreate.get()) {
            MinecraftForge.EVENT_BUS.post(new ContainerMenuEvent.Create(playerInventory.player, menu));
        }
        ldlib2$delegatedCreate.remove();
        ldlib2$creatingWithExtraData.remove();
        if (menu instanceof IModularUIHolder holder) {
            holder.readInitialData(extraData);
        }
    }
}

