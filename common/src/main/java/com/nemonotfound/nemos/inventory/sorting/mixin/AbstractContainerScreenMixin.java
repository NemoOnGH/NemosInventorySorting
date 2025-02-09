package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.nemonotfound.nemos.inventory.sorting.client.gui.components.ContainerFilterBox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Function;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

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
    private int nemosInventorySorting$containerRows;
    @Unique
    private static final ResourceLocation HIGHLIGHTED_SLOT = ResourceLocation.fromNamespaceAndPath(MOD_ID, "container/highlighted_slot");
    @Unique
    private static final ResourceLocation DIMMED_SLOT = ResourceLocation.fromNamespaceAndPath(MOD_ID, "container/dimmed_slot");

    protected AbstractContainerScreenMixin(Component $$0) {
        super($$0);
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    public void init(CallbackInfo ci) {
        if (nemosInventorySorting$shouldHaveSearchBox()) {
            nemosInventorySorting$containerRows = getNemosInventorySorting$calculateContainerRows();

            nemosInventorySorting$containerFilterBox = new ContainerFilterBox(this.font, leftPos, topPos);
            nemosInventorySorting$searchBox = nemosInventorySorting$containerFilterBox.getSearchBox();
            this.addWidget(nemosInventorySorting$searchBox);
        }
    }

    @Unique
    private int getNemosInventorySorting$calculateContainerRows() {
        var allContainerRows = getMenu().slots.size() / 9;

        return allContainerRows - 4;
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (nemosInventorySorting$shouldHaveSearchBox()) {
            if (this.nemosInventorySorting$searchBox.isFocused() && keyCode != 256) {
                cir.setReturnValue(this.nemosInventorySorting$searchBox.keyPressed(keyCode, scanCode, modifiers));
            }
        }
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (nemosInventorySorting$shouldHaveSearchBox()) {
            this.nemosInventorySorting$searchBox.render(guiGraphics, mouseX, mouseY, partialTick);
            var filter = this.nemosInventorySorting$searchBox.getValue();

            if (!filter.isEmpty()) {
                var filteredSlotMap = this.nemosInventorySorting$containerFilterBox.filterSlots(getMenu().slots, filter);
                var leftPosOffset = leftPos + 8;
                var topPosOffset = topPos;

                nemosInventorySorting$markSlots(RenderType::guiTextured, filteredSlotMap.get(true), leftPosOffset, topPosOffset, guiGraphics, HIGHLIGHTED_SLOT);
                nemosInventorySorting$markSlots(RenderType::guiTexturedOverlay, filteredSlotMap.get(false), leftPosOffset, topPosOffset, guiGraphics, DIMMED_SLOT);
            }
        }
    }

//    @Inject(method = "render", at = @At(value = "TAIL"))
//    void renderHighlight(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
//        super.render(guiGraphics, mouseX, mouseY, partialTick);
//
//        if (nemosInventorySorting$shouldHaveSearchBox()) {
//            this.nemosInventorySorting$searchBox.render(guiGraphics, mouseX, mouseY, partialTick);
//            var filter = this.nemosInventorySorting$searchBox.getValue();
//
//            if (!filter.isEmpty()) {
//                var filteredSlotMap = this.nemosInventorySorting$containerFilterBox.filterSlots(getMenu().slots, filter);
//                var leftPosOffset = leftPos + 8;
//                var topPosOffset = topPos;
//
//                for (int slotIndex : filteredSlotMap.get(false)) {
//                    var column = slotIndex % 9;
//                    var row = (int) Math.ceil((double) (slotIndex + 1) / 9);
//                    var xPos = leftPosOffset + (18 * column);
//                    var yPos = nemosInventorySorting$calculateYPos(nemosInventorySorting$containerRows, row, topPosOffset);
//
//                    guiGraphics.blitSprite(RenderType.guiTextured(), xPos, yPos, xPos + 16, yPos + 16, -2139062142, -2139062142, 0);
//                }
//            }
//        }
//    }

    @Unique
    private boolean nemosInventorySorting$shouldHaveSearchBox() {
        return getMenu() instanceof ChestMenu || getMenu() instanceof ShulkerBoxMenu;
    }

    @Unique
    private void nemosInventorySorting$markSlots(
            Function<ResourceLocation, RenderType> renderTypeFunction,
            List<Integer> slots,
            int leftPosOffset,
            int topPosOffset,
            GuiGraphics guiGraphics,
            ResourceLocation texture
    ) {
        for (int slotIndex : slots) {
            var column = slotIndex % 9;
            var row = (int) Math.ceil((double) (slotIndex + 1) / 9);
            var xPos = leftPosOffset + (18 * column);
            var yPos = nemosInventorySorting$calculateYPos(nemosInventorySorting$containerRows, row, topPosOffset);

            guiGraphics.blitSprite(renderTypeFunction, texture, xPos, yPos, 16, 16);
        }
    }

    @Unique
    private int nemosInventorySorting$calculateYPos(int rowCount, int row, int topPosOffset) {
        var base = topPosOffset + (18 * row);

        return row <= rowCount ? base : row <= rowCount + 3 ? base + 13 : base + 17;
    }
}
