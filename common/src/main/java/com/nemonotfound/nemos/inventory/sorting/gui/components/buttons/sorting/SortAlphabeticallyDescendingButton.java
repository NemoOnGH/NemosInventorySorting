package com.nemonotfound.nemos.inventory.sorting.gui.components.buttons.sorting;

import com.nemonotfound.nemos.inventory.sorting.ModKeyMappings;
import com.nemonotfound.nemos.inventory.sorting.gui.components.buttons.AbstractInventoryButton;
import com.nemonotfound.nemos.inventory.sorting.service.sorting.AlphabeticallyDescendingSortingService;
import com.nemonotfound.nemos.inventory.sorting.service.InventoryService;
import net.minecraft.client.KeyMapping;
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
        var endIndex = inventoryService.calculateEndIndex(isButtonShiftable(), this.endIndex);

        inventoryService.handleSorting(sortingService, menu, startIndex, endIndex);
    }

    @Override
    protected KeyMapping getKeyMapping() {
        if (isInventoryButton) {
            return ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING_INVENTORY.get();
        }

        return ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING.get();
    }
}
