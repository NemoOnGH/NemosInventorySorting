package com.nemonotfound.nemos.inventory.sorting.factory;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.SortAlphabeticallyButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;

public class SortAlphabeticallyButtonFactory extends SortButtonFactory {

    private static SortAlphabeticallyButtonFactory INSTANCE;

    private SortAlphabeticallyButtonFactory() {}

    public static SortAlphabeticallyButtonFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SortAlphabeticallyButtonFactory();
        }

        return INSTANCE;
    }

    @Override
    public AbstractSortButton createButton(
            int startIndex, int endIndex, int leftPos, int topPos, int xOffset, int yOffset, int imageWidth, int width,
            int height, AbstractContainerScreen<?> containerScreen
    ) {
        var buttonName = Component.translatable("gui.nemosInventorySorting.sort_alphabetically");
        var shiftButtonName = Component.translatable("gui.nemosInventorySorting.sort_alphabetically_shift");
        var builder = new AbstractSortButton.Builder<>(SortAlphabeticallyButton.class)
                .startIndex(startIndex)
                .endIndex(endIndex)
                .x(getLeftPosWithOffset(leftPos, imageWidth, xOffset))
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
