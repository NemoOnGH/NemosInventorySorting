package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.ModKeyMappings;
import com.nemonotfound.nemos.inventory.sorting.config.model.ComponentConfig;
import com.nemonotfound.nemos.inventory.sorting.config.model.FilterConfig;
import com.nemonotfound.nemos.inventory.sorting.config.service.ConfigService;
import com.nemonotfound.nemos.inventory.sorting.factory.FilterButtonCreator;
import com.nemonotfound.nemos.inventory.sorting.factory.ToggleFilterPersistenceButtonFactory;
import com.nemonotfound.nemos.inventory.sorting.gui.components.FilterBox;
import com.nemonotfound.nemos.inventory.sorting.gui.components.buttons.AbstractFilterToggleButton;
import com.nemonotfound.nemos.inventory.sorting.model.FilterResult;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
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
import java.util.Optional;
import java.util.function.Function;

import static com.nemonotfound.nemos.inventory.sorting.Constants.*;
import static com.nemonotfound.nemos.inventory.sorting.config.DefaultConfigValues.*;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin extends Screen {

    @Shadow
    protected int leftPos;
    @Shadow
    protected int topPos;

    @Shadow
    public abstract AbstractContainerMenu getMenu();

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
    private final ConfigService nemosInventorySorting$configService = ConfigService.getInstance();
    @Unique
    private final Map<KeyMapping, AbstractFilterToggleButton> nemosInventorySorting$keyMappingButtonMap = new HashMap<>();

    protected AbstractContainerScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    public void init(CallbackInfo ci) {
        if (nemosInventorySorting$shouldHaveFilterComponents()) {
            var configs = nemosInventorySorting$configService.readOrGetDefaultComponentConfigs();
            nemosInventorySorting$filterConfig = nemosInventorySorting$configService.readOrGetDefaultFilterConfig();

            nemosInventorySorting$initFilterBox(configs);
            nemosInventorySorting$initFilterButtons(configs);
        }
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

        var yOffset = config.yOffset() != null ? config.yOffset() : Y_OFFSET_ITEM_FILTER;
        nemosInventorySorting$createSearchBox(config.xOffset(), yOffset, config.width(), config.height(), nemosInventorySorting$filterConfig.getFilter());
    }

    @Unique
    private void nemosInventorySorting$initFilterButtons(List<ComponentConfig> configs) {
        var toggleFilterPersistenceButtonFactory = ToggleFilterPersistenceButtonFactory.getInstance();

        nemosInventorySorting$createButton(configs, FILTER_PERSISTENCE_TOGGLE, ModKeyMappings.TOGGLE_FILTER_PERSISTENCE.get(), toggleFilterPersistenceButtonFactory);
    }

    @Unique
    private void nemosInventorySorting$createButton(List<ComponentConfig> configs, String componentName, KeyMapping keyMapping, FilterButtonCreator filterButtonCreator) {
        var optionalComponentConfig = nemosInventorySorting$configService.getOrDefaultComponentConfig(configs, componentName);

        if (optionalComponentConfig.isEmpty()) {
            return;
        }

        var config = optionalComponentConfig.get();

        if (!config.isEnabled()) {
            return;
        }

        var yOffset = config.yOffset() != null ? config.yOffset() : Y_OFFSET_ITEM_FILTER;
        var button = filterButtonCreator.createButton(leftPos, topPos, config.xOffset(), yOffset, config.width(), config.height(), nemosInventorySorting$filterConfig);

        nemosInventorySorting$keyMappingButtonMap.put(keyMapping, button);
        this.addRenderableWidget(button);
    }

    @Inject(method = "onClose", at = @At("TAIL"))
    private void onClose(CallbackInfo ci) {
        if (nemosInventorySorting$filterBox == null) {
            return;
        }

        var filter = nemosInventorySorting$filterConfig.isFilterPersistent() ? nemosInventorySorting$filterBox.getValue() : "";

        nemosInventorySorting$filterConfig.setFilter(filter);
        nemosInventorySorting$configService.writeConfig(true, FILTER_CONFIG_PATH, nemosInventorySorting$filterConfig);
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

        this.addWidget(nemosInventorySorting$filterBox);
        nemosInventorySorting$filterBox.setValue(filter);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!nemosInventorySorting$shouldHaveFilterComponents()) {
            return;
        }

        if (this.nemosInventorySorting$filterBox != null) {
            if (this.nemosInventorySorting$filterBox.isFocused() && keyCode != 256) {
                cir.setReturnValue(this.nemosInventorySorting$filterBox.keyPressed(keyCode, scanCode, modifiers));
                return;
            }
        }

        var optionalButtonEntry = nemosInventorySorting$keyMappingButtonMap.entrySet().stream()
                .filter(entry -> entry.getKey().matches(keyCode, scanCode))
                .findFirst();

        optionalButtonEntry.ifPresent(entry -> {
            var button = entry.getValue();

            button.playDownSound(Minecraft.getInstance().getSoundManager());
            button.onClick(0, 0);
        });
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        Optional<GuiEventListener> optional = this.getChildAt(mouseX, mouseY);

        if (optional.isEmpty()) {
            for (GuiEventListener guiEventListener : this.children()) {
                guiEventListener.setFocused(false);
            }
        }

        var optionalButtonEntry = nemosInventorySorting$keyMappingButtonMap.entrySet().stream()
                .filter(entry -> entry.getKey().matchesMouse(button))
                .findFirst();

        optionalButtonEntry.ifPresent(entry -> {
            var sortButton = entry.getValue();

            sortButton.playDownSound(Minecraft.getInstance().getSoundManager());
            sortButton.onClick(0, 0);
        });
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderFilterBar(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!nemosInventorySorting$shouldHaveFilterComponents() || this.nemosInventorySorting$filterBox == null) {
            return;
        }

        this.nemosInventorySorting$filterBox.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlotHighlightFront(Lnet/minecraft/client/gui/GuiGraphics;)V", shift = At.Shift.AFTER))
    void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!nemosInventorySorting$shouldHaveFilterComponents() || this.nemosInventorySorting$filterBox == null) {
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
    private boolean nemosInventorySorting$shouldHaveFilterComponents() {
        return !(getMenu() instanceof CreativeModeInventoryScreen.ItemPickerMenu);
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

    //TODO: Add buttons here and check containerSize with getMenu
}
