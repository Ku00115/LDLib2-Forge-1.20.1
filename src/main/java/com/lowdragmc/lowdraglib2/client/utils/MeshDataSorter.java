package com.lowdragmc.lowdraglib2.client.utils;

import org.jetbrains.annotations.Nullable;

/**
 * Placeholder for the Minecraft 1.21 MeshData sorting helper.
 * Minecraft 1.20.1 uses the older BufferBuilder.RenderedBuffer path, so this
 * helper is intentionally inert until the sorter is backported to that API.
 */
public final class MeshDataSorter {
    private MeshDataSorter() {
    }

    @Nullable
    public static MeshDataSortResult sortPrimitives(Object meshData, Object bufferBuilder, Object sorting) {
        return null;
    }
}
