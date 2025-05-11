package com.nemonotfound.nemos.inventory.sorting.client.service;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.ClickType;

public class SlotSwappingService {

    private static SlotSwappingService INSTANCE;

    public static SlotSwappingService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SlotSwappingService();
        }

        return INSTANCE;
    }

    public void performSlotSwap(MultiPlayerGameMode gameMode, int containerId, int slot, int targetSlot, LocalPlayer player) {
        pickUpItem(gameMode, containerId, slot, player);
        pickUpItem(gameMode, containerId, targetSlot, player);
        pickUpItem(gameMode, containerId, slot, player);
    }

    private void pickUpItem(MultiPlayerGameMode gameMode, int containerId, int slot, LocalPlayer player) {
        gameMode.handleInventoryMouseClick(containerId, slot, 0, ClickType.PICKUP, player);
    }
}
