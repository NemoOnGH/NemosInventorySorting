package com.nemonotfound.nemos.inventory.sorting.client.service;

import com.nemonotfound.nemos.inventory.sorting.client.model.SlotItem;

import java.util.Comparator;

public class AlphabeticallySortingService extends AbstractSortingService {

    private static AlphabeticallySortingService INSTANCE;

    public AlphabeticallySortingService(InventorySwapService inventorySwapService) {
        super(inventorySwapService);
    }

    public static AlphabeticallySortingService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AlphabeticallySortingService(InventorySwapService.getInstance());
        }

        return INSTANCE;
    }

    @Override
    protected Comparator<SlotItem> comparator() {
        return Comparator.comparing(
                slotItem -> slotItem.itemStack()
                        .getItemName()
                        .getString()
        );
    }
}
