package com.nemonotfound.nemos.inventory.sorting.service;

import com.nemonotfound.nemos.inventory.sorting.service.sorting.AbstractSortingService;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class InventoryService {

    private static InventoryService INSTANCE;

    private final MergingService inventoryMergeService;

    private InventoryService(MergingService inventoryMergeService) {
        this.inventoryMergeService = inventoryMergeService;
    }

    public static InventoryService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new InventoryService(MergingService.getInstance());
        }

        return INSTANCE;
    }

    public void handleSorting(AbstractSortingService sortingService, AbstractContainerMenu menu, int startIndex, int endIndex) {
        var containerId = menu.containerId;

        var sortedSlotItems = sortingService.sortSlotItems(menu, startIndex, endIndex);
        inventoryMergeService.mergeAllItems(menu, sortedSlotItems, containerId);
        var sortedSlotItemsAfterMerge = sortingService.sortSlotItems(menu, startIndex, endIndex);
        var slotSwapMap = sortingService.retrieveSlotSwapMap(sortedSlotItemsAfterMerge, startIndex);
        sortingService.sortItemsInInventory(menu, slotSwapMap, containerId);
    }

    public int calculateEndIndex(boolean shouldIncludeHotbar, int endIndex) {
        if (shouldIncludeHotbar) {
            return endIndex + 9;
        }

        return endIndex;
    }
}
