package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.SortAlphabeticallyButton;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.SortAlphabeticallyDescendingButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
        int y = this.topPos + 40;
        int size = 12;

        Component alphabeticallyComponent = Component.translatable("gui.nemosInventorySorting.sort_alphabetically");
        Component alphabeticallyDescendingComponent = Component.translatable("gui.nemosInventorySorting.sort_alphabetically_descending");
        SortAlphabeticallyButton sortAlphabeticallyButton = new SortAlphabeticallyButton(nemosInventorySorting$getLeftPosWithOffset(40), y, 40, size, size, alphabeticallyComponent, this);
        SortAlphabeticallyDescendingButton sortAlphabeticallyDescendingButton = new SortAlphabeticallyDescendingButton(nemosInventorySorting$getLeftPosWithOffset(22), y, 22, size, size, alphabeticallyDescendingComponent, this);

        this.addRenderableWidget(sortAlphabeticallyButton);
        this.addRenderableWidget(sortAlphabeticallyDescendingButton);
    }

    @Unique
    private int nemosInventorySorting$getLeftPosWithOffset(int offset) {
        return this.leftPos + this.imageWidth - offset - 18;
    }
}