package com.nemonotfound.nemos.inventory.sorting;

import com.nemonotfound.nemos.inventory.sorting.client.ModKeyMappings;
import com.nemonotfound.nemos.inventory.sorting.platform.RegistryHelper;

import java.util.ServiceLoader;

public class NemosInventorySortingCommon {

    public static final RegistryHelper REGISTRY_HELPER = ServiceLoader.load(RegistryHelper.class).findFirst().orElseThrow();

    public static void init() {
        Constants.LOG.info("Thank you for using Nemo's Inventory Sorting!");

        ModKeyMappings.init();
    }
}