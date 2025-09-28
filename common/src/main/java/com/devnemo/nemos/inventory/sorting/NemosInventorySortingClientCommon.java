package com.devnemo.nemos.inventory.sorting;

import com.devnemo.nemos.inventory.sorting.client.InventorySortingCategories;
import com.devnemo.nemos.inventory.sorting.client.InventorySortingKeyMappings;
import com.devnemo.nemos.inventory.sorting.config.DefaultConfigs;
import com.devnemo.nemos.inventory.sorting.config.service.ConfigService;
import com.devnemo.nemos.inventory.sorting.helper.SortOrder;
import com.devnemo.nemos.inventory.sorting.platform.IModLoaderHelper;
import com.devnemo.nemos.inventory.sorting.platform.IRegistryHelper;

import java.util.ServiceLoader;

import static com.devnemo.nemos.inventory.sorting.config.DefaultConfigValues.*;
import static com.devnemo.nemos.inventory.sorting.config.DefaultConfigs.*;

public class NemosInventorySortingClientCommon {

    public static final IRegistryHelper REGISTRY_HELPER = ServiceLoader.load(IRegistryHelper.class).findFirst().orElseThrow();
    public static final IModLoaderHelper MOD_LOADER_HELPER = ServiceLoader.load(IModLoaderHelper.class).findFirst().orElseThrow();

    public static void init() {
        Constants.LOG.info("Thank you for using Nemo's Inventory Sorting!");
        InventorySortingCategories.init();
        InventorySortingKeyMappings.init();
        DefaultConfigs.setupDefaultConfigs();

        ConfigService.getInstance().writeConfig(false, COMPONENT_CONFIG_PATH, DEFAULT_COMPONENT_CONFIGS);
        ConfigService.getInstance().writeConfig(false, FILTER_CONFIG_PATH, DEFAULT_FILTER_CONFIG);

        if (MOD_LOADER_HELPER.isModLoaded("ironchest")) {
            ConfigService.getInstance().writeConfig(false, IRON_CHEST_COMPONENT_CONFIG_PATH, DEFAULT_IRON_CHEST_COMPONENT_CONFIGS);
        }

        SortOrder.init();
    }
}