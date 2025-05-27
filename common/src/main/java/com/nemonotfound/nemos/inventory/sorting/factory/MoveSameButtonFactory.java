package com.nemonotfound.nemos.inventory.sorting.factory;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.buttons.AbstractInventoryButton;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.buttons.MoveSameButton;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class MoveSameButtonFactory extends SortButtonFactory {

    private static MoveSameButtonFactory INSTANCE;

    private MoveSameButtonFactory() {}

    public static MoveSameButtonFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoveSameButtonFactory();
        }

        return INSTANCE;
    }

    @Override
    public AbstractInventoryButton createButton(
            int startIndex, int endIndex, int leftPos, int topPos, int xOffset, int yOffset, int width,
            int height, AbstractContainerMenu menu
    ) {
        var buttonName = Component.translatable("nemos_inventory_sorting.gui.moveSame");
        var shiftButtonName = Component.translatable("nemos_inventory_sorting.gui.moveSameShift");
        AbstractInventoryButton.Builder<MoveSameButton> builder = new AbstractInventoryButton.Builder<>(MoveSameButton.class)
                .startIndex(startIndex)
                .endIndex(endIndex)
                .x(getLeftPosWithOffset(leftPos, xOffset))
                .y(topPos + yOffset)
                .xOffset(xOffset)
                .width(width)
                .height(height)
                .buttonName(buttonName)
                .shiftButtonName(shiftButtonName)
                .menu(menu);

        return builder.build();
    }
}
