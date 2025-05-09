package com.nemonotfound.nemos.inventory.sorting.client.service;

import com.nemonotfound.nemos.inventory.sorting.client.model.SlotItem;

import java.util.Comparator;

public class AlphabeticallyDescendingSortingService extends AbstractSortingService {

    private static AlphabeticallyDescendingSortingService INSTANCE;

    public AlphabeticallyDescendingSortingService(InventorySwapService inventorySwapService) {
        super(inventorySwapService);
    }

    public static AlphabeticallyDescendingSortingService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AlphabeticallyDescendingSortingService(InventorySwapService.getInstance());
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
