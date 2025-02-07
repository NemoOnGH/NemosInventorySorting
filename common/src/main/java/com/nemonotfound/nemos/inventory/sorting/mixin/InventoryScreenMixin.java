package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nemonotfound.nemos.inventory.sorting.client.ModKeyMappings;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import com.nemonotfound.nemos.inventory.sorting.factory.ButtonCreator;
import com.nemonotfound.nemos.inventory.sorting.factory.DropAllButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.factory.SortAlphabeticallyButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.factory.SortAlphabeticallyDescendingButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.interfaces.GuiPosition;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> implements GuiPosition {

    @Unique
    private final Map<KeyMapping, AbstractSortButton> nemosInventorySorting$keyMappingButtonMap = new HashMap<>();

    public InventoryScreenMixin(InventoryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        int xOffsetFirstButton = 18;
        int xOffsetSecondButton = 33;
        int xOffsetThirdButton = 48;

        int size = 11;

        SortAlphabeticallyButtonFactory sortAlphabeticallyButtonFactory = SortAlphabeticallyButtonFactory.getInstance();
        SortAlphabeticallyDescendingButtonFactory sortAlphabeticallyDescendingButtonFactory = SortAlphabeticallyDescendingButtonFactory.getInstance();
        DropAllButtonFactory dropAllButtonFactory = DropAllButtonFactory.getInstance();

        nemosInventorySorting$createButton(ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING_INVENTORY.get(), sortAlphabeticallyDescendingButtonFactory, xOffsetFirstButton, size);
        nemosInventorySorting$createButton(ModKeyMappings.SORT_ALPHABETICALLY_INVENTORY.get(), sortAlphabeticallyButtonFactory, xOffsetSecondButton, size);
        nemosInventorySorting$createButton(ModKeyMappings.DROP_ALL_INVENTORY.get(), dropAllButtonFactory, xOffsetThirdButton, size);

        for (AbstractSortButton button : nemosInventorySorting$keyMappingButtonMap.values()) {
            this.addRenderableWidget(button);
        }
    }

    @Unique
    private void nemosInventorySorting$createButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int xOffset, int size) {
        var nemosInventorySorting$startIndex = 9;
        var nemosInventorySorting$endIndex = 36;
        var nemosInventorySorting$yOffset = 65;
        var sortButton = buttonCreator.createButton(
                nemosInventorySorting$startIndex,
                nemosInventorySorting$endIndex,
                leftPos,
                topPos,
                xOffset,
                nemosInventorySorting$yOffset,
                imageWidth,
                size,
                size,
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

    @Inject(method = "render", at = @At("TAIL"))
    private void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        var button = nemosInventorySorting$keyMappingButtonMap.values().stream()
                .filter(AbstractWidget::isHoveredOrFocused)
                .findAny();

        button.ifPresent(sortButton -> this.renderTooltip(poseStack, sortButton.getButtonName(menu), mouseX, mouseY));
    }

    @Unique
    private void nemosInventorySorting$updateToolTips(boolean isShiftDown) {
        for (AbstractSortButton button : nemosInventorySorting$keyMappingButtonMap.values()) {
            button.setIsShiftKeyDown(isShiftDown);
        }
    }

    @Override
    public int nemosInventorySorting$getLeftPos() {
        return this.leftPos;
    }

    @Override
    public int nemosInventorySorting$getImageWidth() {
        return this.imageWidth;
    }
}