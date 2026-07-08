package com.lowdragmc.lowdraglib2.utils;

public enum TriState {
    TRUE,
    FALSE,
    DEFAULT;

    public boolean orElse(boolean fallback) {
        return switch (this) {
            case TRUE -> true;
            case FALSE -> false;
            case DEFAULT -> fallback;
        };
    }
}
