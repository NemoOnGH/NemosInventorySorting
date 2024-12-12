package com.nemonotfound.nemos.inventory.sorting;

import net.fabricmc.api.ModInitializer;

public class NemosInventorySortingFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        NemosInventorySortingCommon.init();
    }
}
