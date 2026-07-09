package com.lowdragmc.lowdraglib2.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

// Common-side holder block used by client preview renderers.
public class RendererBlock extends Block implements EntityBlock {

    public static final RendererBlock BLOCK = new RendererBlock();

    private RendererBlock() {
        super(Properties.of().noOcclusion().destroyTime(2));
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RendererBlockEntity(pos, state);
    }
}
