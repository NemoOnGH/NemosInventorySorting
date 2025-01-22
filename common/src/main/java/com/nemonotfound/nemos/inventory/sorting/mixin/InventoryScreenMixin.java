package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import com.nemonotfound.nemos.inventory.sorting.factory.DropAllButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.factory.SortAlphabeticallyButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.factory.SortAlphabeticallyDescendingButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.interfaces.GuiPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractRecipeBookScreen<InventoryMenu> implements GuiPosition {

    @Unique
    private AbstractSortButton nemosInventorySorting$inventorySortAlphabeticallyButton;
    @Unique
    private AbstractSortButton nemosInventorySorting$inventorySortAlphabeticallyDescendingButton;
    @Unique
    private AbstractSortButton nemosInventorySorting$inventoryDropAllButton;

    public InventoryScreenMixin(InventoryMenu menu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component component) {
        super(menu, recipeBookComponent, inventory, component);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        int startIndex = 9;
        int endIndex = 36;
        int xOffsetFirstButton = 18;
        int xOffsetSecondButton = 33;
        int xOffsetThirdButton = 48;
        int yOffset = 65;
        int size = 11;

        SortAlphabeticallyButtonFactory sortAlphabeticallyButtonFactory = SortAlphabeticallyButtonFactory.getInstance();
        SortAlphabeticallyDescendingButtonFactory sortAlphabeticallyDescendingButtonFactory = SortAlphabeticallyDescendingButtonFactory.getInstance();
        DropAllButtonFactory dropAllButtonFactory = DropAllButtonFactory.getInstance();

        nemosInventorySorting$inventorySortAlphabeticallyDescendingButton = sortAlphabeticallyDescendingButtonFactory.createButton(startIndex, endIndex, leftPos, topPos, xOffsetFirstButton, yOffset, imageWidth, size, size, this);
        nemosInventorySorting$inventorySortAlphabeticallyButton = sortAlphabeticallyButtonFactory.createButton(startIndex, endIndex, leftPos, topPos, xOffsetSecondButton, yOffset, imageWidth, size, size, this);
        nemosInventorySorting$inventoryDropAllButton = dropAllButtonFactory.createButton(startIndex, endIndex, leftPos, topPos, xOffsetThirdButton, yOffset, imageWidth, size, size, this);

        this.addRenderableWidget(nemosInventorySorting$inventorySortAlphabeticallyButton);
        this.addRenderableWidget(nemosInventorySorting$inventorySortAlphabeticallyDescendingButton);
        this.addRenderableWidget(nemosInventorySorting$inventoryDropAllButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 340) {
            nemosInventorySorting$inventorySortAlphabeticallyButton.setIsShiftKeyDown(true);
            nemosInventorySorting$inventorySortAlphabeticallyButton.setTooltip(this.getMenu());
            nemosInventorySorting$inventorySortAlphabeticallyDescendingButton.setIsShiftKeyDown(true);
            nemosInventorySorting$inventorySortAlphabeticallyDescendingButton.setTooltip(this.getMenu());
            nemosInventorySorting$inventoryDropAllButton.setIsShiftKeyDown(true);
            nemosInventorySorting$inventoryDropAllButton.setTooltip(this.getMenu());
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 340) {
            nemosInventorySorting$inventorySortAlphabeticallyButton.setIsShiftKeyDown(false);
            nemosInventorySorting$inventorySortAlphabeticallyButton.setTooltip(this.getMenu());
            nemosInventorySorting$inventorySortAlphabeticallyDescendingButton.setIsShiftKeyDown(false);
            nemosInventorySorting$inventorySortAlphabeticallyDescendingButton.setTooltip(this.getMenu());
            nemosInventorySorting$inventoryDropAllButton.setIsShiftKeyDown(false);
            nemosInventorySorting$inventoryDropAllButton.setTooltip(this.getMenu());
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public int nemosInventorySorting$getLeftPos() {
        return this.leftPos;
    }

    @Override
    public int nemosInventorySorting$getImageWidth() {
        return this.imageWidth;
    }
}