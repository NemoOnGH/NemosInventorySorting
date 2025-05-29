package com.nemonotfound.nemos.inventory.sorting.service;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Items;

public class SlotSwappingService {

    private static SlotSwappingService INSTANCE;

    public static SlotSwappingService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SlotSwappingService();
        }

        return INSTANCE;
    }

    public void performSlotSwap(AbstractContainerMenu menu, MultiPlayerGameMode gameMode, int containerId, int slot, int targetSlot, LocalPlayer player) {
        pickUpItem(menu, gameMode, containerId, slot, player);
        pickUpItem(menu, gameMode, containerId, targetSlot, player);
        pickUpItem(menu, gameMode, containerId, slot, player);
    }

    private void pickUpItem(AbstractContainerMenu menu, MultiPlayerGameMode gameMode, int containerId, int slot, LocalPlayer player) {
        var cursorStack = player.containerMenu.getCarried();
        var itemSlot = menu.getSlot(slot);
        var mouseButton = 0;

        if ((!cursorStack.is(Items.AIR) && itemSlot.getItem().is(ItemTags.BUNDLES)) ||
                (cursorStack.is(ItemTags.BUNDLES) && !itemSlot.getItem().is(Items.AIR))) {
            mouseButton = 1;
        }

        gameMode.handleInventoryMouseClick(containerId, slot, mouseButton, ClickType.PICKUP, player);
    }
}
