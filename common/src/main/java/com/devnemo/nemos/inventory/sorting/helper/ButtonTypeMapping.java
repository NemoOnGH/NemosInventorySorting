package com.devnemo.nemos.inventory.sorting.helper;

import com.devnemo.nemos.inventory.sorting.factory.ButtonCreator;

public record ButtonTypeMapping(String componentName, ButtonCreator factory, int defaultYOffset, boolean isInventoryButton) {
}
