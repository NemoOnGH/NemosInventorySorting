package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

public class DropAllButton extends AbstractSortButton {

    private final ResourceLocation buttonTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "drop_all_button");
    private final ResourceLocation buttonHoverTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "drop_all_button_highlighted");

    public DropAllButton(Builder<DropAllButton> builder) {
        super(builder);
    }

    @Override
    protected ResourceLocation getButtonHoverTexture() {
        return buttonHoverTexture;
    }

    @Override
    protected ResourceLocation getButtonTexture() {
        return buttonTexture;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        dropAllItems();
    }

    private void dropAllItems() {
        Minecraft minecraft = Minecraft.getInstance();
        MultiPlayerGameMode gameMode = minecraft.gameMode;
        LocalPlayer player = minecraft.player;
        AbstractContainerMenu menu = containerScreen.getMenu();
        int containerId = menu.containerId;

        List<Integer> slotItems = getItemSlots(menu);

        for (Integer slotIndex : slotItems) {
            if (gameMode != null && player != null) {
                gameMode.handleInventoryMouseClick(containerId, slotIndex, 1, ClickType.THROW, player);
            }

        }

    }

    private @NotNull List<Integer> getItemSlots(AbstractContainerMenu menu) {
        NonNullList<Slot> slots = menu.slots;

        return IntStream.range(startIndex, endIndex)
                .mapToObj(slotIndex -> Map.entry(slotIndex, slots.get(slotIndex).getItem()))
                .filter(itemStackEntry -> !itemStackEntry.getValue().is(Items.AIR))
                .map(Map.Entry::getKey)
                .toList();
    }
}
