package com.nemonotfound.nemos.inventory.sorting;

import com.nemonotfound.nemos.inventory.sorting.config.DefaultConfigs;
import com.nemonotfound.nemos.inventory.sorting.config.service.ConfigService;
import com.nemonotfound.nemos.inventory.sorting.platform.IRegistryHelper;

import java.util.ServiceLoader;

import static com.nemonotfound.nemos.inventory.sorting.config.DefaultConfigValues.COMPONENT_CONFIG_PATH;
import static com.nemonotfound.nemos.inventory.sorting.config.DefaultConfigValues.FILTER_CONFIG_PATH;
import static com.nemonotfound.nemos.inventory.sorting.config.DefaultConfigs.DEFAULT_COMPONENT_CONFIGS;
import static com.nemonotfound.nemos.inventory.sorting.config.DefaultConfigs.DEFAULT_FILTER_CONFIG;

public class NemosInventorySortingClientCommon {

    public static final IRegistryHelper REGISTRY_HELPER = ServiceLoader.load(IRegistryHelper.class).findFirst().orElseThrow();

    public static void init() {
        Constants.LOG.info("Thank you for using Nemo's Inventory Sorting!");
        ModKeyMappings.init();
        DefaultConfigs.setupDefaultConfigs();
        ConfigService.getInstance().writeConfig(false, COMPONENT_CONFIG_PATH, DEFAULT_COMPONENT_CONFIGS);
        ConfigService.getInstance().writeConfig(false, FILTER_CONFIG_PATH, DEFAULT_FILTER_CONFIG);
    }
}