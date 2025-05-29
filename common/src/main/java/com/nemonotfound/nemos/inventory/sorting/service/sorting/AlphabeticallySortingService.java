package com.nemonotfound.nemos.inventory.sorting.service.sorting;

import com.nemonotfound.nemos.inventory.sorting.model.SlotItem;
import com.nemonotfound.nemos.inventory.sorting.service.TooltipService;
import com.nemonotfound.nemos.inventory.sorting.service.SlotSwappingService;
import net.minecraft.client.Minecraft;

import java.util.Comparator;

public class AlphabeticallySortingService extends AbstractSortingService {

    private static AlphabeticallySortingService INSTANCE;

    private AlphabeticallySortingService(SlotSwappingService inventorySwapService, TooltipService tooltipService, Minecraft minecraft) {
        super(inventorySwapService, tooltipService, minecraft);
    }

    public static AlphabeticallySortingService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AlphabeticallySortingService(SlotSwappingService.getInstance(), TooltipService.getInstance(), Minecraft.getInstance());
        }

        return INSTANCE;
    }

    @Override
    protected Comparator<SlotItem> comparator() {
        return comparatorByName();
    }
}
