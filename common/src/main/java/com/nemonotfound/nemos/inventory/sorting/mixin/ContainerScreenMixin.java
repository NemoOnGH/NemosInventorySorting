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
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nemonotfound.nemos.inventory.sorting.config.DefaultConfigValues.*;

@Mixin(ContainerScreen.class)
public abstract class ContainerScreenMixin extends AbstractContainerScreen<ChestMenu> {

    @Shadow
    @Final
    private int containerRows;
    @Unique
    private final int nemosInventorySorting$containerSize = this.getMenu().getContainer().getContainerSize();
    @Unique
    private final int nemosInventorySorting$inventoryEndIndex = nemosInventorySorting$containerSize + 27;
    @Unique
    private final Map<KeyMapping, AbstractInventoryButton> nemosInventorySorting$keyMappingButtonMap = new HashMap<>();
    @Unique
    private final ConfigService nemosInventorySorting$configService = ConfigService.getInstance();

    public ContainerScreenMixin(ChestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void init() {
        super.init();
        nemosInventorySorting$initButtons();

        for (AbstractInventoryButton button : nemosInventorySorting$keyMappingButtonMap.values()) {
            this.addRenderableWidget(button);
        }
    }

    @Unique
    private void nemosInventorySorting$initButtons() {
        int yOffsetInventory = 18 + (containerRows * 18);

        var sortAlphabeticallyButtonFactory = SortAlphabeticallyButtonFactory.getInstance();
        var sortAlphabeticallyDescendingButtonFactory = SortAlphabeticallyDescendingButtonFactory.getInstance();
        var dropAllButtonFactory = DropAllButtonFactory.getInstance();
        var moveSameButtonFactory = MoveSameButtonFactory.getInstance();
        var moveAllButtonFactory = MoveAllButtonFactory.getInstance();
        var configs = nemosInventorySorting$configService.readOrGetDefaultComponentConfigs();

        nemosInventorySorting$createButtonForContainer(configs, SORT_ALPHABETICALLY_CONTAINER, ModKeyMappings.SORT_ALPHABETICALLY.get(), sortAlphabeticallyButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createButtonForContainer(configs, SORT_ALPHABETICALLY_DESCENDING_CONTAINER, ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING.get(), sortAlphabeticallyDescendingButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createButtonForContainer(configs, MOVE_SAME_CONTAINER, ModKeyMappings.MOVE_SAME.get(), moveSameButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createButtonForContainer(configs, MOVE_ALL_CONTAINER, ModKeyMappings.MOVE_ALL.get(), moveAllButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createButtonForContainer(configs, DROP_ALL_CONTAINER, ModKeyMappings.DROP_ALL.get(), dropAllButtonFactory, Y_OFFSET_CONTAINER);

        nemosInventorySorting$createButtonForInventory(configs, SORT_ALPHABETICALLY_CONTAINER_INVENTORY, ModKeyMappings.SORT_ALPHABETICALLY_INVENTORY.get(), sortAlphabeticallyButtonFactory, yOffsetInventory);
        nemosInventorySorting$createButtonForInventory(configs, SORT_ALPHABETICALLY_DESCENDING_CONTAINER_INVENTORY, ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING_INVENTORY.get(), sortAlphabeticallyDescendingButtonFactory, yOffsetInventory);
        nemosInventorySorting$createButtonForInventory(configs, MOVE_SAME_CONTAINER_INVENTORY, ModKeyMappings.MOVE_SAME_INVENTORY.get(), moveSameButtonFactory, yOffsetInventory);
        nemosInventorySorting$createButtonForInventory(configs, MOVE_ALL_CONTAINER_INVENTORY, ModKeyMappings.MOVE_ALL_INVENTORY.get(), moveAllButtonFactory, yOffsetInventory);
        nemosInventorySorting$createButtonForInventory(configs, DROP_ALL_CONTAINER_INVENTORY, ModKeyMappings.DROP_ALL_INVENTORY.get(), dropAllButtonFactory, yOffsetInventory);
    }

    @Unique
    private void nemosInventorySorting$createButtonForContainer(List<ComponentConfig> configs, String componentName, KeyMapping keyMapping, ButtonCreator buttonCreator, int defaultYOffset) {
        var optionalComponentConfig = nemosInventorySorting$configService.getOrDefaultComponentConfig(configs, componentName);

        if (optionalComponentConfig.isEmpty()) {
            return;
        }

        var config = optionalComponentConfig.get();

        if (!config.isEnabled()) {
            return;
        }

        var yOffset = config.yOffset() != null ? config.yOffset() : defaultYOffset;
        nemosInventorySorting$createContainerButton(keyMapping, buttonCreator, config.xOffset(), yOffset, config.width(), config.height());
    }

    @Unique
    private void nemosInventorySorting$createButtonForInventory(List<ComponentConfig> configs, String componentName, KeyMapping keyMapping, ButtonCreator buttonCreator, int defaultYOffset) {
        var optionalComponentConfig = nemosInventorySorting$configService.getOrDefaultComponentConfig(configs, componentName);

        if (optionalComponentConfig.isEmpty()) {
            return;
        }

        var config = optionalComponentConfig.get();

        if (!config.isEnabled()) {
            return;
        }

        var yOffset = config.yOffset() != null ? config.yOffset() : defaultYOffset;
        nemosInventorySorting$createInventoryButton(keyMapping, buttonCreator, config.xOffset(), yOffset, config.width(), config.height());
    }

    @Unique
    private void nemosInventorySorting$createContainerButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int xOffset, int yOffset, int width, int height) {
        nemosInventorySorting$createButton(keyMapping, buttonCreator, 0, nemosInventorySorting$containerSize, xOffset, yOffset, width, height);
    }

    @Unique
    private void nemosInventorySorting$createInventoryButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int xOffset, int yOffset, int width, int height) {
        nemosInventorySorting$createButton(keyMapping, buttonCreator, nemosInventorySorting$containerSize, nemosInventorySorting$inventoryEndIndex, xOffset, yOffset, width, height);
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