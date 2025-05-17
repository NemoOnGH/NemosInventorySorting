package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import com.nemonotfound.nemos.inventory.sorting.client.service.TooltipService;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TODO: Refactor & extract logic
public class ContainerFilterBox {

    private final FilterBox searchBox;

    private static final Component FILTER_HINT = Component.translatable("gui.nemos_inventory_sorting.inventory.item_filter")
            .withStyle(ChatFormatting.ITALIC)
            .withStyle(ChatFormatting.GRAY);

    //TODO: Adapt component
    public ContainerFilterBox(Screen containerScreen, Font font, int leftPos, int topPos, int xOffset, int yOffset, int width, int height) {
        this.searchBox = new FilterBox(containerScreen, font, leftPos, topPos, xOffset, yOffset, width, height, Component.translatable("itemGroup.search"));
        this.searchBox.setTextColor(16777215);
        this.searchBox.setVisible(true);
        this.searchBox.setMaxLength(50);
        this.searchBox.setBordered(true);
        this.searchBox.setCanLoseFocus(true);
        this.searchBox.setFocused(false);
        this.searchBox.setHint(FILTER_HINT);
    }

    public FilterBox getSearchBox() {
        return searchBox;
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
