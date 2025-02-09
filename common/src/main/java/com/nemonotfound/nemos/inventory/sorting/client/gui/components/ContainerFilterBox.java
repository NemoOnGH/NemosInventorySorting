package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContainerFilterBox {

    private final EditBox searchBox;

    public ContainerFilterBox(Font font, int leftPos, int topPos) {
        this.searchBox = new EditBox(font, leftPos + 89, topPos - 16, 84, 15, Component.translatable("itemGroup.search"));
        this.searchBox.setTextColor(16777215);
        this.searchBox.setVisible(true);
        this.searchBox.setMaxLength(50);
        this.searchBox.setBordered(true);
        this.searchBox.setCanLoseFocus(true);
        this.searchBox.setFocused(false);
    }

    public EditBox getSearchBox() {
        return searchBox;
    }

    public Map<Boolean, List<Slot>> filterSlots(NonNullList<Slot> slots, String filter) {
        return slots.stream()
                .collect(Collectors.partitioningBy(slot -> filterForItemName(slot, filter)));
    }

    private boolean filterForItemName(Slot slot, String filter) {
        var slotItem = slot.getItem();
        var itemNameContainsFilter = nameContainsFilter(slotItem.getItemName(), filter);
        var itemDisplayNameContainsFilter = nameContainsFilter(slotItem.getDisplayName(), filter);

        return !slotItem.is(Items.AIR) && (itemNameContainsFilter || itemDisplayNameContainsFilter);
    }

    private boolean nameContainsFilter(Component component, String filter) {
        return component.getString().toLowerCase().contains(filter.toLowerCase());
    }
}
