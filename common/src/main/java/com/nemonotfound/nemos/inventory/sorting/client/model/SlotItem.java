package com.nemonotfound.nemos.inventory.sorting.client.model;

import net.minecraft.world.item.ItemStack;

public record SlotItem(int slotIndex, ItemStack itemStack) {
}
