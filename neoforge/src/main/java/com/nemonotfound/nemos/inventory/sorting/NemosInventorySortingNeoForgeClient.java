package com.nemonotfound.nemos.inventory.sorting;


import com.nemonotfound.nemos.inventory.sorting.client.ModKeyMappings;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NemosInventorySortingNeoForgeClient {

    public NemosInventorySortingNeoForgeClient() {
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ModKeyMappings.DROP_ALL.get());
        event.register(ModKeyMappings.DROP_ALL_INVENTORY.get());
        event.register(ModKeyMappings.MOVE_ALL.get());
        event.register(ModKeyMappings.MOVE_ALL_INVENTORY.get());
        event.register(ModKeyMappings.MOVE_SAME.get());
        event.register(ModKeyMappings.MOVE_SAME_INVENTORY.get());
        event.register(ModKeyMappings.SORT_ALPHABETICALLY.get());
        event.register(ModKeyMappings.SORT_ALPHABETICALLY_INVENTORY.get());
        event.register(ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING.get());
        event.register(ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING_INVENTORY.get());
    }
}