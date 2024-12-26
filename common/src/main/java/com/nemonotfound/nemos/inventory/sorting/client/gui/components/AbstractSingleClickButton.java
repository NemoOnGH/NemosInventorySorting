package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public abstract class AbstractSingleClickButton<T extends AbstractSortButton> extends AbstractSortButton {

    public AbstractSingleClickButton(Builder<T> builder) {
        super(builder);
    }

    protected void interactWithAllItems(ClickType clickType, int button) {
        Minecraft minecraft = Minecraft.getInstance();
        MultiPlayerGameMode gameMode = minecraft.gameMode;
        LocalPlayer player = minecraft.player;
        AbstractContainerMenu menu = containerScreen.getMenu();
        int containerId = menu.containerId;
        boolean isCreativeModeMenu = menu instanceof CreativeModeInventoryScreen.ItemPickerMenu;

        List<Integer> slotItems = getItemSlots(menu);

        if (gameMode != null && player != null) {
            Consumer<Integer> function = isCreativeModeMenu ?
                    (slotIndex) -> menu.clicked(slotIndex, button, clickType, player) :
                    (slotIndex) -> gameMode.handleInventoryMouseClick(containerId, slotIndex, button, clickType, player);

            triggerClickForAllItems(slotItems, function);
        }
    }

    private void triggerClickForAllItems(List<Integer> slotItems, Consumer<Integer> function) {
        for (Integer slotIndex : slotItems) {
            function.accept(slotIndex);
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
