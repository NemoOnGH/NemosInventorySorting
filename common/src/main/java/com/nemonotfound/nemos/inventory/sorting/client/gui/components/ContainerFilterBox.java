package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

public class ContainerFilterBox {

    private final EditBox searchBox;
    private final int leftPos;
    private final int topPos;
    private final int containerRows;
    private static final ResourceLocation HIGHLIGHTED_SLOT = new ResourceLocation(MOD_ID, "textures/gui/sprites/container/highlighted_slot.png");
    private static final ResourceLocation DIMMED_SLOT = new ResourceLocation(MOD_ID, "textures/gui/sprites/container/dimmed_slot.png");

    public ContainerFilterBox(Font font, int leftPos, int topPos, int containerRows) {
        this.leftPos = leftPos;
        this.topPos = topPos;
        this.containerRows = containerRows;
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

    public void filterSlots(NonNullList<Slot> slots, String filter, GuiGraphics guiGraphics) {
        var filteredSlotMap = IntStream.range(0, slots.size())
                .boxed()
                .collect(Collectors.partitioningBy(i -> filterForItemName(slots.get(i), filter)));
        var leftPosOffset = leftPos + 8;
        var topPosOffset = topPos;

        markSlots(false, filteredSlotMap.get(true), leftPosOffset, topPosOffset, guiGraphics, HIGHLIGHTED_SLOT);
        markSlots(true, filteredSlotMap.get(false), leftPosOffset, topPosOffset, guiGraphics, DIMMED_SLOT);
    }

    private boolean filterForItemName(Slot slot, String filter) {
        var slotItem = slot.getItem();
        var itemName = slotItem.getItem().getName(slotItem);
        var itemNameContainsFilter = nameContainsFilter(itemName, filter);
        var itemDisplayNameContainsFilter = nameContainsFilter(slotItem.getDisplayName(), filter);

        return !slotItem.is(Items.AIR) && (itemNameContainsFilter || itemDisplayNameContainsFilter);
    }

    private boolean nameContainsFilter(Component component, String filter) {
        return component.getString().toLowerCase().contains(filter.toLowerCase());
    }

    private void markSlots(
            boolean shouldFillGradient,
            List<Integer> slots,
            int leftPosOffset,
            int topPosOffset,
            GuiGraphics guiGraphics,
            ResourceLocation texture
    ) {
        for (int slotIndex : slots) {
            var column = slotIndex % 9;
            var row = (int) Math.ceil((double) (slotIndex + 1) / 9);
            var xPos = leftPosOffset + (18 * column);
            var yPos = calculateYPos(containerRows, row, topPosOffset);

            guiGraphics.blit(texture, xPos, yPos, 0, 0, 16, 16);

            if (shouldFillGradient) {
                guiGraphics.fillGradient(RenderType.guiOverlay(), xPos, yPos, xPos + 16, yPos + 16, -2139062142, -2139062142, 0);
            }
        }
    }

    private int calculateYPos(int rowCount, int row, int topPosOffset) {
        var base = topPosOffset + (18 * row);

        return row <= rowCount ? base : row <= rowCount + 3 ? base + 13 : base + 17;
    }
}
