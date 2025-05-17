package com.nemonotfound.nemos.inventory.sorting.client.gui.components.buttons.sorting;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.buttons.AbstractInventoryButton;
import com.nemonotfound.nemos.inventory.sorting.client.service.sorting.AlphabeticallyDescendingSortingService;
import com.nemonotfound.nemos.inventory.sorting.client.service.InventoryService;
import net.minecraft.resources.ResourceLocation;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

public class SortAlphabeticallyDescendingButton extends AbstractInventoryButton {

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
        var inventoryService = InventoryService.getInstance();
        var sortingService = AlphabeticallyDescendingSortingService.getInstance();
        var menu = containerScreen.getMenu();
        var endIndex = inventoryService.calculateEndIndex(isButtonShiftable(menu), this.endIndex);

        inventoryService.handleSorting(sortingService, menu, startIndex, endIndex);
    }
}
