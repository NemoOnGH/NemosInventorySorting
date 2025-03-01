package com.nemonotfound.nemos.inventory.sorting;

import com.nemonotfound.nemos.inventory.sorting.client.ModKeyMappings;
import com.nemonotfound.nemos.inventory.sorting.client.config.DefaultConfigs;

public class NemosInventorySortingClientCommon {

    public static void init() {
        ModKeyMappings.init();
        DefaultConfigs.getInstance().setupDefaultConfigs();
    }
}