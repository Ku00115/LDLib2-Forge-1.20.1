package com.lowdragmc.lowdraglib2.core.mixins;

import com.lowdragmc.lowdraglib2.Platform;
import com.lowdragmc.lowdraglib2.syncdata.holder.IManagedHolder;
import com.lowdragmc.lowdraglib2.syncdata.holder.IPersistManagedHolder;
import com.lowdragmc.lowdraglib2.syncdata.holder.ISyncMangedHolder;
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author KilaBash
 * @date 2022/11/27
 * @implNote BlockEntityMixin
 */
@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

    @Inject(method = {"getUpdateTag", "m_5995_"}, at = @At(value = "RETURN"))
    private void injectGetUpdateTag(CallbackInfoReturnable<CompoundTag> cir) {
        if (this instanceof ISyncMangedHolder syncMangedHolder) {
            var tag = cir.getReturnValue();
            tag.put(syncMangedHolder.getSyncTag(), syncMangedHolder.serializeInitialData(ldlib2$getRegistryProvider()));
        }
    }

    @Inject(method = {"saveAdditional", "m_183515_"}, at = @At(value = "RETURN"))
    private void injectSaveAdditional(CompoundTag pTag, CallbackInfo ci) {
        if (this instanceof IPersistManagedHolder persistManagedHolder) {
            persistManagedHolder.saveManagedPersistentData(ldlib2$getRegistryProvider(), pTag, false);
        }
    }

    @Inject(method = {"load", "m_142466_"}, at = @At(value = "RETURN"))
    private void injectLoad(CompoundTag pTag, CallbackInfo ci) {
        if (this instanceof ISyncMangedHolder syncMangedHolder && pTag.get(syncMangedHolder.getSyncTag()) instanceof CompoundTag tag) {
            syncMangedHolder.deserializeInitialData(ldlib2$getRegistryProvider(), tag);
        } else if (this instanceof IPersistManagedHolder persistManagedHolder) {
            persistManagedHolder.loadManagedPersistentData(ldlib2$getRegistryProvider(), pTag);
        }
    }

    @Inject(method = {"setRemoved", "m_7651_"}, at = @At(value = "RETURN"))
    private void injectSetRemoved(CallbackInfo ci) {
        if (this instanceof ISyncPersistRPCBlockEntity syncMangedHolder && ((BlockEntity) (Object) this).getLevel() instanceof ServerLevel) {
            syncMangedHolder.detachAsyncLogic();
        }
    }

    @Inject(method = {"clearRemoved", "m_6339_"}, at = @At(value = "RETURN"))
    private void injectClearRemoved(CallbackInfo ci) {
        if (this instanceof IManagedHolder managed) {
            managed.getRootStorage().requireInit();
            if (managed instanceof ISyncMangedHolder syncMangedHolder && ((BlockEntity) (Object) this).getLevel() instanceof ServerLevel) {
                syncMangedHolder.attachAsyncLogic();
            }
        }
    }

    private HolderLookup.Provider ldlib2$getRegistryProvider() {
        var level = ((BlockEntity) (Object) this).getLevel();
        return level == null ? Platform.getFrozenRegistry() : level.registryAccess();
    }

}
