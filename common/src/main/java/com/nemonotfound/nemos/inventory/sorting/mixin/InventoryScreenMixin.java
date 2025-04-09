package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.client.ModKeyMappings;
import com.nemonotfound.nemos.inventory.sorting.client.config.ComponentConfig;
import com.nemonotfound.nemos.inventory.sorting.client.config.ConfigUtil;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import com.nemonotfound.nemos.inventory.sorting.factory.ButtonCreator;
import com.nemonotfound.nemos.inventory.sorting.factory.DropAllButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.factory.SortAlphabeticallyButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.factory.SortAlphabeticallyDescendingButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.interfaces.GuiPosition;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nemonotfound.nemos.inventory.sorting.Constants.*;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractRecipeBookScreen<InventoryMenu> implements GuiPosition {

    @Unique
    private final Map<KeyMapping, AbstractSortButton> nemosInventorySorting$keyMappingButtonMap = new HashMap<>();

    public InventoryScreenMixin(InventoryMenu menu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component component) {
        super(menu, recipeBookComponent, inventory, component);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        SortAlphabeticallyButtonFactory sortAlphabeticallyButtonFactory = SortAlphabeticallyButtonFactory.getInstance();
        SortAlphabeticallyDescendingButtonFactory sortAlphabeticallyDescendingButtonFactory = SortAlphabeticallyDescendingButtonFactory.getInstance();
        DropAllButtonFactory dropAllButtonFactory = DropAllButtonFactory.getInstance();

        var configs = ConfigUtil.readConfigs();

        nemosInventorySorting$createButton(configs, SORT_ALPHABETICALLY_DESCENDING_INVENTORY, ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING_INVENTORY.get(), sortAlphabeticallyDescendingButtonFactory, X_OFFSET_SORT_ALPHABETICALLY_DESCENDING_INVENTORY, Y_OFFSET_INVENTORY, BUTTON_SIZE, BUTTON_SIZE);
        nemosInventorySorting$createButton(configs, SORT_ALPHABETICALLY_INVENTORY, ModKeyMappings.SORT_ALPHABETICALLY_INVENTORY.get(), sortAlphabeticallyButtonFactory, X_OFFSET_SORT_ALPHABETICALLY_INVENTORY, Y_OFFSET_INVENTORY, BUTTON_SIZE, BUTTON_SIZE);
        nemosInventorySorting$createButton(configs, DROP_ALL_INVENTORY, ModKeyMappings.DROP_ALL_INVENTORY.get(), dropAllButtonFactory, X_OFFSET_DROP_ALL_INVENTORY, Y_OFFSET_INVENTORY, BUTTON_SIZE, BUTTON_SIZE);

        for (AbstractSortButton button : nemosInventorySorting$keyMappingButtonMap.values()) {
            this.addRenderableWidget(button);
        }
    }

    @Unique
    private void nemosInventorySorting$createButton(List<ComponentConfig> configsList, String componentName, KeyMapping keyMapping, ButtonCreator buttonCreator, int defaultXOffset, int defaultYOffset, int defaultWidth, int defaultHeight) {
        var optionalComponentConfig = ConfigUtil.getConfigs(configsList, componentName);

        if (optionalComponentConfig.isEmpty()) {
            nemosInventorySorting$createButton(keyMapping, buttonCreator, defaultXOffset, defaultYOffset, defaultWidth, defaultHeight);
            return;
        }

        var config = optionalComponentConfig.get();

        if (!config.isEnabled()) {
            return;
        }

        var yOffset = config.yOffset() != null ? config.yOffset() : defaultYOffset;
        nemosInventorySorting$createButton(keyMapping, buttonCreator, config.xOffset(), yOffset, config.width(), config.height());
    }

    @Unique
    private void nemosInventorySorting$createButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int xOffset, int yOffset, int width, int height) {
        var nemosInventorySorting$startIndex = 9;
        var nemosInventorySorting$endIndex = 36;
        var sortButton = buttonCreator.createButton(
                nemosInventorySorting$startIndex,
                nemosInventorySorting$endIndex,
                leftPos,
                topPos,
                xOffset,
                yOffset,
                width,
                height,
                this
        );
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
        for (AbstractSortButton button : nemosInventorySorting$keyMappingButtonMap.values()) {
            button.setIsShiftKeyDown(isShiftDown);
            button.setTooltip(this.getMenu());
        }
    }

    @Override
    public int nemosInventorySorting$getLeftPos() {
        return this.leftPos;
    }
}