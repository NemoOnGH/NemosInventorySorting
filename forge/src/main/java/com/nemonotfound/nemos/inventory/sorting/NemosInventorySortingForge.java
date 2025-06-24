package com.nemonotfound.nemos.inventory.sorting;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

@Mod(MOD_ID)
public class NemosInventorySortingForge {

    public NemosInventorySortingForge() {
        if (FMLEnvironment.dist.isClient()) {
            NemosInventorySortingClientCommon.init();
        }
    }
}