package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.SortAlphabeticallyButton;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.SortAlphabeticallyDescendingButton;
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

    public InventoryScreenMixin(InventoryMenu menu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component component) {
        super(menu, recipeBookComponent, inventory, component);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        int y = this.topPos + 70;
        int size = 12;

        Component alphabeticallyComponent = Component.translatable("gui.nemosInventorySorting.sort_alphabetically");
        Component alphabeticallyDescendingComponent = Component.translatable("gui.nemosInventorySorting.sort_alphabetically_descending");
        SortAlphabeticallyButton sortAlphabeticallyButton = new SortAlphabeticallyButton(nemosInventorySorting$getLeftPosWithOffset(40), y, 40, size, size, alphabeticallyComponent, this);
        SortAlphabeticallyDescendingButton sortAlphabeticallyDescendingButton = new SortAlphabeticallyDescendingButton(nemosInventorySorting$getLeftPosWithOffset(22), y, 22, size, size, alphabeticallyDescendingComponent, this);

        this.addRenderableWidget(sortAlphabeticallyButton);
        this.addRenderableWidget(sortAlphabeticallyDescendingButton);
    }

    @Override
    public int nemosInventorySorting$getLeftPos() {
        return this.leftPos;
    }

    @Override
    public int nemosInventorySorting$getImageWidth() {
        return this.imageWidth;
    }

    @Unique
    private int nemosInventorySorting$getLeftPosWithOffset(int offset) {
        return this.leftPos + this.imageWidth - offset;
    }
}