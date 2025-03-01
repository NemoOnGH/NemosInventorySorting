package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.client.ModKeyMappings;
import com.nemonotfound.nemos.inventory.sorting.client.config.ComponentConfig;
import com.nemonotfound.nemos.inventory.sorting.client.config.ConfigUtil;
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
import java.util.List;
import java.util.Map;

import static com.nemonotfound.nemos.inventory.sorting.Constants.*;

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
        var yOffsetInventory = 71;

        SortAlphabeticallyButtonFactory sortAlphabeticallyButtonFactory = SortAlphabeticallyButtonFactory.getInstance();
        SortAlphabeticallyDescendingButtonFactory sortAlphabeticallyDescendingButtonFactory = SortAlphabeticallyDescendingButtonFactory.getInstance();
        DropAllButtonFactory dropAllButtonFactory = DropAllButtonFactory.getInstance();
        MoveSameButtonFactory moveSameButtonFactory = MoveSameButtonFactory.getInstance();
        MoveAllButtonFactory moveAllButtonFactory = MoveAllButtonFactory.getInstance();

        var configs = ConfigUtil.readConfigs();

        nemosInventorySorting$createContainerButton(configs, SORT_ALPHABETICALLY_CONTAINER, ModKeyMappings.SORT_ALPHABETICALLY.get(), sortAlphabeticallyButtonFactory, X_OFFSET_SORT_ALPHABETICALLY_CONTAINER, Y_OFFSET_CONTAINER, BUTTON_SIZE, BUTTON_SIZE);
        nemosInventorySorting$createContainerButton(configs, SORT_ALPHABETICALLY_DESCENDING_CONTAINER, ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING.get(), sortAlphabeticallyDescendingButtonFactory, X_OFFSET_SORT_ALPHABETICALLY_DESCENDING_CONTAINER, Y_OFFSET_CONTAINER, BUTTON_SIZE, BUTTON_SIZE);
        nemosInventorySorting$createContainerButton(configs, MOVE_SAME_CONTAINER, ModKeyMappings.MOVE_SAME.get(), moveSameButtonFactory, X_OFFSET_MOVE_SAME_CONTAINER, Y_OFFSET_CONTAINER, BUTTON_SIZE, BUTTON_SIZE);
        nemosInventorySorting$createContainerButton(configs, MOVE_ALL_CONTAINER, ModKeyMappings.MOVE_ALL.get(), moveAllButtonFactory, X_OFFSET_MOVE_ALL_CONTAINER, Y_OFFSET_CONTAINER, BUTTON_SIZE, BUTTON_SIZE);
        nemosInventorySorting$createContainerButton(configs, DROP_ALL_CONTAINER, ModKeyMappings.DROP_ALL.get(), dropAllButtonFactory, X_OFFSET_DROP_ALL_CONTAINER, Y_OFFSET_CONTAINER, BUTTON_SIZE, BUTTON_SIZE);

        nemosInventorySorting$createInventoryButton(configs, SORT_ALPHABETICALLY_CONTAINER_INVENTORY, ModKeyMappings.SORT_ALPHABETICALLY_INVENTORY.get(), sortAlphabeticallyButtonFactory, X_OFFSET_SORT_ALPHABETICALLY_CONTAINER, yOffsetInventory, BUTTON_SIZE, BUTTON_SIZE);
        nemosInventorySorting$createInventoryButton(configs, SORT_ALPHABETICALLY_DESCENDING_CONTAINER_INVENTORY, ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING_INVENTORY.get(), sortAlphabeticallyDescendingButtonFactory, X_OFFSET_SORT_ALPHABETICALLY_DESCENDING_CONTAINER, yOffsetInventory, BUTTON_SIZE, BUTTON_SIZE);
        nemosInventorySorting$createInventoryButton(configs, MOVE_SAME_CONTAINER_INVENTORY, ModKeyMappings.MOVE_SAME_INVENTORY.get(), moveSameButtonFactory, X_OFFSET_MOVE_SAME_CONTAINER, yOffsetInventory, BUTTON_SIZE, BUTTON_SIZE);
        nemosInventorySorting$createInventoryButton(configs, MOVE_ALL_CONTAINER_INVENTORY, ModKeyMappings.MOVE_ALL_INVENTORY.get(), moveAllButtonFactory, X_OFFSET_MOVE_ALL_CONTAINER, yOffsetInventory, BUTTON_SIZE, BUTTON_SIZE);
        nemosInventorySorting$createInventoryButton(configs, DROP_ALL_CONTAINER_INVENTORY, ModKeyMappings.DROP_ALL_INVENTORY.get(), dropAllButtonFactory, X_OFFSET_DROP_ALL_CONTAINER, yOffsetInventory, BUTTON_SIZE, BUTTON_SIZE);


        for (AbstractSortButton button : nemosInventorySorting$keyMappingButtonMap.values()) {
            this.addRenderableWidget(button);
        }
    }

    @Unique
    private void nemosInventorySorting$createInventoryButton(List<ComponentConfig> configs, String componentName, KeyMapping keyMapping, ButtonCreator buttonCreator, int defaultXOffset, int defaultYOffset, int defaultWidth, int defaultHeight) {
        var startIndex = 27;
        var endIndex = 54;

        nemosInventorySorting$createButton(configs, componentName, keyMapping, buttonCreator, startIndex, endIndex, defaultXOffset, defaultYOffset, defaultWidth, defaultHeight);
    }

    @Unique
    private void nemosInventorySorting$createContainerButton(List<ComponentConfig> configs, String componentName, KeyMapping keyMapping, ButtonCreator buttonCreator, int defaultXOffset, int defaultYOffset, int defaultWidth, int defaultHeight) {
        var startIndex = 0;
        var endIndex = 27;

        nemosInventorySorting$createButton(configs, componentName, keyMapping, buttonCreator, startIndex, endIndex, defaultXOffset, defaultYOffset, defaultWidth, defaultHeight);
    }

    @Unique
    private void nemosInventorySorting$createButton(List<ComponentConfig> configs, String componentName, KeyMapping keyMapping, ButtonCreator buttonCreator, int startIndex, int endIndex, int defaultXOffset, int defaultYOffset, int defaultWidth, int defaultHeight) {
        var optionalComponentConfig = ConfigUtil.getConfigs(configs, componentName);

        if (optionalComponentConfig.isEmpty()) {
            nemosInventorySorting$createButton(keyMapping, buttonCreator, startIndex, endIndex, defaultXOffset, defaultYOffset, defaultWidth, defaultHeight);
            return;
        }

        var config = optionalComponentConfig.get();

        if (!config.isEnabled()) {
            return;
        }

        var yOffset = config.yOffset() != null ? config.yOffset() : defaultYOffset;
        nemosInventorySorting$createButton(keyMapping, buttonCreator, startIndex, endIndex, config.xOffset(), yOffset, config.width(), config.height());
    }

    @Unique
    private void nemosInventorySorting$createButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int startIndex, int endIndex, int xOffset, int yOffset, int width, int height) {
        var sortButton = buttonCreator.createButton(startIndex, endIndex, leftPos, topPos, xOffset, yOffset, width, height, this);
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