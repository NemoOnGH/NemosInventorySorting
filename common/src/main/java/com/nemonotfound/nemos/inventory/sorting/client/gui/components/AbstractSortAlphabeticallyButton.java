package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import com.nemonotfound.nemos.inventory.sorting.interfaces.GuiPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractSortAlphabeticallyButton extends AbstractWidget {


    private final AbstractContainerScreen<?> containerScreen;
    private final int xOffset;

    public AbstractSortAlphabeticallyButton(int x, int y, int xOffset, int width, int height, Component message, AbstractContainerScreen<?> containerScreen) {
        super(x, y, width, height, message);
        this.containerScreen = containerScreen;
        this.xOffset = xOffset;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (containerScreen instanceof CreativeModeInventoryScreen creativeModeInventoryScreen && !creativeModeInventoryScreen.isInventoryOpen()) {
            return;
        }

        if (containerScreen instanceof InventoryScreen) {
            int leftPos = ((GuiPosition) containerScreen).nemosInventorySorting$getLeftPos();
            int imageWidth = ((GuiPosition) containerScreen).nemosInventorySorting$getImageWidth();

            this.setX(leftPos + imageWidth - this.xOffset);
        }

        if (this.isHovered()) {
            guiGraphics.blitSprite(RenderType::guiTextured, getButtonHoverTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        } else {
            guiGraphics.blitSprite(RenderType::guiTextured, getButtonTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        }
    }

    protected abstract ResourceLocation getButtonHoverTexture();
    protected abstract ResourceLocation getButtonTexture();

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

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
        Map<String, List<Map.Entry<Integer, ItemStack>>> groupedItemMap = getSortedSlotItems(menu).stream()
                .collect(Collectors.groupingBy(itemMap -> itemMap.getValue().getItemName().getString()));

        groupedItemMap.forEach((key, mapEntryList) -> mergeItems(mapEntryList, menu, containerId, minecraft));
    }

    private Map<Integer, Integer> createSortedItemMap(AbstractContainerMenu menu) {
        List<Map.Entry<Integer, ItemStack>> itemMapEntries = getSortedSlotItems(menu);
        Map<Integer, Integer> sortedItemMap = new LinkedHashMap<>();
        int containerStartIndex = getContainerStartIndex(menu);

        for (int i = 0; i < itemMapEntries.size(); i++) {
            int newSlot = i + containerStartIndex;
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

        return IntStream.range(getContainerStartIndex(menu), getContainerEndIndex(menu))
                .mapToObj(slotIndex -> Map.entry(slotIndex, slots.get(slotIndex).getItem()))
                .filter(itemStackEntry -> !itemStackEntry.getValue().is(Items.AIR))
                .sorted(compare())
                .toList();

    }

    private int getContainerStartIndex(AbstractContainerMenu menu) {
        if (menu instanceof InventoryMenu || menu instanceof CreativeModeInventoryScreen.ItemPickerMenu) {
            return 9;
        }

        return 0;
    }

    private int getContainerEndIndex(AbstractContainerMenu menu) {
        if (menu instanceof ChestMenu) {
            return ((ChestMenu) menu).getContainer().getContainerSize();
        } else if (menu instanceof ShulkerBoxMenu) {
            return 27;
        } else if (menu instanceof InventoryMenu || menu instanceof CreativeModeInventoryScreen.ItemPickerMenu) {
            return 36;
        }

        return 0;
    }

    protected Comparator<Map.Entry<Integer, ItemStack>> compare() {
        return Comparator.comparing(entry -> entry.getValue().getItemName().getString());
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
                break;
            }

            Map.Entry<Integer, ItemStack> leftSlotEntry = mapEntryList.get(leftSlotIndex);
            Map.Entry<Integer, ItemStack> rightSlotEntry = mapEntryList.get(rightSlotIndex);
            Slot leftSlot = menu.slots.get(leftSlotEntry.getKey());
            Slot rightSlot = menu.slots.get(rightSlotEntry.getKey());
            ItemStack leftItem = leftSlot.getItem();
            ItemStack rightItem = rightSlot.getItem();

            if (isFullStack(leftItem) && doDataComponentsMatch(leftItem.getComponents(), rightItem.getComponents())) {
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
        return itemStack.getCount() < itemStack.getMaxStackSize();
    }

    private boolean doDataComponentsMatch(DataComponentMap firstItemComponents, DataComponentMap secondItemComponents) {
        if (hasCustomName(firstItemComponents)) {
            return hasCustomName(secondItemComponents) && matchesCustomName(firstItemComponents, secondItemComponents);
        }

        if (hasMapId(firstItemComponents)) {
            return hasMapId(secondItemComponents) && matchesMapId(firstItemComponents, secondItemComponents);
        }

        return true;
    }

    private boolean hasCustomName(DataComponentMap components) {
        return components.has(DataComponents.CUSTOM_NAME);
    }

    private boolean matchesCustomName(DataComponentMap firstItemComponents, DataComponentMap secondItemComponents) {
        return firstItemComponents.get(DataComponents.CUSTOM_NAME) == secondItemComponents.get(DataComponents.CUSTOM_NAME);
    }

    private boolean hasMapId(DataComponentMap components) {
        return components.has(DataComponents.MAP_ID);
    }

    private boolean matchesMapId(DataComponentMap firstItemComponents, DataComponentMap secondItemComponents) {
        return firstItemComponents.get(DataComponents.MAP_ID) == secondItemComponents.get(DataComponents.MAP_ID);
    }

    private void swapItems(MultiPlayerGameMode gameMode, int containerId, int slot, int targetSlot, LocalPlayer player) {
        pickUpItem(gameMode, containerId, slot, player);
        pickUpItem(gameMode, containerId, targetSlot, player);
        pickUpItem(gameMode, containerId, slot, player);
    }

    private void pickUpItem(MultiPlayerGameMode gameMode, int containerId, int slot, LocalPlayer player) {
        ItemStack cursorStack = player.containerMenu.getCarried();
        AbstractContainerMenu menu = containerScreen.getMenu();
        Slot itemSlot = menu.getSlot(slot);
        int mouseButton = 0;

        if ((!cursorStack.is(Items.AIR) && itemSlot.getItem().is(ItemTags.BUNDLES)) || (cursorStack.is(ItemTags.BUNDLES) && !itemSlot.getItem().is(Items.AIR))) {
            mouseButton = 1;
        }

        gameMode.handleInventoryMouseClick(containerId, slot, mouseButton, ClickType.PICKUP, player);
    }
}