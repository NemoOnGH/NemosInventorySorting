package com.devnemo.nemos.inventory.sorting.service;

import com.devnemo.nemos.inventory.sorting.model.FilterResult;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterService {

    private static FilterService INSTANCE;
    private final TooltipService tooltipService;

    public static FilterService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FilterService(TooltipService.getInstance());
        }

        return INSTANCE;
    }

    public FilterService(TooltipService tooltipService) {
        this.tooltipService = tooltipService;
    }

    public Map<FilterResult, List<Slot>> filterSlots(NonNullList<Slot> slots, String filter) {
        return slots.stream()
                .collect(Collectors.groupingBy(slot -> filterSlot(slot, filter)));
    }

    private FilterResult filterSlot(Slot slot, String filter) {
        var slotItem = slot.getItem();

        if (slotItem.is(Items.AIR)) {
            return FilterResult.EXCLUDED;
        }

        if (itemContainerContentsMatchFilter(slotItem, filter)) {
            return FilterResult.HAS_INCLUDED_ITEM;
        }

        return matchesFilter(slotItem, filter) ? FilterResult.INCLUDED : FilterResult.EXCLUDED;
    }

    private boolean itemContainerContentsMatchFilter(ItemStack itemStack, String filter) {
        var compoundTag = itemStack.getTag();

        if (compoundTag == null || !compoundTag.contains("BlockEntityTag")) {
            return false;
        }

        var containerItems = compoundTag.get("BlockEntityTag").getAsString();

        return containerItems.contains(filter);
    }

    private boolean contentsMatchFilter(Stream<ItemStack> stream, String filter) {
        return stream.anyMatch(itemStack -> matchesFilter(itemStack, filter));
    }

    private boolean matchesFilter(ItemStack itemStack, String filter) {
        var itemNameMatchesFilter = componentMatchesFilter(itemStack.getItem().getName(itemStack), filter);
        var itemDisplayNameMatchesFilter = componentMatchesFilter(itemStack.getDisplayName(), filter);
        var tooltipMatchesFilter = tooltipMatchesFilter(itemStack, filter);

        return itemNameMatchesFilter || itemDisplayNameMatchesFilter || tooltipMatchesFilter;
    }

    private boolean tooltipMatchesFilter(ItemStack itemStack, String filter) {
        var tooltipComponents = tooltipService.retrieveTooltipLines(itemStack);
        var itemEnchantsMatchesFilter = tooltipService.retrieveEnchantmentNames(tooltipComponents)
                .toLowerCase()
                .contains(filter.toLowerCase());
        var jukeboxSongMatchesFilter = tooltipService.retrieveJukeboxSongName(tooltipComponents)
                .toLowerCase()
                .contains(filter.toLowerCase());
        var potionMatchesFilter = tooltipService.retrievePotionName(tooltipComponents)
                .toLowerCase()
                .contains(filter.toLowerCase());

        return itemEnchantsMatchesFilter || jukeboxSongMatchesFilter || potionMatchesFilter;
    }

    private boolean componentMatchesFilter(Component component, String filter) {
        return component.getString().toLowerCase().contains(filter.toLowerCase());
    }
}
