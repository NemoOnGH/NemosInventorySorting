package com.nemonotfound.nemos.inventory.sorting.client.service.sorting;

import com.nemonotfound.nemos.inventory.sorting.client.model.SlotItem;
import com.nemonotfound.nemos.inventory.sorting.client.service.SlotSwappingService;
import net.minecraft.client.Minecraft;

import java.util.Comparator;

public class AlphabeticallyDescendingSortingService extends AbstractSortingService {

    private static AlphabeticallyDescendingSortingService INSTANCE;

    private AlphabeticallyDescendingSortingService(SlotSwappingService inventorySwapService, Minecraft minecraft) {
        super(inventorySwapService, minecraft);
    }

    public static AlphabeticallyDescendingSortingService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AlphabeticallyDescendingSortingService(SlotSwappingService.getInstance(), Minecraft.getInstance());
        }

        return INSTANCE;
    }

    @Override
    protected Comparator<SlotItem> comparator() {
        Comparator<SlotItem> comparator = Comparator.comparing(
                slotItem -> slotItem.itemStack()
                        .getItemName()
                        .getString()
        );

        return comparator.reversed();
    }
}
