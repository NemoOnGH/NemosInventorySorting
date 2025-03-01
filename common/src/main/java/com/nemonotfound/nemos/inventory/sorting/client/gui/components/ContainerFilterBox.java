package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContainerFilterBox {

    private final EditBox searchBox;

    public ContainerFilterBox(Font font, int leftPos, int topPos, int xOffset, int yOffset, int width, int height) {
        this.searchBox = new EditBox(font, leftPos + xOffset, topPos + yOffset, width, height, Component.translatable("itemGroup.search"));
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
        var itemNameContainsFilter = componentContainsFilter(slotItem.getItem().getName(slotItem), filter);
        var itemDisplayNameContainsFilter = componentContainsFilter(slotItem.getDisplayName(), filter);
        var itemEnchantsContainFilter = checkEnchantments(slotItem, filter);

        return !slotItem.is(Items.AIR) && (itemNameContainsFilter || itemDisplayNameContainsFilter || itemEnchantsContainFilter);
    }

    private boolean checkEnchantments(ItemStack itemStack, String filter) {
        var dataComponents = itemStack.getComponents();

        if (!itemStack.is(Items.ENCHANTED_BOOK) || !dataComponents.has(DataComponents.STORED_ENCHANTMENTS)) {
            return false;
        }

        var storedEnchantments = dataComponents.get(DataComponents.STORED_ENCHANTMENTS);

        if (storedEnchantments == null) {
            return false;
        }

        return storedEnchantments.entrySet().stream()
                .map(holderEntry -> Enchantment.getFullname(holderEntry.getKey(), holderEntry.getIntValue()))
                .anyMatch(component -> componentContainsFilter(component, filter));
    }

    private boolean componentContainsFilter(Component component, String filter) {
        return component.getString().toLowerCase().contains(filter.toLowerCase());
    }
}
