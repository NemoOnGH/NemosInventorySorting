package com.devnemo.nemos.inventory.sorting.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

import static com.devnemo.nemos.inventory.sorting.Constants.MOD_ID;

public class InventorySortingCategories {

    public static final KeyMapping.Category NEMOS_INVENTORY_SORTING = registerCategory();

    public static void init() {}

    private static KeyMapping.Category registerCategory() {
        return KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, com.devnemo.nemos.inventory.sorting.Constants.MOD_ID));
    }
}
