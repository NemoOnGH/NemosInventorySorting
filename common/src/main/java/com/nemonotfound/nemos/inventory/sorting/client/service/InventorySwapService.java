package com.nemonotfound.nemos.inventory.sorting.client.service;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Items;

public class InventorySwapService {

    private static InventorySwapService INSTANCE;

    public static InventorySwapService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new InventorySwapService();
        }

        return INSTANCE;
    }

    public void performSlotSwap(AbstractContainerScreen<?> containerScreen, MultiPlayerGameMode gameMode, int containerId, int slot, int targetSlot, LocalPlayer player) {
        pickUpItem(containerScreen, gameMode, containerId, slot, player);
        pickUpItem(containerScreen, gameMode, containerId, targetSlot, player);
        pickUpItem(containerScreen, gameMode, containerId, slot, player);
    }

    private void pickUpItem(AbstractContainerScreen<?> containerScreen, MultiPlayerGameMode gameMode, int containerId, int slot, LocalPlayer player) {
        var cursorStack = player.containerMenu.getCarried();
        var menu = containerScreen.getMenu();
        var itemSlot = menu.getSlot(slot);
        var mouseButton = 0;

        if ((!cursorStack.is(Items.AIR) && itemSlot.getItem().is(ItemTags.BUNDLES)) ||
                (cursorStack.is(ItemTags.BUNDLES) && !itemSlot.getItem().is(Items.AIR))) {
            mouseButton = 1;
        }

        gameMode.handleInventoryMouseClick(containerId, slot, mouseButton, ClickType.PICKUP, player);
    }
}
