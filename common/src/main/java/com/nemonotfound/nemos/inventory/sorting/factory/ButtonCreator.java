package com.nemonotfound.nemos.inventory.sorting.factory;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public interface ButtonCreator {

    AbstractSortButton createButton(
            int startIndex,
            int endIndex,
            int leftPos,
            int topPos,
            int xOffset,
            int yOffset,
            int imageWidth,
            int width,
            int height,
            AbstractContainerScreen<?> containerScreen
    );
}
