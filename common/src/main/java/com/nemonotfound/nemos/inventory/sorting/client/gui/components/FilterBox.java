package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import com.nemonotfound.nemos.inventory.sorting.interfaces.GuiPosition;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class FilterBox extends EditBox {

    private final Screen containerScreen;

    private final int xOffset;

    //TODO: Add method (with interface) to update position
    public FilterBox(Screen containerScreen, Font font, int x, int y, int xOffset, int yOffset, int width, int height, Component message) {
        super(font, x + xOffset, y + yOffset, width, height, message);
        this.containerScreen = containerScreen;
        this.xOffset = xOffset;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (containerScreen instanceof AbstractRecipeBookScreen<?>) {
            int leftPos = ((GuiPosition) containerScreen).nemosInventorySorting$getLeftPos();

            this.setX(leftPos + this.xOffset);
        }

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }
}
