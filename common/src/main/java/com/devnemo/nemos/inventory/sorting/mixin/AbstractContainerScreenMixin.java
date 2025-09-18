package com.devnemo.nemos.inventory.sorting.mixin;

import com.devnemo.nemos.inventory.sorting.config.model.ComponentConfig;
import com.devnemo.nemos.inventory.sorting.config.model.FilterConfig;
import com.devnemo.nemos.inventory.sorting.config.service.ConfigService;
import com.devnemo.nemos.inventory.sorting.factory.*;
import com.devnemo.nemos.inventory.sorting.gui.components.FilterBox;
import com.devnemo.nemos.inventory.sorting.helper.ButtonTypeMapping;
import com.devnemo.nemos.inventory.sorting.model.FilterResult;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.devnemo.nemos.inventory.sorting.Constants.MOD_ID;
import static com.devnemo.nemos.inventory.sorting.config.DefaultConfigValues.*;

//TODO: Refactor
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin extends Screen {

    @Shadow
    protected int leftPos;
    @Shadow
    protected int topPos;

    @Shadow
    public abstract AbstractContainerMenu getMenu();

    @Shadow
    protected int inventoryLabelY;
    @Shadow protected int imageWidth;
    @Unique
    private FilterBox nemosInventorySorting$filterBox;
    @Unique
    private FilterConfig nemosInventorySorting$filterConfig;
    @Unique
    private static final ResourceLocation HIGHLIGHTED_SLOT = new ResourceLocation(MOD_ID, "textures/gui/sprites/container/highlighted_slot.png");
    @Unique
    private static final ResourceLocation HIGHLIGHTED_SLOT_INCLUDED_ITEM = new ResourceLocation(MOD_ID, "textures/gui/sprites/container/highlighted_slot_included_item.png");
    @Unique
    private static final ResourceLocation DIMMED_SLOT = new ResourceLocation(MOD_ID, "textures/gui/sprites/container/dimmed_slot.png");
    @Unique
    private int nemosInventorySorting$inventoryEndIndex;
    @Unique
    private int nemosInventorySorting$containerSize;
    @Unique
    private int nemosInventorySorting$filterBoxWidth = 0;

    @Unique
    private final ConfigService nemosInventorySorting$configService = ConfigService.getInstance();
    @Unique
    private final List<AbstractWidget> nemosInventorySorting$widgets = new ArrayList<>();

    protected AbstractContainerScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    public void init(CallbackInfo ci) {
        nemosInventorySorting$inventoryEndIndex = getMenu().slots.size() - 9;

        if (getMenu() instanceof InventoryMenu) {
            nemosInventorySorting$inventoryEndIndex--;
        }

        nemosInventorySorting$containerSize = nemosInventorySorting$inventoryEndIndex - 27;
        var componentConfigs = nemosInventorySorting$configService.readOrGetDefaultComponentConfigs();

        if (nemosInventorySorting$shouldHaveFilter()) {
            nemosInventorySorting$filterConfig = nemosInventorySorting$configService.readOrGetDefaultFilterConfig();

            nemosInventorySorting$initFilter(componentConfigs);
        }

        if (nemosInventorySorting$shouldHaveStorageContainerButtons()) {
            nemosInventorySorting$initStorageContainerButtons(componentConfigs);
        }

        if (nemosInventorySorting$shouldHaveInventoryButtons()) {
            nemosInventorySorting$initInventoryButtons(componentConfigs);
        }

        if (nemosInventorySorting$shouldHaveContainerInventorySortingButtons()) {
            nemosInventorySorting$initContainerInventoryButtons(componentConfigs);
        }

        for (AbstractWidget widget : nemosInventorySorting$widgets) {
            this.addRenderableWidget(widget);
        }
    }

    @Override
    protected void clearWidgets() {
        nemosInventorySorting$widgets.clear();
        super.clearWidgets();
    }

    @Unique
    private void nemosInventorySorting$initFilter(List<ComponentConfig> configs) {
        var optionalComponentConfig = nemosInventorySorting$configService.getOrDefaultComponentConfig(configs, ITEM_FILTER);

        if (optionalComponentConfig.isEmpty()) {
            return;
        }

        var config = optionalComponentConfig.get();

        if (!config.isEnabled()) {
            return;
        }

        nemosInventorySorting$filterBoxWidth = config.width();
        var xOffset = config.xOffset() != null ? config.xOffset() : imageWidth - nemosInventorySorting$filterBoxWidth - 3;
        var yOffset = config.yOffset() != null ? config.yOffset() : Y_OFFSET_ITEM_FILTER;

        nemosInventorySorting$createSearchBox(xOffset, yOffset, nemosInventorySorting$filterBoxWidth, config.height(), nemosInventorySorting$filterConfig.getFilter());
        nemosInventorySorting$createButton(configs, FILTER_PERSISTENCE_TOGGLE, ToggleFilterPersistenceButtonFactory.getInstance());
    }

    @Inject(method = "onClose", at = @At("TAIL"))
    private void onClose(CallbackInfo ci) {
        if (nemosInventorySorting$filterBox == null) {
            return;
        }

        nemosInventorySorting$filterBox.updateAndSaveFilter(nemosInventorySorting$filterConfig);
    }

    @Unique
    private void nemosInventorySorting$createSearchBox(int xOffset, int yOffset, int width, int height, String filter) {
        nemosInventorySorting$filterBox = new FilterBox(
                font,
                leftPos,
                topPos,
                xOffset,
                yOffset,
                width,
                height,
                Component.translatable("nemos_inventory_sorting.itemFilter")
        );

        this.addRenderableWidget(nemosInventorySorting$filterBox);
        nemosInventorySorting$filterBox.setValue(filter);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.nemosInventorySorting$filterBox != null) {
            if (this.nemosInventorySorting$filterBox.isFocused() && keyCode != 256) {
                cir.setReturnValue(this.nemosInventorySorting$filterBox.keyPressed(keyCode, scanCode, modifiers));
                return;
            }
        }

        if (nemosInventorySorting$triggerActionOnWidget(widget -> widget.keyPressed(keyCode, scanCode, modifiers))) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (nemosInventorySorting$triggerActionOnWidget(widget -> widget.keyReleased(keyCode, scanCode, modifiers))) {
            return true;
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        Optional<GuiEventListener> optional = this.getChildAt(mouseX, mouseY);

        if (optional.isEmpty()) {
            for (GuiEventListener guiEventListener : this.children()) {
                guiEventListener.setFocused(false);
            }
        }

        if (nemosInventorySorting$triggerActionOnWidget(widget -> widget.mouseClicked(mouseX, mouseY, button))) {
            cir.setReturnValue(true);
        }
    }

    @Unique
    private boolean nemosInventorySorting$triggerActionOnWidget(Function<AbstractWidget, Boolean> function) {
        for (var widget : nemosInventorySorting$widgets) {
            if (function.apply(widget)) {
                return true;
            }
        }

        return false;
    }

    @Inject(method = "render", at = @At("TAIL"))
    void renderHighlightedSlot(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!nemosInventorySorting$shouldHaveFilter() || this.nemosInventorySorting$filterBox == null) {
            return;
        }

        var filter = this.nemosInventorySorting$filterBox.getValue();

        if (!filter.isEmpty()) {
            var filteredSlotMap = this.nemosInventorySorting$filterBox.filterSlots(getMenu().slots, filter);

            nemosInventorySorting$markSlots(filteredSlotMap.get(FilterResult.INCLUDED), guiGraphics, HIGHLIGHTED_SLOT);
            nemosInventorySorting$markSlots(filteredSlotMap.get(FilterResult.HAS_INCLUDED_ITEM), guiGraphics, HIGHLIGHTED_SLOT_INCLUDED_ITEM);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderLabels(Lnet/minecraft/client/gui/GuiGraphics;II)V", shift = At.Shift.AFTER))
    void renderDimmedSlot(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!nemosInventorySorting$shouldHaveFilter() || this.nemosInventorySorting$filterBox == null) {
            return;
        }

        var filter = this.nemosInventorySorting$filterBox.getValue();

        if (!filter.isEmpty()) {
            var filteredSlotMap = this.nemosInventorySorting$filterBox.filterSlots(getMenu().slots, filter);

            nemosInventorySorting$markSlots(filteredSlotMap.get(FilterResult.EXCLUDED), guiGraphics, DIMMED_SLOT);
        }
    }

    @Unique
    private boolean nemosInventorySorting$shouldHaveFilter() {
        return !(getMenu() instanceof CreativeModeInventoryScreen.ItemPickerMenu);
    }

    @Unique
    private boolean nemosInventorySorting$shouldHaveStorageContainerButtons() {
        var menu = getMenu();

        return menu instanceof ChestMenu || menu instanceof ShulkerBoxMenu;
    }

    @Unique
    private boolean nemosInventorySorting$shouldHaveInventoryButtons() {
        return getMenu() instanceof InventoryMenu;
    }

    @Unique
    private boolean nemosInventorySorting$shouldHaveContainerInventorySortingButtons() {
        var menu = getMenu();

        return menu instanceof EnchantmentMenu ||
                menu instanceof FurnaceMenu ||
                menu instanceof SmokerMenu ||
                menu instanceof BlastFurnaceMenu ||
                menu instanceof CraftingMenu ||
                menu instanceof GrindstoneMenu ||
                menu instanceof BrewingStandMenu;
    }

    @Unique
    private void nemosInventorySorting$markSlots(
            List<Slot> slots,
            GuiGraphics guiGraphics,
            ResourceLocation texture
    ) {
        if (slots == null) {
            return;
        }

        for (Slot slot : slots) {
            var xPos = leftPos + slot.x;
            var yPos = topPos + slot.y;

            if (texture == DIMMED_SLOT) {
                guiGraphics.blit(texture, slot.x, slot.y, 0, 0, 16, 16);
                guiGraphics.fillGradient(RenderType.guiOverlay(), slot.x, slot.y, slot.x + 16, slot.y + 16, -2139062142, -2139062142, 0);

                continue;
            }

            guiGraphics.blit(texture, xPos, yPos, 0, 0, 16, 16);
        }
    }

    @Unique
    private void nemosInventorySorting$initStorageContainerButtons(List<ComponentConfig> componentConfigs) {
        var defaultInventoryYOffset = getMenu() instanceof ShulkerBoxMenu ? inventoryLabelY - 1 : inventoryLabelY - 2;

        nemosInventorySorting$createButtons(
                componentConfigs,
                new ButtonTypeMapping(SORT_STORAGE_CONTAINER, SortButtonFactory.getInstance(), Y_OFFSET_CONTAINER, false),
                new ButtonTypeMapping(MOVE_SAME_STORAGE_CONTAINER, MoveSameButtonFactory.getInstance(), Y_OFFSET_CONTAINER, false),
                new ButtonTypeMapping(MOVE_ALL_STORAGE_CONTAINER, MoveAllButtonFactory.getInstance(), Y_OFFSET_CONTAINER, false),
                new ButtonTypeMapping(DROP_ALL_STORAGE_CONTAINER, DropAllButtonFactory.getInstance(), Y_OFFSET_CONTAINER, false),
                new ButtonTypeMapping(SORT_STORAGE_CONTAINER_INVENTORY, SortButtonFactory.getInstance(), defaultInventoryYOffset, true),
                new ButtonTypeMapping(MOVE_SAME_STORAGE_CONTAINER_INVENTORY, MoveSameButtonFactory.getInstance(), defaultInventoryYOffset, true),
                new ButtonTypeMapping(MOVE_ALL_STORAGE_CONTAINER_INVENTORY, MoveAllButtonFactory.getInstance(), defaultInventoryYOffset, true),
                new ButtonTypeMapping(DROP_ALL_STORAGE_CONTAINER_INVENTORY, DropAllButtonFactory.getInstance(), defaultInventoryYOffset, true)
        );
    }


    @Unique
    private void nemosInventorySorting$initInventoryButtons(List<ComponentConfig> componentConfigs) {
        nemosInventorySorting$createButtons(
                componentConfigs,
                new ButtonTypeMapping(SORT_INVENTORY, SortButtonFactory.getInstance(), Y_OFFSET_INVENTORY, true),
                new ButtonTypeMapping(DROP_ALL_INVENTORY, DropAllButtonFactory.getInstance(), Y_OFFSET_INVENTORY, true)
        );
    }

    @Unique
    private void nemosInventorySorting$initContainerInventoryButtons(List<ComponentConfig> componentConfigs) {
        var defaultInventoryYOffset = inventoryLabelY - 1;

        nemosInventorySorting$createButtons(
                componentConfigs,
                new ButtonTypeMapping(SORT_CONTAINER_INVENTORY, SortButtonFactory.getInstance(), defaultInventoryYOffset, true),
                new ButtonTypeMapping(DROP_ALL_CONTAINER_INVENTORY, DropAllButtonFactory.getInstance(), defaultInventoryYOffset, true)
        );
    }

    @Unique
    private void nemosInventorySorting$createButton(List<ComponentConfig> configs, String componentName, FilterButtonCreator filterButtonCreator) {
        var optionalComponentConfig = nemosInventorySorting$configService.getOrDefaultComponentConfig(configs, componentName);

        if (optionalComponentConfig.isEmpty()) {
            return;
        }

        var config = optionalComponentConfig.get();

        if (!config.isEnabled()) {
            return;
        }

        var width = config.width();
        var xOffset = config.xOffset() != null ? config.xOffset() : imageWidth - nemosInventorySorting$filterBoxWidth - width - 5;
        var yOffset = config.yOffset() != null ? config.yOffset() : Y_OFFSET_ITEM_FILTER;
        var button = filterButtonCreator.createButton(leftPos, topPos, xOffset, yOffset, width, config.height(), nemosInventorySorting$filterConfig);

        nemosInventorySorting$widgets.add(button);
    }

    @Unique
    private void nemosInventorySorting$createButtons(List<ComponentConfig> configs,
                                                     ButtonTypeMapping... mappings) {
        for (ButtonTypeMapping mapping : mappings) {
            var optionalConfig = nemosInventorySorting$configService.getOrDefaultComponentConfig(configs, mapping.componentName());

            if (optionalConfig.isEmpty()) {
                continue;
            }

            var config = optionalConfig.get();

            if (!config.isEnabled()) {
                continue;
            }

            int yOffset = config.yOffset() != null ? config.yOffset() : mapping.defaultYOffset();

            nemosInventorySorting$createButton(mapping.factory(), mapping.isInventoryButton(), config.xOffset(), yOffset, config.width(), config.height());
        }
    }

    @Unique
    private void nemosInventorySorting$createButton(ButtonCreator buttonCreator, boolean isInventoryButton, int xOffset, int yOffset, int width, int height) {
        var startIndex = isInventoryButton ? nemosInventorySorting$containerSize : 0;
        var endIndex = isInventoryButton ? nemosInventorySorting$inventoryEndIndex : nemosInventorySorting$containerSize;

        nemosInventorySorting$createButton(buttonCreator, startIndex, endIndex, xOffset, yOffset, width, height, isInventoryButton);
    }

    @Unique
    private void nemosInventorySorting$createButton(ButtonCreator buttonCreator, int startIndex, int endIndex, int xOffset, int yOffset, int width, int height, boolean isInventoryButton) {
        var sortButton = buttonCreator.createButton(startIndex, endIndex, leftPos, topPos, xOffset, yOffset, width, height, getMenu(), isInventoryButton);
        nemosInventorySorting$widgets.add(sortButton);
    }
}