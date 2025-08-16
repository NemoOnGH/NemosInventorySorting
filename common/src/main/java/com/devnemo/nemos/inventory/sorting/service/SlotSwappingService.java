package com.devnemo.nemos.inventory.sorting.service;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;

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
        gameMode.handleInventoryMouseClick(containerId, slot, 0, ClickType.PICKUP, player);
    }
}
