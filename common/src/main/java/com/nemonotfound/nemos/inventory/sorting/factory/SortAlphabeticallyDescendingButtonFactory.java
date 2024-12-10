package com.nemonotfound.nemos.inventory.sorting.factory;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.SortAlphabeticallyDescendingButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;

public class SortAlphabeticallyDescendingButtonFactory extends SortButtonFactory {

    private static SortAlphabeticallyDescendingButtonFactory INSTANCE;

    private SortAlphabeticallyDescendingButtonFactory() {}

    public static SortAlphabeticallyDescendingButtonFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SortAlphabeticallyDescendingButtonFactory();
        }

        return INSTANCE;
    }

    @Override
    public AbstractSortButton createButton(
            int startIndex, int endIndex, int leftPos, int topPos, int xOffset, int yOffset, int imageWidth, int width,
            int height, AbstractContainerScreen<?> containerScreen
    ) {
        Component component = Component.translatable("gui.nemosInventorySorting.sort_alphabetically_descending");
        AbstractSortButton.Builder<SortAlphabeticallyDescendingButton> builder = new AbstractSortButton.Builder<>(SortAlphabeticallyDescendingButton.class)
                .startIndex(startIndex)
                .endIndex(endIndex)
                .x(getLeftPosWithOffset(leftPos, imageWidth, xOffset))
                .y(topPos + yOffset)
                .xOffset(xOffset)
                .width(width)
                .height(height)
                .component(component)
                .containerScreen(containerScreen);

        return builder.build();
    }
}
