package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.client.config.ConfigUtil;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.ContainerFilterBox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.nemonotfound.nemos.inventory.sorting.Constants.*;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin extends Screen {

    @Shadow
    protected int leftPos;
    @Shadow
    protected int topPos;

    @Shadow
    public abstract AbstractContainerMenu getMenu();

    @Unique
    private ContainerFilterBox nemosInventorySorting$containerFilterBox;
    @Unique
    private EditBox nemosInventorySorting$searchBox;
    @Unique
    private static final ResourceLocation HIGHLIGHTED_SLOT = ResourceLocation.fromNamespaceAndPath(MOD_ID, "container/highlighted_slot");
    @Unique
    private static final ResourceLocation DIMMED_SLOT = ResourceLocation.fromNamespaceAndPath(MOD_ID, "container/dimmed_slot");

    protected AbstractContainerScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    public void init(CallbackInfo ci) {
        if (nemosInventorySorting$shouldHaveSearchBox()) {
            var configs = ConfigUtil.readConfigs();
            var optionalComponentConfig = ConfigUtil.getConfigs(configs, ITEM_FILTER);

            if (optionalComponentConfig.isEmpty()) {
                nemosInventorySorting$createSearchBox(X_OFFSET_ITEM_FILTER, Y_OFFSET_ITEM_FILTER, ITEM_FILTER_WIDTH, ITEM_FILTER_HEIGHT);
                return;
            }

            var config = optionalComponentConfig.get();

            if (!config.isEnabled()) {
                return;
            }

            var yOffset = config.yOffset() != null ? config.yOffset() : Y_OFFSET_ITEM_FILTER;
            nemosInventorySorting$createSearchBox(config.xOffset(), yOffset, config.width(), config.height());
        }
    }

    @Unique
    private void nemosInventorySorting$createSearchBox(int xOffset, int yOffset, int width, int height) {
        nemosInventorySorting$containerFilterBox = new ContainerFilterBox(this.font, leftPos, topPos, xOffset, yOffset, width, height);
        nemosInventorySorting$searchBox = nemosInventorySorting$containerFilterBox.getSearchBox();
        this.addWidget(nemosInventorySorting$searchBox);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (nemosInventorySorting$shouldHaveSearchBox() && this.nemosInventorySorting$searchBox != null) {
            if (this.nemosInventorySorting$searchBox.isFocused() && keyCode != 256) {
                cir.setReturnValue(this.nemosInventorySorting$searchBox.keyPressed(keyCode, scanCode, modifiers));
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        Optional<GuiEventListener> optional = this.getChildAt(mouseX, mouseY);

        if (optional.isEmpty()) {
            for (GuiEventListener guiEventListener : this.children()) {
                guiEventListener.setFocused(false);
            }
        }
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!nemosInventorySorting$shouldHaveSearchBox() || this.nemosInventorySorting$searchBox == null) {
            return;
        }

        this.nemosInventorySorting$searchBox.render(guiGraphics, mouseX, mouseY, partialTick);
        var filter = this.nemosInventorySorting$searchBox.getValue();

        if (!filter.isEmpty()) {
            var filteredSlotMap = this.nemosInventorySorting$containerFilterBox.filterSlots(getMenu().slots, filter);

            nemosInventorySorting$markSlots(RenderType::guiTextured, filteredSlotMap.get(true), guiGraphics, HIGHLIGHTED_SLOT);
            nemosInventorySorting$markSlots(RenderType::guiTexturedOverlay, filteredSlotMap.get(false), guiGraphics, DIMMED_SLOT);
        }
    }

    @Unique
    private boolean nemosInventorySorting$shouldHaveSearchBox() {
        return getMenu() instanceof ChestMenu || getMenu() instanceof ShulkerBoxMenu;
    }

    @Unique
    private void nemosInventorySorting$markSlots(
            Function<ResourceLocation, RenderType> renderTypeFunction,
            List<Slot> slots,
            GuiGraphics guiGraphics,
            ResourceLocation texture
    ) {
        for (Slot slot : slots) {
            guiGraphics.blitSprite(renderTypeFunction, texture, leftPos + slot.x, topPos + slot.y, 16, 16);
        }
    }
}
