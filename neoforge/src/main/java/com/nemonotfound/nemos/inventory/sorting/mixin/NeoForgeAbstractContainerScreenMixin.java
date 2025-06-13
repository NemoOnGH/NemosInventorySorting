package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.ModKeyMappings;
import com.nemonotfound.nemos.inventory.sorting.config.model.ComponentConfig;
import com.nemonotfound.nemos.inventory.sorting.config.service.ConfigService;
import com.nemonotfound.nemos.inventory.sorting.factory.*;
import com.nemonotfound.nemos.inventory.sorting.gui.components.buttons.AbstractInventoryButton;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestMenu;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nemonotfound.nemos.inventory.sorting.NemosInventorySortingClientCommon.MOD_LOADER_HELPER;
import static com.nemonotfound.nemos.inventory.sorting.config.DefaultConfigValues.*;

//TODO: Refactor
@Mixin(AbstractContainerScreen.class)
public abstract class NeoForgeAbstractContainerScreenMixin extends Screen {

    @Shadow
    protected int leftPos;
    @Shadow
    protected int topPos;

    @Shadow
    public abstract AbstractContainerMenu getMenu();

    @Shadow protected int imageHeight;
    @Shadow protected int imageWidth;
    @Unique
    private int nemosInventorySortingNeoForge$inventoryEndIndex;
    @Unique
    private int nemosInventorySortingNeoForge$containerSize;
    @Unique
    private int nemosInventorySorting$latestInventoryXOffset = 0;
    @Unique
    private int nemosInventorySorting$latestStorageContainerXOffset = 0;

    @Unique
    private final ConfigService nemosInventorySortingNeoForge$configService = ConfigService.getInstance();
    @Unique
    private final Map<KeyMapping, AbstractWidget> nemosInventorySortingNeoForge$keyMappingButtonMap = new HashMap<>();

    protected NeoForgeAbstractContainerScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    public void init(CallbackInfo ci) {
        nemosInventorySortingNeoForge$inventoryEndIndex = getMenu().slots.size() - 9;
        nemosInventorySortingNeoForge$containerSize = nemosInventorySortingNeoForge$inventoryEndIndex - 27;
        nemosInventorySorting$latestStorageContainerXOffset = imageWidth - 8;
        nemosInventorySorting$latestInventoryXOffset = imageWidth - 8;

        if (nemosInventorySorting$shouldHaveStorageContainerButtons()) {
            nemosInventorySorting$initStorageContainerButtons();
        }

        for (AbstractWidget widget : nemosInventorySortingNeoForge$keyMappingButtonMap.values()) {
            this.addRenderableWidget(widget);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        var optionalButtonEntry = nemosInventorySortingNeoForge$keyMappingButtonMap.entrySet().stream()
                .filter(entry -> entry.getKey().matches(keyCode, scanCode))
                .findFirst();

        if (keyCode == 340) {
            nemosInventorySorting$updateToolTips(true);
        } else { //TODO: Refactor to add logic in keyPressed
            optionalButtonEntry.ifPresent(entry -> {
                var button = entry.getValue();

                button.playDownSound(Minecraft.getInstance().getSoundManager());
                button.onClick(0, 0);
            });
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (nemosInventorySorting$shouldHaveStorageContainerButtons() && keyCode == 340) {
            nemosInventorySorting$updateToolTips(false);
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        var optionalButtonEntry = nemosInventorySortingNeoForge$keyMappingButtonMap.entrySet().stream()
                .filter(entry -> entry.getKey().matchesMouse(button))
                .findFirst();

        optionalButtonEntry.ifPresent(entry -> {
            var sortButton = entry.getValue();

            sortButton.playDownSound(Minecraft.getInstance().getSoundManager());
            sortButton.onClick(0, 0);
        });
    }

    @Unique
    private boolean nemosInventorySorting$shouldHaveStorageContainerButtons() {
        var menu = getMenu();
        var isIronChestLoaded = MOD_LOADER_HELPER.isModLoaded("ironchest");

        return isIronChestLoaded && (menu instanceof IronChestMenu ironChestMenu && ironChestMenu.getChestType() != IronChestsTypes.DIRT);
    }

    @Unique
    private void nemosInventorySorting$initStorageContainerButtons() {
        var sortAlphabeticallyButtonFactory = SortAlphabeticallyButtonFactory.getInstance();
        var sortAlphabeticallyDescendingButtonFactory = SortAlphabeticallyDescendingButtonFactory.getInstance();
        var dropAllButtonFactory = DropAllButtonFactory.getInstance();
        var moveSameButtonFactory = MoveSameButtonFactory.getInstance();
        var moveAllButtonFactory = MoveAllButtonFactory.getInstance();
        var configs = nemosInventorySortingNeoForge$configService.readOrGetDefaultIronChestComponentConfigs();
        var yOffset = imageHeight - 96;

        nemosInventorySorting$createButtonForContainer(configs, SORT_ALPHABETICALLY_STORAGE_CONTAINER, ModKeyMappings.SORT_ALPHABETICALLY.get(), sortAlphabeticallyButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createButtonForContainer(configs, SORT_ALPHABETICALLY_DESCENDING_STORAGE_CONTAINER, ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING.get(), sortAlphabeticallyDescendingButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createButtonForContainer(configs, MOVE_SAME_STORAGE_CONTAINER, ModKeyMappings.MOVE_SAME.get(), moveSameButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createButtonForContainer(configs, MOVE_ALL_STORAGE_CONTAINER, ModKeyMappings.MOVE_ALL.get(), moveAllButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createButtonForContainer(configs, DROP_ALL_STORAGE_CONTAINER, ModKeyMappings.DROP_ALL.get(), dropAllButtonFactory, Y_OFFSET_CONTAINER);

        nemosInventorySorting$createButtonForInventory(configs, SORT_ALPHABETICALLY_STORAGE_CONTAINER_INVENTORY, ModKeyMappings.SORT_ALPHABETICALLY_INVENTORY.get(), sortAlphabeticallyButtonFactory, yOffset);
        nemosInventorySorting$createButtonForInventory(configs, SORT_ALPHABETICALLY_DESCENDING_STORAGE_CONTAINER_INVENTORY, ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING_INVENTORY.get(), sortAlphabeticallyDescendingButtonFactory, yOffset);
        nemosInventorySorting$createButtonForInventory(configs, MOVE_SAME_STORAGE_CONTAINER_INVENTORY, ModKeyMappings.MOVE_SAME_INVENTORY.get(), moveSameButtonFactory, yOffset);
        nemosInventorySorting$createButtonForInventory(configs, MOVE_ALL_STORAGE_CONTAINER_INVENTORY, ModKeyMappings.MOVE_ALL_INVENTORY.get(), moveAllButtonFactory, yOffset);
        nemosInventorySorting$createButtonForInventory(configs, DROP_ALL_STORAGE_CONTAINER_INVENTORY, ModKeyMappings.DROP_ALL_INVENTORY.get(), dropAllButtonFactory, yOffset);
    }

    @Unique
    private void nemosInventorySorting$createButtonForContainer(List<ComponentConfig> configs, String componentName, KeyMapping keyMapping, ButtonCreator buttonCreator, int defaultYOffset) {
        var optionalComponentConfig = nemosInventorySortingNeoForge$configService.getOrDefaultComponentConfig(configs, componentName);

        if (optionalComponentConfig.isEmpty()) {
            return;
        }

        var config = optionalComponentConfig.get();

        if (!config.isEnabled()) {
            return;
        }

        var width = config.width();
        var xOffset = config.xOffset() != null ? config.xOffset() : nemosInventorySorting$latestStorageContainerXOffset - width - 7;
        var yOffset = config.yOffset() != null ? config.yOffset() : defaultYOffset;

        nemosInventorySorting$latestStorageContainerXOffset = xOffset;
        nemosInventorySorting$createContainerButton(keyMapping, buttonCreator, xOffset, yOffset, width, config.height());
    }

    @Unique
    private void nemosInventorySorting$createButtonForInventory(List<ComponentConfig> configs, String componentName, KeyMapping keyMapping, ButtonCreator buttonCreator, int defaultYOffset) {
        var optionalComponentConfig = nemosInventorySortingNeoForge$configService.getOrDefaultComponentConfig(configs, componentName);

        if (optionalComponentConfig.isEmpty()) {
            return;
        }

        var config = optionalComponentConfig.get();

        if (!config.isEnabled()) {
            return;
        }

        var width = config.width();
        var xOffset = config.xOffset() != null ? config.xOffset() : nemosInventorySorting$latestInventoryXOffset - width - 7;
        var yOffset = config.yOffset() != null ? config.yOffset() : defaultYOffset;

        nemosInventorySorting$latestInventoryXOffset = xOffset;
        nemosInventorySorting$createInventoryButton(keyMapping, buttonCreator, xOffset, yOffset, width, config.height());
    }

    @Unique
    private void nemosInventorySorting$createContainerButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int xOffset, int yOffset, int width, int height) {
        nemosInventorySorting$createButton(keyMapping, buttonCreator, 0, nemosInventorySortingNeoForge$containerSize, xOffset, yOffset, width, height);
    }

    @Unique
    private void nemosInventorySorting$createInventoryButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int xOffset, int yOffset, int width, int height) {
        nemosInventorySorting$createButton(keyMapping, buttonCreator, nemosInventorySortingNeoForge$containerSize, nemosInventorySortingNeoForge$inventoryEndIndex, xOffset, yOffset, width, height);
    }

    @Unique
    private void nemosInventorySorting$createButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int startIndex, int endIndex, int xOffset, int yOffset, int width, int height) {
        var sortButton = buttonCreator.createButton(startIndex, endIndex, leftPos, topPos, xOffset, yOffset, width, height, getMenu());
        nemosInventorySortingNeoForge$keyMappingButtonMap.put(keyMapping, sortButton);
    }

    //TODO: Adapt Render for AbstractWidget, to use shiftableTooltip
    @Unique
    private void nemosInventorySorting$updateToolTips(boolean isShiftDown) {
        for (AbstractWidget widget : nemosInventorySortingNeoForge$keyMappingButtonMap.values()) {
            if (widget instanceof AbstractInventoryButton button) {
                button.setIsShiftKeyDown(isShiftDown);
                button.setTooltip(getMenu());
            }
        }
    }
}
