package com.nemonotfound.nemos.inventory.sorting.platform;

import net.minecraft.client.KeyMapping;

import java.util.function.Supplier;

public class RegistryHelperForge implements RegistryHelper {

    @Override
    public Supplier<KeyMapping> registerKeyMapping(KeyMapping keyMapping) {
        return () -> keyMapping;
    }
}
