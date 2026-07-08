package com.lowdragmc.lowdraglib2.utils.function;

import java.util.function.Supplier;

public final class Suppliers {
    private Suppliers() {
    }

    public static <T> Supplier<T> nul() {
        return () -> null;
    }
}
