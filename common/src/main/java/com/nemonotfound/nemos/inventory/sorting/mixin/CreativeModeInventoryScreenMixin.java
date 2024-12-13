package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import com.nemonotfound.nemos.inventory.sorting.factory.DropAllButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.factory.SortAlphabeticallyButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.factory.SortAlphabeticallyDescendingButtonFactory;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    public CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        int startIndex = 9;
        int endIndex = 36;
        int xOffsetFirstButton = 40;
        int xOffsetSecondButton = 58;
        int xOffsetThirdButton = 76;
        int yOffset = 39;
        int size = 11;

        SortAlphabeticallyButtonFactory sortAlphabeticallyButtonFactory = SortAlphabeticallyButtonFactory.getInstance();
        SortAlphabeticallyDescendingButtonFactory sortAlphabeticallyDescendingButtonFactory = SortAlphabeticallyDescendingButtonFactory.getInstance();
        DropAllButtonFactory dropAllButtonFactory = DropAllButtonFactory.getInstance();

        AbstractSortButton sortAlphabeticallyDescendingButton = sortAlphabeticallyDescendingButtonFactory.createButton(startIndex, endIndex, leftPos, topPos, xOffsetFirstButton, yOffset, imageWidth, size, size, this);
        AbstractSortButton sortAlphabeticallyButton = sortAlphabeticallyButtonFactory.createButton(startIndex, endIndex, leftPos, topPos, xOffsetSecondButton, yOffset, imageWidth, size, size, this);
        AbstractSortButton dropAllButton = dropAllButtonFactory.createButton(startIndex, endIndex, leftPos, topPos, xOffsetThirdButton, yOffset, imageWidth, size, size, this);

        this.addRenderableWidget(sortAlphabeticallyButton);
        this.addRenderableWidget(sortAlphabeticallyDescendingButton);
        this.addRenderableWidget(dropAllButton);
    }
}