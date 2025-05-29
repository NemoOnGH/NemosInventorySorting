package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.ModKeyMappings;
import com.nemonotfound.nemos.inventory.sorting.config.model.ComponentConfig;
import com.nemonotfound.nemos.inventory.sorting.config.service.ConfigService;
import com.nemonotfound.nemos.inventory.sorting.factory.*;
import com.nemonotfound.nemos.inventory.sorting.gui.components.FilterBox;
import com.nemonotfound.nemos.inventory.sorting.gui.components.buttons.AbstractInventoryButton;
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

import static com.nemonotfound.nemos.inventory.sorting.config.DefaultConfigValues.*;

@Mixin(ShulkerBoxScreen.class)
public abstract class ShulkerBoxScreenMixin extends AbstractContainerScreen<ShulkerBoxMenu> {

    @Unique
    private final Map<KeyMapping, AbstractInventoryButton> nemosInventorySorting$keyMappingButtonMap = new HashMap<>();
    @Unique
    private final ConfigService nemosInventorySorting$configService = ConfigService.getInstance();
    
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

        var configs = nemosInventorySorting$configService.readOrGetDefaultComponentConfigs();

        nemosInventorySorting$createContainerButton(configs, SORT_ALPHABETICALLY_CONTAINER, ModKeyMappings.SORT_ALPHABETICALLY.get(), sortAlphabeticallyButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createContainerButton(configs, SORT_ALPHABETICALLY_DESCENDING_CONTAINER, ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING.get(), sortAlphabeticallyDescendingButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createContainerButton(configs, MOVE_SAME_CONTAINER, ModKeyMappings.MOVE_SAME.get(), moveSameButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createContainerButton(configs, MOVE_ALL_CONTAINER, ModKeyMappings.MOVE_ALL.get(), moveAllButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createContainerButton(configs, DROP_ALL_CONTAINER, ModKeyMappings.DROP_ALL.get(), dropAllButtonFactory, Y_OFFSET_CONTAINER);

        nemosInventorySorting$createInventoryButton(configs, SORT_ALPHABETICALLY_CONTAINER_INVENTORY, ModKeyMappings.SORT_ALPHABETICALLY_INVENTORY.get(), sortAlphabeticallyButtonFactory, yOffsetInventory);
        nemosInventorySorting$createInventoryButton(configs, SORT_ALPHABETICALLY_DESCENDING_CONTAINER_INVENTORY, ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING_INVENTORY.get(), sortAlphabeticallyDescendingButtonFactory, yOffsetInventory);
        nemosInventorySorting$createInventoryButton(configs, MOVE_SAME_CONTAINER_INVENTORY, ModKeyMappings.MOVE_SAME_INVENTORY.get(), moveSameButtonFactory, yOffsetInventory);
        nemosInventorySorting$createInventoryButton(configs, MOVE_ALL_CONTAINER_INVENTORY, ModKeyMappings.MOVE_ALL_INVENTORY.get(), moveAllButtonFactory, yOffsetInventory);
        nemosInventorySorting$createInventoryButton(configs, DROP_ALL_CONTAINER_INVENTORY, ModKeyMappings.DROP_ALL_INVENTORY.get(), dropAllButtonFactory, yOffsetInventory);


        for (AbstractInventoryButton button : nemosInventorySorting$keyMappingButtonMap.values()) {
            this.addRenderableWidget(button);
        }
    }

    @Unique
    private void nemosInventorySorting$createInventoryButton(List<ComponentConfig> configs, String componentName, KeyMapping keyMapping, ButtonCreator buttonCreator, int defaultYOffset) {
        var startIndex = 27;
        var endIndex = 54;

        nemosInventorySorting$createButton(configs, componentName, keyMapping, buttonCreator, startIndex, endIndex, defaultYOffset);
    }

    @Unique
    private void nemosInventorySorting$createContainerButton(List<ComponentConfig> configs, String componentName, KeyMapping keyMapping, ButtonCreator buttonCreator, int defaultYOffset) {
        var startIndex = 0;
        var endIndex = 27;

        nemosInventorySorting$createButton(configs, componentName, keyMapping, buttonCreator, startIndex, endIndex, defaultYOffset);
    }

    @Unique
    private void nemosInventorySorting$createButton(List<ComponentConfig> configs, String componentName, KeyMapping keyMapping, ButtonCreator buttonCreator, int startIndex, int endIndex, int defaultYOffset) {
        var optionalComponentConfig = nemosInventorySorting$configService.getOrDefaultComponentConfig(configs, componentName);

        if (optionalComponentConfig.isEmpty()) {
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
        var sortButton = buttonCreator.createButton(startIndex, endIndex, leftPos, topPos, xOffset, yOffset, width, height, getMenu());
        nemosInventorySorting$keyMappingButtonMap.put(keyMapping, sortButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        var optionalButtonEntry = nemosInventorySorting$keyMappingButtonMap.entrySet().stream()
                .filter(entry -> entry.getKey().matches(keyCode, scanCode))
                .findFirst();
        var optionalFilterBox = children().stream()
                .filter(widget -> widget instanceof FilterBox)
                .findFirst();

        if (keyCode == 340) {
            nemosInventorySorting$updateToolTips(true);
        } else if (optionalFilterBox.isPresent() && optionalFilterBox.get().isFocused()) {
            return super.keyPressed(keyCode, scanCode, modifiers);
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        var optionalButtonEntry = nemosInventorySorting$keyMappingButtonMap.entrySet().stream()
                .filter(entry -> entry.getKey().matchesMouse(button))
                .findFirst();

        optionalButtonEntry.ifPresent(entry -> {
            var sortButton = entry.getValue();

            sortButton.playDownSound(Minecraft.getInstance().getSoundManager());
            sortButton.onClick(0, 0);
        });

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Unique
    private void nemosInventorySorting$updateToolTips(boolean isShiftDown) {
        for (AbstractInventoryButton button : nemosInventorySorting$keyMappingButtonMap.values()) {
            button.setIsShiftKeyDown(isShiftDown);
            button.setTooltip(this.getMenu());
        }
    }
}