package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.client.ModKeyMappings;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import com.nemonotfound.nemos.inventory.sorting.factory.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(ShulkerBoxScreen.class)
public abstract class ShulkerBoxScreenMixin extends AbstractContainerScreen<ShulkerBoxMenu> {

    @Unique
    private final Map<KeyMapping, AbstractSortButton> nemosInventorySorting$keyMappingButtonMap = new HashMap<>();
    
    public ShulkerBoxScreenMixin(ShulkerBoxMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void init() {
        super.init();

        var xOffsetFirstButton = 22;
        var xOffsetSecondButton = 40;
        var xOffsetThirdButton = 58;
        var xOffsetFourthButton = 76;
        var xOffsetFifthButton = 94;
        var yOffsetInventory = 71;
        var yOffsetContainer = 5;
        var size = 11;

        SortAlphabeticallyButtonFactory sortAlphabeticallyButtonFactory = SortAlphabeticallyButtonFactory.getInstance();
        SortAlphabeticallyDescendingButtonFactory sortAlphabeticallyDescendingButtonFactory = SortAlphabeticallyDescendingButtonFactory.getInstance();
        DropAllButtonFactory dropAllButtonFactory = DropAllButtonFactory.getInstance();
        MoveSameButtonFactory moveSameButtonFactory = MoveSameButtonFactory.getInstance();
        MoveAllButtonFactory moveAllButtonFactory = MoveAllButtonFactory.getInstance();

        nemosInventorySorting$createContainerButton(ModKeyMappings.SORT_ALPHABETICALLY.get(), sortAlphabeticallyButtonFactory, xOffsetSecondButton, yOffsetContainer, size);
        nemosInventorySorting$createContainerButton(ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING.get(), sortAlphabeticallyDescendingButtonFactory, xOffsetFirstButton, yOffsetContainer, size);
        nemosInventorySorting$createContainerButton(ModKeyMappings.MOVE_SAME.get(), moveSameButtonFactory, xOffsetThirdButton, yOffsetContainer, size);
        nemosInventorySorting$createContainerButton(ModKeyMappings.MOVE_ALL.get(), moveAllButtonFactory, xOffsetFourthButton, yOffsetContainer, size);
        nemosInventorySorting$createContainerButton(ModKeyMappings.DROP_ALL.get(), dropAllButtonFactory, xOffsetFifthButton, yOffsetContainer, size);

        nemosInventorySorting$createInventoryButton(ModKeyMappings.SORT_ALPHABETICALLY_INVENTORY.get(), sortAlphabeticallyButtonFactory, xOffsetSecondButton, yOffsetInventory, size);
        nemosInventorySorting$createInventoryButton(ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING_INVENTORY.get(), sortAlphabeticallyDescendingButtonFactory, xOffsetFirstButton, yOffsetInventory, size);
        nemosInventorySorting$createInventoryButton(ModKeyMappings.MOVE_SAME_INVENTORY.get(), moveSameButtonFactory, xOffsetThirdButton, yOffsetInventory, size);
        nemosInventorySorting$createInventoryButton(ModKeyMappings.MOVE_ALL_INVENTORY.get(), moveAllButtonFactory, xOffsetFourthButton, yOffsetInventory, size);
        nemosInventorySorting$createInventoryButton(ModKeyMappings.DROP_ALL_INVENTORY.get(), dropAllButtonFactory, xOffsetFifthButton, yOffsetInventory, size);


        for (AbstractSortButton button : nemosInventorySorting$keyMappingButtonMap.values()) {
            this.addRenderableWidget(button);
        }
    }

    @Unique
    private void nemosInventorySorting$createContainerButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int xOffset, int yOffset, int size) {
        nemosInventorySorting$createButton(keyMapping, buttonCreator, 0, 27, xOffset, yOffset, size);
    }

    @Unique
    private void nemosInventorySorting$createInventoryButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int xOffset, int yOffset, int size) {
        nemosInventorySorting$createButton(keyMapping, buttonCreator, 27, 54, xOffset, yOffset, size);
    }

    @Unique
    private void nemosInventorySorting$createButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int startIndex, int endIndex, int xOffset, int yOffset, int size) {
        var sortButton = buttonCreator.createButton(startIndex, endIndex, leftPos, topPos, xOffset, yOffset, imageWidth, size, size, this);
        nemosInventorySorting$keyMappingButtonMap.put(keyMapping, sortButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        var optionalButtonEntry = nemosInventorySorting$keyMappingButtonMap.entrySet().stream()
                .filter(entry -> entry.getKey().matches(keyCode, scanCode))
                .findFirst();

        if (keyCode == 340) {
            nemosInventorySorting$updateToolTips(true);
        } else {
            optionalButtonEntry.ifPresent(entry -> {
                var button = entry.getValue();

                button.playDownSound(Minecraft.getInstance().getSoundManager());
                button.onClick(0, 0);
            });
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 340) {
            nemosInventorySorting$updateToolTips(false);
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Unique
    private void nemosInventorySorting$updateToolTips(boolean isShiftDown) {
        for (AbstractSortButton button : nemosInventorySorting$keyMappingButtonMap.values()) {
            button.setIsShiftKeyDown(isShiftDown);
            button.setTooltip(this.getMenu());
        }
    }
}