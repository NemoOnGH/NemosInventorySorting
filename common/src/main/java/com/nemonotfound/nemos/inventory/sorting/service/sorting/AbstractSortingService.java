package com.nemonotfound.nemos.inventory.sorting.service.sorting;

import com.nemonotfound.nemos.inventory.sorting.Constants;
import com.nemonotfound.nemos.inventory.sorting.model.SlotItem;
import com.nemonotfound.nemos.inventory.sorting.service.TooltipService;
import com.nemonotfound.nemos.inventory.sorting.service.SlotSwappingService;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.IntStream;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MAX_SORTING_CYCLES;

public abstract class AbstractSortingService {

    private final SlotSwappingService inventorySwapService;
    private final TooltipService tooltipService;
    private final Minecraft minecraft;

    protected AbstractSortingService(SlotSwappingService inventorySwapService, TooltipService tooltipService, Minecraft minecraft) {
        this.inventorySwapService = inventorySwapService;
        this.tooltipService = tooltipService;
        this.minecraft = minecraft;
    }

    public @NotNull List<SlotItem> sortSlotItems(AbstractContainerMenu menu, int startIndex, int endIndex) {
        return IntStream.range(startIndex, endIndex)
                .mapToObj(slotIndex -> new SlotItem(slotIndex, menu.slots.get(slotIndex).getItem()))
                .filter(slotItem -> !slotItem.itemStack().isEmpty())
                .sorted(comparator())
                .toList();
    }

    abstract Comparator<SlotItem> comparator();

    protected Comparator<SlotItem> comparatorByName() {
        Comparator<SlotItem> comparator = Comparator.comparing(
                slotItem -> slotItem.itemStack()
                        .getItemName()
                        .getString()
        );

        return comparatorByTooltip(comparator);
    }

    private Comparator<SlotItem> comparatorByTooltip(Comparator<SlotItem> comparator) {
        var enchantmentComparator = comparator.thenComparing(slotItem -> {
            var tooltipComponents = tooltipService.retrieveTooltipLines(slotItem.itemStack());

            return tooltipService.retrieveEnchantmentNames(tooltipComponents);
        });

        var jukeboxSongComparator = enchantmentComparator.thenComparing(slotItem -> {
            var tooltipComponents = tooltipService.retrieveTooltipLines(slotItem.itemStack());

            return tooltipService.retrieveJukeboxSongName(tooltipComponents);
        });

        return jukeboxSongComparator.thenComparing(slotItem -> {
            var tooltipComponents = tooltipService.retrieveTooltipLines(slotItem.itemStack());

            return tooltipService.retrievePotionName(tooltipComponents);
        });
    }

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

    public void sortItemsInInventory(AbstractContainerMenu menu, Map<Integer, Integer> slotSwapMap, int containerId) {
        int remainingCyles = MAX_SORTING_CYCLES;

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

            inventorySwapService.performSlotSwap(
                    menu,
                    minecraft.gameMode,
                    containerId,
                    currentSlot,
                    targetSlot,
                    minecraft.player
            );

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
