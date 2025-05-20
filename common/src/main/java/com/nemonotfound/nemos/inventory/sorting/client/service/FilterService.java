package com.nemonotfound.nemos.inventory.sorting.client.service;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterService {

    private static FilterService INSTANCE;

    public static FilterService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new FilterService();
        }

        return INSTANCE;
    }

    public Map<Boolean, List<Slot>> filterSlots(NonNullList<Slot> slots, String filter) {
        return slots.stream()
                .collect(Collectors.partitioningBy(slot -> filterForItemName(slot, filter)));
    }

    private boolean filterForItemName(Slot slot, String filter) {
        var slotItem = slot.getItem();
        var itemNameContainsFilter = componentContainsFilter(slotItem.getItemName(), filter);
        var itemDisplayNameContainsFilter = componentContainsFilter(slotItem.getDisplayName(), filter);
        var itemEnchantsContainFilter = checkEnchantments(slotItem, filter);

        return !slotItem.is(Items.AIR) && (itemNameContainsFilter || itemDisplayNameContainsFilter || itemEnchantsContainFilter);
    }

    private boolean checkEnchantments(ItemStack itemStack, String filter) {
        var tooltipService = TooltipService.getInstance();

        return tooltipService.retrieveEnchantmentNames(itemStack)
                .toLowerCase()
                .contains(filter.toLowerCase());
    }

    private boolean componentContainsFilter(Component component, String filter) {
        return component.getString().toLowerCase().contains(filter.toLowerCase());
    }
}
