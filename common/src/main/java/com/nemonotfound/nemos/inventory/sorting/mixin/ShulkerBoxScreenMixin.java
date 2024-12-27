package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import com.nemonotfound.nemos.inventory.sorting.factory.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShulkerBoxScreen.class)
public abstract class ShulkerBoxScreenMixin extends AbstractContainerScreen<ShulkerBoxMenu> {

    public ShulkerBoxScreenMixin(ShulkerBoxMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void init() {
        super.init();

        int shulkerBoxStartIndex = 0;
        int shulkerBoxEndIndex = 27;
        int inventoryStartIndex = 27;
        int inventoryEndIndex = 54;
        int inventoryWithHotbarEndIndex = inventoryEndIndex + 9;
        int xOffsetFirstButton = 22;
        int xOffsetSecondButton = 40;
        int xOffsetThirdButton = 58;
        int xOffsetFourthButton = 76;
        int xOffsetFifthButton = 94;
        int yOffsetInventory = 71;
        int yOffsetContainer = 5;
        int size = 11;

        SortAlphabeticallyButtonFactory sortAlphabeticallyButtonFactory = SortAlphabeticallyButtonFactory.getInstance();
        SortAlphabeticallyDescendingButtonFactory sortAlphabeticallyDescendingButtonFactory = SortAlphabeticallyDescendingButtonFactory.getInstance();
        DropAllButtonFactory dropAllButtonFactory = DropAllButtonFactory.getInstance();
        MoveSameButtonFactory moveSameButtonFactory = MoveSameButtonFactory.getInstance();
        MoveAllButtonFactory moveAllButtonFactory = MoveAllButtonFactory.getInstance();

        AbstractSortButton sortAlphabeticallyButton = sortAlphabeticallyButtonFactory.createButton(shulkerBoxStartIndex, shulkerBoxEndIndex, leftPos, topPos, xOffsetSecondButton, yOffsetContainer, imageWidth, size, size, this);
        AbstractSortButton sortAlphabeticallyDescendingButton = sortAlphabeticallyDescendingButtonFactory.createButton(shulkerBoxStartIndex, shulkerBoxEndIndex, leftPos, topPos, xOffsetFirstButton, yOffsetContainer, imageWidth, size, size, this);
        AbstractSortButton moveSameButton = moveSameButtonFactory.createButton(shulkerBoxStartIndex, shulkerBoxEndIndex, leftPos, topPos, xOffsetThirdButton, yOffsetContainer, imageWidth, size, size, this);
        AbstractSortButton moveAllButton = moveAllButtonFactory.createButton(shulkerBoxStartIndex, shulkerBoxEndIndex, leftPos, topPos, xOffsetFourthButton, yOffsetContainer, imageWidth, size, size, this);
        AbstractSortButton dropAllButton = dropAllButtonFactory.createButton(shulkerBoxStartIndex, shulkerBoxEndIndex, leftPos, topPos, xOffsetFifthButton, yOffsetContainer, imageWidth, size, size, this);
        AbstractSortButton inventorySortAlphabeticallyInButton = sortAlphabeticallyButtonFactory.createButton(inventoryStartIndex, inventoryEndIndex, leftPos, topPos, xOffsetSecondButton, yOffsetInventory, imageWidth, size, size, this);
        AbstractSortButton inventorySortAlphabeticallyDescendingInButton = sortAlphabeticallyDescendingButtonFactory.createButton(inventoryStartIndex, inventoryEndIndex, leftPos, topPos, xOffsetFirstButton, yOffsetInventory, imageWidth, size, size, this);
        AbstractSortButton inventoryMoveSameButton = moveSameButtonFactory.createButton(inventoryStartIndex, inventoryWithHotbarEndIndex, leftPos, topPos, xOffsetThirdButton, yOffsetInventory, imageWidth, size, size, this);
        AbstractSortButton inventoryMoveAllButton = moveAllButtonFactory.createButton(inventoryStartIndex, inventoryEndIndex, leftPos, topPos, xOffsetFourthButton, yOffsetInventory, imageWidth, size, size, this);
        AbstractSortButton inventoryDropAllButton = dropAllButtonFactory.createButton(inventoryStartIndex, inventoryEndIndex, leftPos, topPos, xOffsetFifthButton, yOffsetInventory, imageWidth, size, size, this);

        this.addRenderableWidget(sortAlphabeticallyButton);
        this.addRenderableWidget(sortAlphabeticallyDescendingButton);
        this.addRenderableWidget(moveSameButton);
        this.addRenderableWidget(moveAllButton);
        this.addRenderableWidget(dropAllButton);
        this.addRenderableWidget(inventorySortAlphabeticallyInButton);
        this.addRenderableWidget(inventorySortAlphabeticallyDescendingInButton);
        this.addRenderableWidget(inventoryMoveSameButton);
        this.addRenderableWidget(inventoryMoveAllButton);
        this.addRenderableWidget(inventoryDropAllButton);
    }
}