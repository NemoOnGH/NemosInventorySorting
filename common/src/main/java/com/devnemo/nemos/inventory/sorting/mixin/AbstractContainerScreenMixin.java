package com.devnemo.nemos.inventory.sorting.mixin;

import com.devnemo.nemos.inventory.sorting.config.model.ComponentConfig;
import com.devnemo.nemos.inventory.sorting.config.model.FilterConfig;
import com.devnemo.nemos.inventory.sorting.config.service.ConfigService;
import com.devnemo.nemos.inventory.sorting.factory.*;
import com.devnemo.nemos.inventory.sorting.gui.components.FilterBox;
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
    private static final ResourceLocation HIGHLIGHTED_SLOT = ResourceLocation.fromNamespaceAndPath(MOD_ID, "container/highlighted_slot");
    @Unique
    private static final ResourceLocation HIGHLIGHTED_SLOT_INCLUDED_ITEM = ResourceLocation.fromNamespaceAndPath(MOD_ID, "container/highlighted_slot_included_item");
    @Unique
    private static final ResourceLocation DIMMED_SLOT = ResourceLocation.fromNamespaceAndPath(MOD_ID, "container/dimmed_slot");
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
            nemosInventorySorting$inventoryEndIndex --;
        }

        nemosInventorySorting$containerSize = nemosInventorySorting$inventoryEndIndex - 27;

        if (nemosInventorySorting$shouldHaveFilter()) {
            var configs = nemosInventorySorting$configService.readOrGetDefaultComponentConfigs();
            nemosInventorySorting$filterConfig = nemosInventorySorting$configService.readOrGetDefaultFilterConfig();

            nemosInventorySorting$initFilterBox(configs);
            nemosInventorySorting$initFilterButtons(configs);
        }

        if (nemosInventorySorting$shouldHaveStorageContainerButtons()) {
            nemosInventorySorting$initStorageContainerButtons();
        }

        if (nemosInventorySorting$shouldHaveInventoryButtons()) {
            nemosInventorySorting$initInventoryButtons();
        }

        if (nemosInventorySorting$shouldHaveContainerInventorySortingButtons()) {
            nemosInventorySorting$initContainerInventoryButtons();
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
    private void nemosInventorySorting$initFilterBox(List<ComponentConfig> configs) {
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
    }

    @Unique
    private void nemosInventorySorting$initFilterButtons(List<ComponentConfig> configs) {
        var toggleFilterPersistenceButtonFactory = ToggleFilterPersistenceButtonFactory.getInstance();

        nemosInventorySorting$createButton(configs, FILTER_PERSISTENCE_TOGGLE, toggleFilterPersistenceButtonFactory);
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

        for (AbstractWidget widget : nemosInventorySorting$widgets) {
            if (widget.keyPressed(keyCode, scanCode, modifiers)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (AbstractWidget widget : nemosInventorySorting$widgets) {
            if (widget.keyReleased(keyCode, scanCode, modifiers)) {
                return true;
            }
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

        for (AbstractWidget widget : nemosInventorySorting$widgets) {
            if (widget.mouseClicked(mouseX, mouseY, button)) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlotHighlightFront(Lnet/minecraft/client/gui/GuiGraphics;)V", shift = At.Shift.AFTER))
    void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!nemosInventorySorting$shouldHaveFilter() || this.nemosInventorySorting$filterBox == null) {
            return;
        }

        var filter = this.nemosInventorySorting$filterBox.getValue();

        if (!filter.isEmpty()) {
            var filteredSlotMap = this.nemosInventorySorting$filterBox.filterSlots(getMenu().slots, filter);

            nemosInventorySorting$markSlots(RenderType::guiTextured, filteredSlotMap.get(FilterResult.INCLUDED), guiGraphics, HIGHLIGHTED_SLOT);
            nemosInventorySorting$markSlots(RenderType::guiTextured, filteredSlotMap.get(FilterResult.HAS_INCLUDED_ITEM), guiGraphics, HIGHLIGHTED_SLOT_INCLUDED_ITEM);
            nemosInventorySorting$markSlots(RenderType::guiTexturedOverlay, filteredSlotMap.get(FilterResult.EXCLUDED), guiGraphics, DIMMED_SLOT);
        }
    }

    @Unique
    private boolean nemosInventorySorting$shouldHaveFilter() {
        return !(getMenu() instanceof CreativeModeInventoryScreen.ItemPickerMenu);
    }

    @Unique
    private boolean nemosInventorySorting$shouldHaveStorageContainerButtons() {
        return getMenu() instanceof ChestMenu || getMenu() instanceof ShulkerBoxMenu;
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
                menu instanceof CrafterMenu ||
                menu instanceof GrindstoneMenu ||
                menu instanceof BrewingStandMenu;
    }

    @Unique
    private void nemosInventorySorting$markSlots(
            Function<ResourceLocation, RenderType> renderTypeFunction,
            List<Slot> slots,
            GuiGraphics guiGraphics,
            ResourceLocation texture
    ) {
        if (slots == null) {
            return;
        }

        for (Slot slot : slots) {
            guiGraphics.blitSprite(renderTypeFunction, texture, slot.x, slot.y, 16, 16);
        }
    }

    @Unique
    private void nemosInventorySorting$initStorageContainerButtons() {
        var sortButtonFactory = SortButtonFactory.getInstance();
        var dropAllButtonFactory = DropAllButtonFactory.getInstance();
        var moveSameButtonFactory = MoveSameButtonFactory.getInstance();
        var moveAllButtonFactory = MoveAllButtonFactory.getInstance();
        var configs = nemosInventorySorting$configService.readOrGetDefaultComponentConfigs();
        var yOffset = getMenu() instanceof ShulkerBoxMenu ? inventoryLabelY - 1 : inventoryLabelY - 2;

        nemosInventorySorting$createButtonForContainer(configs, SORT_STORAGE_CONTAINER, sortButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createButtonForContainer(configs, MOVE_SAME_STORAGE_CONTAINER, moveSameButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createButtonForContainer(configs, MOVE_ALL_STORAGE_CONTAINER, moveAllButtonFactory, Y_OFFSET_CONTAINER);
        nemosInventorySorting$createButtonForContainer(configs, DROP_ALL_STORAGE_CONTAINER, dropAllButtonFactory, Y_OFFSET_CONTAINER);

        nemosInventorySorting$createButtonForInventory(configs, SORT_STORAGE_CONTAINER_INVENTORY, sortButtonFactory, yOffset);
        nemosInventorySorting$createButtonForInventory(configs, MOVE_SAME_STORAGE_CONTAINER_INVENTORY, moveSameButtonFactory, yOffset);
        nemosInventorySorting$createButtonForInventory(configs, MOVE_ALL_STORAGE_CONTAINER_INVENTORY, moveAllButtonFactory, yOffset);
        nemosInventorySorting$createButtonForInventory(configs, DROP_ALL_STORAGE_CONTAINER_INVENTORY, dropAllButtonFactory, yOffset);
    }

    @Unique
    private void nemosInventorySorting$initInventoryButtons() {
        var sortButtonFactory = SortButtonFactory.getInstance();
        var dropAllButtonFactory = DropAllButtonFactory.getInstance();

        var configs = nemosInventorySorting$configService.readOrGetDefaultComponentConfigs();

        nemosInventorySorting$createButtonForInventory(configs, SORT_INVENTORY, sortButtonFactory, Y_OFFSET_INVENTORY);
        nemosInventorySorting$createButtonForInventory(configs, DROP_ALL_INVENTORY, dropAllButtonFactory, Y_OFFSET_INVENTORY);
    }

    @Unique
    private void nemosInventorySorting$initContainerInventoryButtons() {
        var sortButtonFactory = SortButtonFactory.getInstance();
        var dropAllButtonFactory = DropAllButtonFactory.getInstance();
        var yOffset = inventoryLabelY - 1;

        var configs = nemosInventorySorting$configService.readOrGetDefaultComponentConfigs();

        nemosInventorySorting$createButtonForInventory(configs, SORT_CONTAINER_INVENTORY, sortButtonFactory, yOffset);
        nemosInventorySorting$createButtonForInventory(configs, DROP_ALL_CONTAINER_INVENTORY, dropAllButtonFactory, yOffset);
    }

    @Unique
    private void nemosInventorySorting$createButtonForContainer(List<ComponentConfig> configs, String componentName, ButtonCreator buttonCreator, int defaultYOffset) {
        var optionalComponentConfig = nemosInventorySorting$configService.getOrDefaultComponentConfig(configs, componentName);

        if (optionalComponentConfig.isEmpty()) {
            return;
        }

        var config = optionalComponentConfig.get();

        if (!config.isEnabled()) {
            return;
        }

        var yOffset = config.yOffset() != null ? config.yOffset() : defaultYOffset;
        nemosInventorySorting$createContainerButton(buttonCreator, config.xOffset(), yOffset, config.width(), config.height());
    }

    @Unique
    private void nemosInventorySorting$createButtonForInventory(List<ComponentConfig> configs, String componentName, ButtonCreator buttonCreator, int defaultYOffset) {
        var optionalComponentConfig = nemosInventorySorting$configService.getOrDefaultComponentConfig(configs, componentName);

        if (optionalComponentConfig.isEmpty()) {
            return;
        }

        var config = optionalComponentConfig.get();

        if (!config.isEnabled()) {
            return;
        }

        var yOffset = config.yOffset() != null ? config.yOffset() : defaultYOffset;
        nemosInventorySorting$createInventoryButton(buttonCreator, config.xOffset(), yOffset, config.width(), config.height());
    }

    @Unique
    private void nemosInventorySorting$createContainerButton(ButtonCreator buttonCreator, int xOffset, int yOffset, int width, int height) {
        nemosInventorySorting$createButton(buttonCreator, 0, nemosInventorySorting$containerSize, xOffset, yOffset, width, height, false);
    }

    @Unique
    private void nemosInventorySorting$createInventoryButton(ButtonCreator buttonCreator, int xOffset, int yOffset, int width, int height) {
        nemosInventorySorting$createButton(buttonCreator, nemosInventorySorting$containerSize, nemosInventorySorting$inventoryEndIndex, xOffset, yOffset, width, height, true);
    }

    @Unique
    private void nemosInventorySorting$createButton(ButtonCreator buttonCreator, int startIndex, int endIndex, int xOffset, int yOffset, int width, int height, boolean isInventoryButton) {
        var sortButton = buttonCreator.createButton(startIndex, endIndex, leftPos, topPos, xOffset, yOffset, width, height, getMenu(), isInventoryButton);
        nemosInventorySorting$widgets.add(sortButton);
    }
}
