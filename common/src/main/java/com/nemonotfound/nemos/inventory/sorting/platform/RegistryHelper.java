package com.nemonotfound.nemos.inventory.sorting.platform;

import net.minecraft.client.KeyMapping;

import java.util.function.Supplier;

public interface RegistryHelper {

    Supplier<KeyMapping> registerKeyMapping(KeyMapping keyMapping);
}
