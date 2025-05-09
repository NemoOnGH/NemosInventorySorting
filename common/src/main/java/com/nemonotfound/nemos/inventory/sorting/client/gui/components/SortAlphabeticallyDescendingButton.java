package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import com.nemonotfound.nemos.inventory.sorting.client.service.AlphabeticallyDescendingSortingService;
import com.nemonotfound.nemos.inventory.sorting.client.service.InventoryMergeService;
import net.minecraft.resources.ResourceLocation;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

public class SortAlphabeticallyDescendingButton extends AbstractSortButton {

    private final ResourceLocation buttonTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "sort_button_alphabetically_dec");
    private final ResourceLocation buttonHoverTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "sort_button_alphabetically_dec_highlighted");

    public SortAlphabeticallyDescendingButton(Builder<SortAlphabeticallyDescendingButton> builder) {
        super(builder);
    }

    @Override
    protected ResourceLocation getButtonHoverTexture() {
        return buttonHoverTexture;
    }

    @Override
    protected ResourceLocation getButtonTexture() {
        return buttonTexture;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        sortItemsAlphabeticallyDescending();
    }

    private void sortItemsAlphabeticallyDescending() {
        var menu = containerScreen.getMenu();
        var containerId = menu.containerId;
        var sortingService = AlphabeticallyDescendingSortingService.getInstance();
        var inventoryMergeService = InventoryMergeService.getInstance();

        var sortedSlotItems = sortingService.sortSlotItems(menu, startIndex, calculateEndIndex(menu));
        inventoryMergeService.mergeAllItems(containerScreen, sortedSlotItems, menu, containerId);
        var sortedSlotItemsAfterMerge = sortingService.sortSlotItems(menu, startIndex, calculateEndIndex(menu));
        var slotSwapMap = sortingService.retrieveSlotSwapMap(sortedSlotItemsAfterMerge, startIndex);
        sortingService.sortItemsInInventory(containerScreen, slotSwapMap, containerId);
    }
}
