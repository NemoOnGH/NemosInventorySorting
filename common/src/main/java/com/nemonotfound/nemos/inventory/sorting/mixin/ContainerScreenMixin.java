package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.SortAlphabeticallyButton;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.SortAlphabeticallyDescendingButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ContainerScreen.class)
public abstract class ContainerScreenMixin extends AbstractContainerScreen<ChestMenu> {

    public ContainerScreenMixin(ChestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void init() {
        super.init();

        int y = this.topPos + 4;
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
        return this.leftPos + this.imageWidth - offset;
    }
}