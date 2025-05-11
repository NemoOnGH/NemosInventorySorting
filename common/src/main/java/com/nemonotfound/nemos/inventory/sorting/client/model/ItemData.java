package com.nemonotfound.nemos.inventory.sorting.client.model;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;

public record ItemData(DataComponentMap dataComponentMap, Component component) {
}
