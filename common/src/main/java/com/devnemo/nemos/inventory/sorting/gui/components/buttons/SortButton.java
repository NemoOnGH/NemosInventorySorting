package com.devnemo.nemos.inventory.sorting.gui.components.buttons;

import com.devnemo.nemos.inventory.sorting.ModKeyMappings;
import com.devnemo.nemos.inventory.sorting.service.InventoryService;
import com.devnemo.nemos.inventory.sorting.service.SortingService;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

import static com.devnemo.nemos.inventory.sorting.Constants.MOD_ID;

public class SortButton extends AbstractInventoryButton {

    private final ResourceLocation buttonTexture = new ResourceLocation(MOD_ID, "textures/gui/sprites/sort_button.png");
    private final ResourceLocation buttonHoverTexture = new ResourceLocation(MOD_ID, "textures/gui/sprites/sort_button_highlighted.png");

    public SortButton(Builder<SortButton> builder) {
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
        var sortingService = SortingService.getInstance();
        var endIndex = inventoryService.calculateEndIndex(isButtonShiftable(), this.endIndex);

        inventoryService.handleSorting(sortingService, menu, startIndex, endIndex);
    }

    @Override
    protected KeyMapping getKeyMapping() {
        if (isInventoryButton) {
            return ModKeyMappings.SORT_INVENTORY.get();
        }

        return ModKeyMappings.SORT.get();
    }
}
