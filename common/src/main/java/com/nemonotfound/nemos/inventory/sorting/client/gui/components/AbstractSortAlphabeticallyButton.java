package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import com.nemonotfound.nemos.inventory.sorting.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

public abstract class AbstractSortAlphabeticallyButton extends AbstractSortButton {

    private static final int MAX_CYCLES = 1000;

    public AbstractSortAlphabeticallyButton(Builder<? extends AbstractSortAlphabeticallyButton> builder) {
        super(builder);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        sortItems();
    }

    private void sortItems() {
        var minecraft = Minecraft.getInstance();
        var menu = containerScreen.getMenu();
        int containerId = menu.containerId;

        mergeAllItems(menu, containerId, minecraft);
        var slotSwapMap = createSortedItemMap(menu);

        swapItems(slotSwapMap, minecraft, containerId);
    }

    private void mergeAllItems(AbstractContainerMenu menu, int containerId, Minecraft minecraft) {
        var groupedItemMap = sortSlotItems(menu).stream()
                .collect(groupingBy(slotItem -> slotItem.itemStack().getComponents()));

        groupedItemMap.forEach((key, slotItems) -> mergeItems(slotItems, menu, containerId, minecraft));
    }

    private Map<Integer, Integer> createSortedItemMap(AbstractContainerMenu menu) {
        var sortedSlotItemEntries = sortSlotItems(menu);
        Map<Integer, Integer> slotSwapMap = new LinkedHashMap<>();

        for (int i = 0; i < sortedSlotItemEntries.size(); i++) {
            int newSlot = i + startIndex;
            int currentSlot = sortedSlotItemEntries.get(i).slotIndex();

            if (currentSlot != newSlot) {
                slotSwapMap.put(currentSlot, newSlot);
            }
        }

        return slotSwapMap;
    }

    private void swapItems(Map<Integer, Integer> slotSwapMap, Minecraft minecraft, int containerId) {
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

            performSlotSwap(minecraft.gameMode, containerId, currentSlot, targetSlot, minecraft.player);

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

    private @NotNull List<SlotItem> sortSlotItems(AbstractContainerMenu menu) {
        return IntStream.range(startIndex, calculateEndIndex(menu))
                .mapToObj(slotIndex -> new SlotItem(slotIndex, menu.slots.get(slotIndex).getItem()))
                .filter(slotItem -> !slotItem.itemStack().isEmpty())
                .sorted(compare())
                .toList();
    }

    protected Comparator<SlotItem> compare() {
        return Comparator.comparing(
                slotItem -> slotItem.itemStack()
                        .getItemName()
                        .getString()
        );
    }

    private void mergeItems(List<SlotItem> slotItems, AbstractContainerMenu menu, int containerId, Minecraft minecraft) {
        if (slotItems.size() <= 1) {
            return;
        }

        var leftSlotIndex = 0;
        var rightSlotIndex = slotItems.size() - 1;
        var remainingCycles = MAX_CYCLES;

        while (leftSlotIndex < rightSlotIndex && remainingCycles-- > 0) {
            var leftSlotItem = slotItems.get(leftSlotIndex);
            var rightSlotItem = slotItems.get(rightSlotIndex);
            var leftSlot = menu.slots.get(leftSlotItem.slotIndex());
            var rightSlot = menu.slots.get(rightSlotItem.slotIndex());
            var leftItem = leftSlot.getItem();

            if (!isFullStack(leftItem)) {
                performSlotSwap(minecraft.gameMode, containerId, rightSlotItem.slotIndex(), leftSlotItem.slotIndex(), minecraft.player);
            } else {
                leftSlotIndex++;
            }

            if (rightSlot.getItem().isEmpty()) {
                rightSlotIndex--;
            }
        }

        if (remainingCycles <= 0) {
            Constants.LOG.warn("Merging items exceeded cycle limit. Please report this.");
        }
    }

    private boolean isFullStack(ItemStack itemStack) {
        return itemStack.getCount() >= itemStack.getMaxStackSize();
    }

    private void performSlotSwap(MultiPlayerGameMode gameMode, int containerId, int slot, int targetSlot, LocalPlayer player) {
        pickUpItem(gameMode, containerId, slot, player);
        pickUpItem(gameMode, containerId, targetSlot, player);
        pickUpItem(gameMode, containerId, slot, player);
    }

    private void pickUpItem(MultiPlayerGameMode gameMode, int containerId, int slot, LocalPlayer player) {
        var cursorStack = player.containerMenu.getCarried();
        var menu = containerScreen.getMenu();
        var itemSlot = menu.getSlot(slot);
        var mouseButton = 0;

        if ((!cursorStack.is(Items.AIR) && itemSlot.getItem().is(ItemTags.BUNDLES)) ||
                (cursorStack.is(ItemTags.BUNDLES) && !itemSlot.getItem().is(Items.AIR))) {
            mouseButton = 1;
        }

        gameMode.handleInventoryMouseClick(containerId, slot, mouseButton, ClickType.PICKUP, player);
    }

    protected record SlotItem(int slotIndex, ItemStack itemStack) {}
}