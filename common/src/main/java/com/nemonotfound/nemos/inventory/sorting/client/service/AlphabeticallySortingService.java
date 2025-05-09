package com.nemonotfound.nemos.inventory.sorting.client.service;

import com.nemonotfound.nemos.inventory.sorting.client.model.SlotItem;
import net.minecraft.client.Minecraft;

import java.util.Comparator;

public class AlphabeticallySortingService extends AbstractSortingService {

    private static AlphabeticallySortingService INSTANCE;

    public AlphabeticallySortingService(InventorySwapService inventorySwapService, Minecraft minecraft) {
        super(inventorySwapService, minecraft);
    }

    public static AlphabeticallySortingService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AlphabeticallySortingService(InventorySwapService.getInstance(), Minecraft.getInstance());
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
