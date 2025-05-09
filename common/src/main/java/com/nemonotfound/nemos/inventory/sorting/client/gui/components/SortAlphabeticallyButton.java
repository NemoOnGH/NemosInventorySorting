package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import com.nemonotfound.nemos.inventory.sorting.client.service.AlphabeticallySortingService;
import com.nemonotfound.nemos.inventory.sorting.client.service.InventoryMergeService;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

public class SortAlphabeticallyButton extends AbstractSortButton {

    private final ResourceLocation buttonTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "sort_button_alphabetically_inc");
    private final ResourceLocation buttonHoverTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "sort_button_alphabetically_inc_highlighted");

    public SortAlphabeticallyButton(Builder<SortAlphabeticallyButton> builder) {
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
        sortItemsAlphabetically();
    }

    private void sortItemsAlphabetically() {
        var minecraft = Minecraft.getInstance();
        var menu = containerScreen.getMenu();
        var containerId = menu.containerId;
        var sortingService = AlphabeticallySortingService.getInstance();
        var inventoryMergeService = InventoryMergeService.getInstance();

        var sortedSlotItems = sortingService.sortSlotItems(menu, startIndex, calculateEndIndex(menu));
        inventoryMergeService.mergeAllItems(containerScreen, sortedSlotItems, menu, containerId, minecraft);
        var sortedSlotItemsAfterMerge = sortingService.sortSlotItems(menu, startIndex, calculateEndIndex(menu));
        var slotSwapMap = sortingService.retrieveSlotSwapMap(sortedSlotItemsAfterMerge, startIndex);
        sortingService.sortItems(containerScreen, slotSwapMap, minecraft, containerId);
    }
}
