package com.nemonotfound.nemos.inventory.sorting.client.service;

import com.nemonotfound.nemos.inventory.sorting.Constants;
import com.nemonotfound.nemos.inventory.sorting.client.model.SlotItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class AbstractSortingService {

    private final InventorySwapService inventorySwapService;

    private static final int MAX_CYCLES = 1000;

    public AbstractSortingService(InventorySwapService inventorySwapService) {
        this.inventorySwapService = inventorySwapService;
    }

    public @NotNull List<SlotItem> sortSlotItems(AbstractContainerMenu menu, int startIndex, int endIndex) {
        return IntStream.range(startIndex, endIndex)
                .mapToObj(slotIndex -> new SlotItem(slotIndex, menu.slots.get(slotIndex).getItem()))
                .filter(slotItem -> !slotItem.itemStack().isEmpty())
                .sorted(comparator())
                .toList();
    }

    abstract Comparator<SlotItem> comparator();

    public Map<Integer, Integer> retrieveSlotSwapMap(List<SlotItem> slotItems, int startIndex) {
        Map<Integer, Integer> slotSwapMap = new LinkedHashMap<>();

        for (int i = 0; i < slotItems.size(); i++) {
            int newSlot = i + startIndex;
            int currentSlot = slotItems.get(i).slotIndex();

            if (currentSlot != newSlot) {
                slotSwapMap.put(currentSlot, newSlot);
            }
        }

        return slotSwapMap;
    }

    public void sortItems(AbstractContainerScreen<?> containerScreen, Map<Integer, Integer> slotSwapMap, Minecraft minecraft, int containerId) {
        int remainingCyles = MAX_CYCLES;

        while (!slotSwapMap.isEmpty() && remainingCyles-- > 0) {
            var iterator = slotSwapMap.entrySet().iterator();

            if (!iterator.hasNext()) {
                break;
            }

            var entry = iterator.next();
            int currentSlot = entry.getKey();
            int targetSlot = entry.getValue();

            if (currentSlot == targetSlot) {
                iterator.remove();
                continue;
            }

            inventorySwapService.performSlotSwap(containerScreen, minecraft.gameMode, containerId, currentSlot, targetSlot, minecraft.player);

            if (slotSwapMap.containsKey(targetSlot)) {
                slotSwapMap.put(currentSlot, slotSwapMap.get(targetSlot));
            } else {
                iterator.remove();
            }

            slotSwapMap.put(targetSlot, targetSlot);
        }

        if (remainingCyles <= 0) {
            Constants.LOG.warn("Slot swap cycle limit reached. Please report this");
        }
    }
}
