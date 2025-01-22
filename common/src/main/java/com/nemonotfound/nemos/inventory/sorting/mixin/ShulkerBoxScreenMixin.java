package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.ContainerFilterBox;
import com.nemonotfound.nemos.inventory.sorting.factory.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxScreen.class)
public abstract class ShulkerBoxScreenMixin extends AbstractContainerScreen<ShulkerBoxMenu> {

    @Unique
    private ContainerFilterBox nemosInventorySorting$containerFilterBox;
    @Unique
    private EditBox nemosInventorySorting$searchBox;
    @Unique
    private AbstractSortButton nemosInventorySorting$inventoryMoveSameButton;
    @Unique
    private AbstractSortButton nemosInventorySorting$inventorySortAlphabeticallyButton;
    @Unique
    private AbstractSortButton nemosInventorySorting$inventorySortAlphabeticallyDescendingButton;
    @Unique
    private AbstractSortButton nemosInventorySorting$inventoryMoveAllButton;
    @Unique
    private AbstractSortButton nemosInventorySorting$inventoryDropAllButton;
    
    public ShulkerBoxScreenMixin(ShulkerBoxMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void init() {
        super.init();

        var shulkerBoxStartIndex = 0;
        var shulkerBoxEndIndex = 27;
        var inventoryStartIndex = 27;
        var inventoryEndIndex = 54;
        var xOffsetFirstButton = 22;
        var xOffsetSecondButton = 40;
        var xOffsetThirdButton = 58;
        var xOffsetFourthButton = 76;
        var xOffsetFifthButton = 94;
        var yOffsetInventory = 71;
        var yOffsetContainer = 5;
        var size = 11;

        nemosInventorySorting$containerFilterBox = new ContainerFilterBox(this.font, this.leftPos, this.topPos, 3);
        nemosInventorySorting$searchBox = nemosInventorySorting$containerFilterBox.getSearchBox();
        this.addWidget(nemosInventorySorting$searchBox);

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
        nemosInventorySorting$inventorySortAlphabeticallyButton = sortAlphabeticallyButtonFactory.createButton(inventoryStartIndex, inventoryEndIndex, leftPos, topPos, xOffsetSecondButton, yOffsetInventory, imageWidth, size, size, this);
        nemosInventorySorting$inventorySortAlphabeticallyDescendingButton = sortAlphabeticallyDescendingButtonFactory.createButton(inventoryStartIndex, inventoryEndIndex, leftPos, topPos, xOffsetFirstButton, yOffsetInventory, imageWidth, size, size, this);
        nemosInventorySorting$inventoryMoveSameButton = moveSameButtonFactory.createButton(inventoryStartIndex, inventoryEndIndex, leftPos, topPos, xOffsetThirdButton, yOffsetInventory, imageWidth, size, size, this);
        nemosInventorySorting$inventoryMoveAllButton = moveAllButtonFactory.createButton(inventoryStartIndex, inventoryEndIndex, leftPos, topPos, xOffsetFourthButton, yOffsetInventory, imageWidth, size, size, this);
        nemosInventorySorting$inventoryDropAllButton = dropAllButtonFactory.createButton(inventoryStartIndex, inventoryEndIndex, leftPos, topPos, xOffsetFifthButton, yOffsetInventory, imageWidth, size, size, this);

        this.addRenderableWidget(sortAlphabeticallyButton);
        this.addRenderableWidget(sortAlphabeticallyDescendingButton);
        this.addRenderableWidget(moveSameButton);
        this.addRenderableWidget(moveAllButton);
        this.addRenderableWidget(dropAllButton);
        this.addRenderableWidget(nemosInventorySorting$inventorySortAlphabeticallyButton);
        this.addRenderableWidget(nemosInventorySorting$inventorySortAlphabeticallyDescendingButton);
        this.addRenderableWidget(nemosInventorySorting$inventoryMoveSameButton);
        this.addRenderableWidget(nemosInventorySorting$inventoryMoveAllButton);
        this.addRenderableWidget(nemosInventorySorting$inventoryDropAllButton);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", shift = At.Shift.AFTER))
    private void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        nemosInventorySorting$searchBox.render(guiGraphics, mouseX, mouseY, partialTick);
        var filter = nemosInventorySorting$searchBox.getValue();

        if (!filter.isEmpty()) {
            this.nemosInventorySorting$containerFilterBox.filterSlots(this.getMenu().slots, filter, guiGraphics);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.nemosInventorySorting$searchBox.isFocused() && keyCode != 256) {
            return this.nemosInventorySorting$searchBox.keyPressed(keyCode, scanCode, modifiers);
        } else if (keyCode == 340) {
            nemosInventorySorting$inventoryMoveSameButton.setIsShiftKeyDown(true);
            nemosInventorySorting$inventoryMoveSameButton.setTooltip(this.getMenu());
            nemosInventorySorting$inventorySortAlphabeticallyButton.setIsShiftKeyDown(true);
            nemosInventorySorting$inventorySortAlphabeticallyButton.setTooltip(this.getMenu());
            nemosInventorySorting$inventorySortAlphabeticallyDescendingButton.setIsShiftKeyDown(true);
            nemosInventorySorting$inventorySortAlphabeticallyDescendingButton.setTooltip(this.getMenu());
            nemosInventorySorting$inventoryMoveAllButton.setIsShiftKeyDown(true);
            nemosInventorySorting$inventoryMoveAllButton.setTooltip(this.getMenu());
            nemosInventorySorting$inventoryDropAllButton.setIsShiftKeyDown(true);
            nemosInventorySorting$inventoryDropAllButton.setTooltip(this.getMenu());
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 340) {
            nemosInventorySorting$inventoryMoveSameButton.setIsShiftKeyDown(false);
            nemosInventorySorting$inventoryMoveSameButton.setTooltip(this.getMenu());
            nemosInventorySorting$inventorySortAlphabeticallyButton.setIsShiftKeyDown(false);
            nemosInventorySorting$inventorySortAlphabeticallyButton.setTooltip(this.getMenu());
            nemosInventorySorting$inventorySortAlphabeticallyDescendingButton.setIsShiftKeyDown(false);
            nemosInventorySorting$inventorySortAlphabeticallyDescendingButton.setTooltip(this.getMenu());
            nemosInventorySorting$inventoryMoveAllButton.setIsShiftKeyDown(false);
            nemosInventorySorting$inventoryMoveAllButton.setTooltip(this.getMenu());
            nemosInventorySorting$inventoryDropAllButton.setIsShiftKeyDown(false);
            nemosInventorySorting$inventoryDropAllButton.setTooltip(this.getMenu());
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        nemosInventorySorting$searchBox.setFocused(nemosInventorySorting$searchBox.mouseClicked(mouseX, mouseY, button));

        return super.mouseClicked(mouseX, mouseY, button);
    }
}