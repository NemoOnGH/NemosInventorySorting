package com.nemonotfound.nemos.inventory.sorting.factory;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.DropAllButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;

public class DropAllButtonFactory extends SortButtonFactory {

    private static DropAllButtonFactory INSTANCE;

    private DropAllButtonFactory() {}

    public static DropAllButtonFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DropAllButtonFactory();
        }

        return INSTANCE;
    }

    @Override
    public AbstractSortButton createButton(
            int startIndex, int endIndex, int leftPos, int topPos, int xOffset, int yOffset, int width,
            int height, AbstractContainerScreen<?> containerScreen
    ) {
        var buttonName = Component.translatable("gui.nemosInventorySorting.drop_all");
        var shiftButtonName = Component.translatable("gui.nemosInventorySorting.drop_all_shift");
        var builder = new AbstractSortButton.Builder<>(DropAllButton.class)
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
