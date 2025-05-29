package com.nemonotfound.nemos.inventory.sorting.gui.components;

import com.nemonotfound.nemos.inventory.sorting.model.FilterResult;
import com.nemonotfound.nemos.inventory.sorting.service.FilterService;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;

import java.util.List;
import java.util.Map;

public class FilterBox extends EditBox implements RecipeBookUpdatable {

    private static final Component FILTER_HINT = Component.translatable("nemos_inventory_sorting.gui.inventory.itemFilter")
            .withStyle(ChatFormatting.ITALIC)
            .withStyle(ChatFormatting.GRAY);

    private final FilterService filterService;
    private final int xOffset;

    public FilterBox(Font font, int x, int y, int xOffset, int yOffset, int width, int height, Component message) {
        super(font, x + xOffset, y + yOffset, width, height, message);
        this.filterService = FilterService.getInstance();
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

    public Map<FilterResult, List<Slot>> filterSlots(NonNullList<Slot> slots, String filter) {
        return filterService.filterSlots(slots, filter);
    }
}
