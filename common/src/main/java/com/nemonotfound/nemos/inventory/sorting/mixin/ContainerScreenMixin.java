package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nemonotfound.nemos.inventory.sorting.client.ModKeyMappings;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.AbstractSortButton;
import com.nemonotfound.nemos.inventory.sorting.factory.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ContainerScreen.class)
public abstract class ContainerScreenMixin extends AbstractContainerScreen<ChestMenu> {

    @Shadow @Final private int containerRows;
    @Unique
    private final int nemosInventorySorting$containerSize = this.getMenu().getContainer().getContainerSize();
    @Unique
    private final int nemosInventorySorting$inventoryEndIndex = nemosInventorySorting$containerSize + 27;
    @Unique
    private final Map<KeyMapping, AbstractSortButton> nemosInventorySorting$keyMappingButtonMap = new HashMap<>();

    public ContainerScreenMixin(ChestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void init() {
        super.init();

        nemosInventorySorting$initButtons();

        for (AbstractSortButton button : nemosInventorySorting$keyMappingButtonMap.values()) {
            this.addRenderableWidget(button);
        }
    }

    @Unique
    private void nemosInventorySorting$initButtons() {
        int xOffsetFirstButton = 22;
        int xOffsetSecondButton = 40;
        int xOffsetThirdButton = 58;
        int xOffsetFourthButton = 76;
        int xOffsetFifthButton = 94;
        int yOffsetInventory = 18 + (containerRows * 18);
        int yOffsetContainer = 5;
        int size = 11;

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
    }

    @Unique
    private void nemosInventorySorting$createContainerButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int xOffset, int yOffset, int size) {
        nemosInventorySorting$createButton(keyMapping, buttonCreator, 0, nemosInventorySorting$containerSize, xOffset, yOffset, size);
    }

    @Unique
    private void nemosInventorySorting$createInventoryButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int xOffset, int yOffset, int size) {
        nemosInventorySorting$createButton(keyMapping, buttonCreator, nemosInventorySorting$containerSize, nemosInventorySorting$inventoryEndIndex, xOffset, yOffset, size);
    }

    @Unique
    private void nemosInventorySorting$createButton(KeyMapping keyMapping, ButtonCreator buttonCreator, int startIndex, int endIndex, int xOffset, int yOffset, int size) {
        var sortButton = buttonCreator.createButton(startIndex, endIndex, leftPos, topPos, xOffset, yOffset, imageWidth, size, size, this);
        nemosInventorySorting$keyMappingButtonMap.put(keyMapping, sortButton);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V", shift = At.Shift.AFTER))
    private void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        super.render(poseStack, mouseX, mouseY, partialTick);

        var button = nemosInventorySorting$keyMappingButtonMap.values().stream()
                .filter(AbstractWidget::isHoveredOrFocused)
                .findAny();

        button.ifPresent(sortButton -> this.renderTooltip(poseStack, sortButton.getButtonName(menu), mouseX, mouseY));
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
        }
    }
}