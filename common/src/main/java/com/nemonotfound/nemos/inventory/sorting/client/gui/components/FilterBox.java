package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class FilterBox extends EditBox implements RecipeBookUpdatable {

    private final int xOffset;
    private static final Component FILTER_HINT = Component.translatable("gui.nemos_inventory_sorting.inventory.item_filter")
            .withStyle(ChatFormatting.ITALIC)
            .withStyle(ChatFormatting.GRAY);

    public FilterBox(Font font, int x, int y, int xOffset, int yOffset, int width, int height, Component message) {
        super(font, x + xOffset, y + yOffset, width, height, message);
        this.xOffset = xOffset;
        this.setTextColor(16777215);
        this.setVisible(true);
        this.setMaxLength(50);
        this.setBordered(true);
        this.setCanLoseFocus(true);
        this.setFocused(false);
        this.setHint(FILTER_HINT);
    }

    public void updateXPosition(int leftPos) {
        this.setX(leftPos + this.xOffset);
    }
}
