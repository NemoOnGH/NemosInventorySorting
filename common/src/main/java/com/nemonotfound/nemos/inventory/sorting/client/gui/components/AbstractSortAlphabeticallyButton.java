package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import com.nemonotfound.nemos.inventory.sorting.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

public abstract class AbstractSortAlphabeticallyButton extends AbstractSortButton {

    public AbstractSortAlphabeticallyButton(Builder<? extends AbstractSortAlphabeticallyButton> builder) {
        super(builder);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        sortItems();
    }

    private void sortItems() {
        Minecraft minecraft = Minecraft.getInstance();
        AbstractContainerMenu menu = containerScreen.getMenu();
        int containerId = menu.containerId;

        mergeAllItems(menu, containerId, minecraft);
        Map<Integer, Integer> sortedItemMap = createSortedItemMap(menu);

        swapItemsUntilSorted(sortedItemMap, minecraft, containerId);
        mergeAllItems(menu, containerId, minecraft);
    }

    private void mergeAllItems(AbstractContainerMenu menu, int containerId, Minecraft minecraft) {
        Map<DataComponentMap, List<Map.Entry<Integer, ItemStack>>> groupedItemMap = getSortedSlotItems(menu).stream()
                .collect(groupingBy(itemMap -> itemMap.getValue().getComponents()));

        groupedItemMap.forEach((key, mapEntryList) -> mergeItems(mapEntryList, menu, containerId, minecraft));
    }

    private Map<Integer, Integer> createSortedItemMap(AbstractContainerMenu menu) {
        List<Map.Entry<Integer, ItemStack>> itemMapEntries = getSortedSlotItems(menu);
        Map<Integer, Integer> sortedItemMap = new LinkedHashMap<>();

        for (int i = 0; i < itemMapEntries.size(); i++) {
            int newSlot = i + startIndex;
            int currentSlot = itemMapEntries.get(i).getKey();
            if (currentSlot != newSlot) {
                sortedItemMap.put(currentSlot, newSlot);
            }
        }

        return sortedItemMap;
    }

    //TODO: Refactor
    private void swapItemsUntilSorted(Map<Integer, Integer> sortedItemMap, Minecraft minecraft, int containerId) {
        int cycles = 1000;

        while (!sortedItemMap.isEmpty()) {
            if (cycles <= 0) {
                break;
            }

            Iterator<Map.Entry<Integer, Integer>> slotsIterator = sortedItemMap.entrySet().iterator();

            if (slotsIterator.hasNext()) {
                Map.Entry<Integer, Integer> slotsEntry = slotsIterator.next();
                int currentSlot = slotsEntry.getKey();
                int targetSlot = slotsEntry.getValue();

                if (currentSlot == targetSlot) {
                    slotsIterator.remove();
                    continue;
                }

                swapItems(minecraft.gameMode, containerId, currentSlot, targetSlot, minecraft.player);

                if (sortedItemMap.containsKey(targetSlot)) {
                    sortedItemMap.put(currentSlot, sortedItemMap.get(targetSlot));
                } else {
                    slotsIterator.remove();
                }

                sortedItemMap.put(targetSlot, targetSlot);
            }

            cycles--;
        }
    }

    private @NotNull List<Map.Entry<Integer, ItemStack>> getSortedSlotItems(AbstractContainerMenu menu) {
        NonNullList<Slot> slots = menu.slots;

        return IntStream.range(startIndex, endIndex)
                .mapToObj(slotIndex -> Map.entry(slotIndex, slots.get(slotIndex).getItem()))
                .filter(itemStackEntry -> !itemStackEntry.getValue().is(Items.AIR))
                .sorted(compare())
                .toList();

    }

    protected Comparator<Map.Entry<Integer, ItemStack>> compare() {
        return Comparator.comparing(entry -> {
            var itemStack = entry.getValue();
            var itemName = itemStack.getItem().getName(itemStack);

            return itemName.getString();
        });
    }

    private void mergeItems(List<Map.Entry<Integer, ItemStack>> mapEntryList, AbstractContainerMenu menu, int containerId, Minecraft minecraft) {
        if (mapEntryList.size() <= 1) {
            return;
        }

        int leftSlotIndex = 0;
        int rightSlotIndex = mapEntryList.size() - 1;
        int cycles = 1000;

        while (leftSlotIndex < rightSlotIndex) {
            if (cycles <= 0) {
                Constants.LOG.warn("Merging items exceeded max. attempts");

                break;
            }

            Map.Entry<Integer, ItemStack> leftSlotEntry = mapEntryList.get(leftSlotIndex);
            Map.Entry<Integer, ItemStack> rightSlotEntry = mapEntryList.get(rightSlotIndex);
            Slot leftSlot = menu.slots.get(leftSlotEntry.getKey());
            Slot rightSlot = menu.slots.get(rightSlotEntry.getKey());
            ItemStack leftItem = leftSlot.getItem();

            if (!isFullStack(leftItem)) {
                swapItems(minecraft.gameMode, containerId, rightSlotEntry.getKey(), leftSlotEntry.getKey(), minecraft.player);
            } else {
                leftSlotIndex++;
            }

            if (rightSlot.getItem().is(Items.AIR)) {
                rightSlotIndex--;
            }

            cycles--;
        }
    }

    private boolean isFullStack(ItemStack itemStack) {
        return itemStack.getCount() >= itemStack.getMaxStackSize();
    }

    private void swapItems(MultiPlayerGameMode gameMode, int containerId, int slot, int targetSlot, LocalPlayer player) {
        pickUpItem(gameMode, containerId, slot, player);
        pickUpItem(gameMode, containerId, targetSlot, player);
        pickUpItem(gameMode, containerId, slot, player);
    }

    private void pickUpItem(MultiPlayerGameMode gameMode, int containerId, int slot, LocalPlayer player) {
        gameMode.handleInventoryMouseClick(containerId, slot, 0, ClickType.PICKUP, player);
    }
}