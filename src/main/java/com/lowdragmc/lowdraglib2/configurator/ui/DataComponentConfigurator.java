package com.lowdragmc.lowdraglib2.configurator.ui;

/**
 * Minecraft 1.20.1 predates the DataComponent item system. Item and fluid stack
 * configurators use NBT tag editing on this port, so this class remains only as
 * a binary/source compatibility placeholder for integrations compiled against
 * newer LDLib2 sources.
 */
public class DataComponentConfigurator extends ConfiguratorGroup {
    public DataComponentConfigurator() {
        super("configurator.data_component");
    }
}
