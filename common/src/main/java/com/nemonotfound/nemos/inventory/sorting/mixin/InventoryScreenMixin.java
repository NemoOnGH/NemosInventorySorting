package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.ModKeyMappings;
import com.nemonotfound.nemos.inventory.sorting.config.model.ComponentConfig;
import com.nemonotfound.nemos.inventory.sorting.config.service.ConfigService;
import com.nemonotfound.nemos.inventory.sorting.factory.ButtonCreator;
import com.nemonotfound.nemos.inventory.sorting.factory.DropAllButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.factory.SortAlphabeticallyButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.factory.SortAlphabeticallyDescendingButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.gui.components.buttons.AbstractInventoryButton;
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

import static com.nemonotfound.nemos.inventory.sorting.config.DefaultConfigValues.*;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractRecipeBookScreen<InventoryMenu> {

    @Unique
    private final Map<KeyMapping, AbstractInventoryButton> nemosInventorySorting$keyMappingButtonMap = new HashMap<>();
    @Unique
    private final ConfigService nemosInventorySorting$configService = ConfigService.getInstance();

    public InventoryScreenMixin(InventoryMenu menu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component component) {
        super(menu, recipeBookComponent, inventory, component);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        SortAlphabeticallyButtonFactory sortAlphabeticallyButtonFactory = SortAlphabeticallyButtonFactory.getInstance();
        SortAlphabeticallyDescendingButtonFactory sortAlphabeticallyDescendingButtonFactory = SortAlphabeticallyDescendingButtonFactory.getInstance();
        DropAllButtonFactory dropAllButtonFactory = DropAllButtonFactory.getInstance();

        var configs = nemosInventorySorting$configService.readOrGetDefaultComponentConfigs();

        nemosInventorySorting$createButton(configs, SORT_ALPHABETICALLY_DESCENDING_INVENTORY, ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING_INVENTORY.get(), sortAlphabeticallyDescendingButtonFactory, Y_OFFSET_INVENTORY);
        nemosInventorySorting$createButton(configs, SORT_ALPHABETICALLY_INVENTORY, ModKeyMappings.SORT_ALPHABETICALLY_INVENTORY.get(), sortAlphabeticallyButtonFactory, Y_OFFSET_INVENTORY);
        nemosInventorySorting$createButton(configs, DROP_ALL_INVENTORY, ModKeyMappings.DROP_ALL_INVENTORY.get(), dropAllButtonFactory, Y_OFFSET_INVENTORY);

        for (AbstractInventoryButton button : nemosInventorySorting$keyMappingButtonMap.values()) {
            this.addRenderableWidget(button);
        }
    }

    @Unique
    private void nemosInventorySorting$createButton(List<ComponentConfig> configsList, String componentName, KeyMapping keyMapping, ButtonCreator buttonCreator, int defaultYOffset) {
        var optionalComponentConfig = nemosInventorySorting$configService.getOrDefaultComponentConfigs(configsList, componentName);

        if (optionalComponentConfig.isEmpty()) {
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
                getMenu()
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
        for (AbstractInventoryButton button : nemosInventorySorting$keyMappingButtonMap.values()) {
            button.setIsShiftKeyDown(isShiftDown);
            button.setTooltip(this.getMenu());
        }
    }
}