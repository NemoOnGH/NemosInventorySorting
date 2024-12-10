package com.nemonotfound.nemos.inventory.sorting.factory;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public abstract class SortButtonFactory {

    abstract AbstractSortButton createButton(
            int startIndex, int endIndex, int leftPos, int topPos, int xOffset, int yOffset, int imageWidth, int width,
                                             int height, AbstractContainerScreen<?> containerScreen);

    protected int getLeftPosWithOffset(int leftPos, int imageWidth, int offset) {
        return leftPos + imageWidth - offset;
    }
}
