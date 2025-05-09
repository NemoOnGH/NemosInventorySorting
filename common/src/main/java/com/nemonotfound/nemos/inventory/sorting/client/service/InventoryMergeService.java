package com.nemonotfound.nemos.inventory.sorting.client.service;

import com.nemonotfound.nemos.inventory.sorting.Constants;
import com.nemonotfound.nemos.inventory.sorting.client.model.SlotItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MAX_MERGING_CYCLES;
import static java.util.stream.Collectors.groupingBy;

public class InventoryMergeService {

    private static InventoryMergeService INSTANCE;
    private final InventorySwapService inventorySwapService;
    private final Minecraft minecraft;

    public InventoryMergeService(InventorySwapService inventorySwapService, Minecraft minecraft) {
        this.inventorySwapService = inventorySwapService;
        this.minecraft = minecraft;
    }

    public static InventoryMergeService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new InventoryMergeService(InventorySwapService.getInstance(), Minecraft.getInstance());
        }

        return INSTANCE;
    }

    public void mergeAllItems(AbstractContainerScreen<?> containerScreen, List<SlotItem> sortedSlotItems, AbstractContainerMenu menu, int containerId) {
        var groupedItemMap = sortedSlotItems.stream()
                .collect(groupingBy(slotItem -> slotItem.itemStack().getComponents()));

        groupedItemMap.forEach((key, slotItems) -> mergeItems(containerScreen, slotItems, menu, containerId));
    }

    private void mergeItems(AbstractContainerScreen<?> containerScreen, List<SlotItem> slotItems, AbstractContainerMenu menu, int containerId) {
        if (slotItems.size() <= 1) {
            return;
        }

        var leftSlotIndex = 0;
        var rightSlotIndex = slotItems.size() - 1;
        var remainingCycles = MAX_MERGING_CYCLES;

        while (leftSlotIndex < rightSlotIndex && remainingCycles-- > 0) {
            var leftSlotItem = slotItems.get(leftSlotIndex);
            var rightSlotItem = slotItems.get(rightSlotIndex);
            var leftSlot = menu.slots.get(leftSlotItem.slotIndex());
            var rightSlot = menu.slots.get(rightSlotItem.slotIndex());
            var leftItem = leftSlot.getItem();

            if (!isFullStack(leftItem)) {
                inventorySwapService.performSlotSwap(
                        containerScreen,
                        minecraft.gameMode,
                        containerId,
                        rightSlotItem.slotIndex(),
                        leftSlotItem.slotIndex(),
                        minecraft.player
                );
            } else {
                leftSlotIndex++;
            }

            if (rightSlot.getItem().isEmpty()) {
                rightSlotIndex--;
            }
        }

        if (remainingCycles <= 0) {
            Constants.LOG.warn("Merging items exceeded cycle limit. Please report this.");
        }
    }

    private boolean isFullStack(ItemStack itemStack) {
        return itemStack.getCount() >= itemStack.getMaxStackSize();
    }
}
