package com.lowdragmc.lowdraglib2.utils.function;

import java.util.function.Consumer;

public final class Consumers {
    private Consumers() {
    }

    public static <T> Consumer<T> nop() {
        return value -> {
        };
    }
}
