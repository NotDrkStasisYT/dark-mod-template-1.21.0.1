package net.drk.Z28.d;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public final class DamageNumbers {

    public static @NotNull Logger LOGGER = LogManager.getLogger();

    private static DamageNumbers INSTANCE = null;

    public static @NotNull DamageNumbersHandler getHandler() {
        if (INSTANCE == null) {
            throw new IllegalStateException("DamageNumbers not initialized");
        }
        return INSTANCE.handler;
    }

    private final @NotNull DamageNumbersHandler handler;

    public DamageNumbers() {
        INSTANCE = this;
        handler = new DamageNumbersImpl(); // Initialize without config
    }
}
