package com.lowdragmc.lowdraglib2.core.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.lowdragmc.lowdraglib2.LDLib2;
import com.lowdragmc.lowdraglib2.client.renderer.IBlockRendererProvider;
import com.lowdragmc.lowdraglib2.common.block.RendererBlock;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

/**
 * @author KilaBash
 * @date 2022/05/28
 */
@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Shadow abstract UnbakedModel getModel(ResourceLocation modelPath);

    @ModifyExpressionValue(method = "registerModelAndLoadDependencies",
                           at = @At(value = "INVOKE",
                                    target = "Lnet/minecraft/client/resources/model/UnbakedModel;getDependencies()Ljava/util/Collection;"))
    protected Collection<ResourceLocation> ldlib2$changeLoadedModel(Collection<ResourceLocation> original,
                                                                   @Local(argsOnly = true) ModelResourceLocation modelResourceLocation,
                                                                   @Local(argsOnly = true) LocalRef<UnbakedModel> model) {
        if (!modelResourceLocation.getVariant().equals("standalone")) {
            ResourceLocation resourceLocation = new ResourceLocation(modelResourceLocation.getNamespace(), modelResourceLocation.getPath());
            var block = BuiltInRegistries.BLOCK.get(resourceLocation);
            if (block instanceof IBlockRendererProvider || block == RendererBlock.BLOCK) {
                UnbakedModel newModel = getModel(LDLib2.id("block/renderer_model"));
                model.set(newModel);
                return newModel.getDependencies();
            }
        }
        return original;
    }
}
