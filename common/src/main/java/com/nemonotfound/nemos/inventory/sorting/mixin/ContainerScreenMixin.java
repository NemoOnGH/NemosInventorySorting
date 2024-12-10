package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import com.nemonotfound.nemos.inventory.sorting.factory.DropAllButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.factory.SortAlphabeticallyButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.factory.SortAlphabeticallyDescendingButtonFactory;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerScreen.class)
public abstract class ContainerScreenMixin extends AbstractContainerScreen<ChestMenu> {

    @Shadow @Final private int containerRows;

    public ContainerScreenMixin(ChestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void init() {
        super.init();

        int xOffsetFirstButton = 22;
        int xOffsetSecondButton = 40;
        int xOffsetThirdButton = 58;
        int yOffsetInventory = 72;
        int yOffsetContainer = 5;
        int size = 11;
        int containerSize = this.getMenu().getContainer().getContainerSize();

        if (containerRows == 6) {
            yOffsetInventory = 126;
        }

        SortAlphabeticallyButtonFactory sortAlphabeticallyButtonFactory = SortAlphabeticallyButtonFactory.getInstance();
        SortAlphabeticallyDescendingButtonFactory sortAlphabeticallyDescendingButtonFactory = SortAlphabeticallyDescendingButtonFactory.getInstance();
        DropAllButtonFactory dropAllButtonFactory = DropAllButtonFactory.getInstance();

        AbstractSortButton sortAlphabeticallyButton = sortAlphabeticallyButtonFactory.createButton(0, containerSize, leftPos, topPos, xOffsetSecondButton, yOffsetContainer, imageWidth, size, size, this);
        AbstractSortButton sortAlphabeticallyDescendingButton = sortAlphabeticallyDescendingButtonFactory.createButton(0, containerSize, leftPos, topPos, xOffsetFirstButton, yOffsetContainer, imageWidth, size, size, this);
        AbstractSortButton dropAllButton = dropAllButtonFactory.createButton(0, containerSize, leftPos, topPos, xOffsetThirdButton, yOffsetContainer, imageWidth, size, size, this);
        AbstractSortButton inventorySortAlphabeticallyInButton = sortAlphabeticallyButtonFactory.createButton(containerSize, containerSize + 27, leftPos, topPos, xOffsetSecondButton, yOffsetInventory, imageWidth, size, size, this);
        AbstractSortButton inventorySortAlphabeticallyDescendingInButton = sortAlphabeticallyDescendingButtonFactory.createButton(containerSize, containerSize + 27, leftPos, topPos, xOffsetFirstButton, yOffsetInventory, imageWidth, size, size, this);
        AbstractSortButton inventoryDropAllButton = dropAllButtonFactory.createButton(containerSize, containerSize + 27, leftPos, topPos, xOffsetThirdButton, yOffsetInventory, imageWidth, size, size, this);

        this.addRenderableWidget(sortAlphabeticallyButton);
        this.addRenderableWidget(sortAlphabeticallyDescendingButton);
        this.addRenderableWidget(dropAllButton);
        this.addRenderableWidget(inventorySortAlphabeticallyInButton);
        this.addRenderableWidget(inventorySortAlphabeticallyDescendingInButton);
        this.addRenderableWidget(inventoryDropAllButton);
    }
}