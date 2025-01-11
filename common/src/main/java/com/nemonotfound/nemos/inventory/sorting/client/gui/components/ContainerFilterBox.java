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
    private static final ResourceLocation HIGHLIGHTED_SLOT = ResourceLocation.fromNamespaceAndPath(MOD_ID, "container/highlighted_slot");
    private static final ResourceLocation DIMMED_SLOT = ResourceLocation.fromNamespaceAndPath(MOD_ID, "container/dimmed_slot");

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

        markSlots(filteredSlotMap.get(true), leftPosOffset, topPosOffset, guiGraphics, HIGHLIGHTED_SLOT);
        markSlots(filteredSlotMap.get(false), leftPosOffset, topPosOffset, guiGraphics, DIMMED_SLOT);
    }

    private boolean filterForItemName(Slot slot, String filter) {
        var slotItem = slot.getItem();

        return !slotItem.is(Items.AIR) && slotItem.getItemName().getString().toLowerCase().contains(filter.toLowerCase());
    }

    private void markSlots(
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

            guiGraphics.blitSprite(RenderType::guiTextured, texture, xPos, yPos, 16, 16);
        }
    }

    private Integer calculateYPos(int rowCount, int row, int topPosOffset) {
        var base = topPosOffset + (18 * row);

        return row <= rowCount ? base : row <= rowCount + 3 ? base + 13 : base + 17;
    }
}
