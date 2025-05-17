package com.nemonotfound.nemos.inventory.sorting.factory;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.buttons.AbstractInventoryButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public interface ButtonCreator {

    AbstractInventoryButton createButton(
            int startIndex,
            int endIndex,
            int leftPos,
            int topPos,
            int xOffset,
            int yOffset,
            int width,
            int height,
            AbstractContainerScreen<?> containerScreen
    );
}
