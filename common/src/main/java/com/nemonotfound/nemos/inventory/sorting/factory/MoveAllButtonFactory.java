package com.nemonotfound.nemos.inventory.sorting.factory;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.buttons.AbstractInventoryButton;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.buttons.MoveAllButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;

public class MoveAllButtonFactory extends SortButtonFactory {

    private static MoveAllButtonFactory INSTANCE;

    private MoveAllButtonFactory() {}

    public static MoveAllButtonFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoveAllButtonFactory();
        }

        return INSTANCE;
    }

    @Override
    public AbstractInventoryButton createButton(
            int startIndex, int endIndex, int leftPos, int topPos, int xOffset, int yOffset, int width,
            int height, AbstractContainerScreen<?> containerScreen
    ) {
        var buttonName = Component.translatable("gui.nemos_inventory_sorting.move_all");
        var shiftButtonName = Component.translatable("gui.nemos_inventory_sorting.move_all_shift");
        var builder = new AbstractInventoryButton.Builder<>(MoveAllButton.class)
                .startIndex(startIndex)
                .endIndex(endIndex)
                .x(getLeftPosWithOffset(leftPos, xOffset))
                .y(topPos + yOffset)
                .xOffset(xOffset)
                .width(width)
                .height(height)
                .buttonName(buttonName)
                .shiftButtonName(shiftButtonName)
                .containerScreen(containerScreen);

        return builder.build();
    }
}
