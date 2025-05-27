package com.nemonotfound.nemos.inventory.sorting;

import com.nemonotfound.nemos.inventory.sorting.client.ModKeyMappings;
import com.nemonotfound.nemos.inventory.sorting.client.config.DefaultConfigs;
import com.nemonotfound.nemos.inventory.sorting.platform.IRegistryHelper;

import java.util.ServiceLoader;

public class NemosInventorySortingClientCommon {

    public static final IRegistryHelper REGISTRY_HELPER = ServiceLoader.load(IRegistryHelper.class).findFirst().orElseThrow();

    public static void init() {
        Constants.LOG.info("Thank you for using Nemo's Inventory Sorting!");
        ModKeyMappings.init();
        DefaultConfigs.getInstance().setupDefaultConfigs();
    }
}