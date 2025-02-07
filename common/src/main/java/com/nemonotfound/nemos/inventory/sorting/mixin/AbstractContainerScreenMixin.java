package com.nemonotfound.nemos.inventory.sorting.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nemonotfound.nemos.inventory.sorting.client.gui.components.ContainerFilterBox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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
    private static final ResourceLocation HIGHLIGHTED_SLOT = new ResourceLocation(MOD_ID, "textures/gui/sprites/container/highlighted_slot.png");
    @Unique
    private static final ResourceLocation DIMMED_SLOT = new ResourceLocation(MOD_ID, "textures/gui/sprites/container/dimmed_slot.png");

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

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V", shift = At.Shift.AFTER))
    void renderBackground(PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        super.render(poseStack, mouseX, mouseY, partialTick);

        if (nemosInventorySorting$shouldHaveSearchBox()) {
            this.nemosInventorySorting$searchBox.render(poseStack, mouseX, mouseY, partialTick);
            var filter = this.nemosInventorySorting$searchBox.getValue();

            if (!filter.isEmpty()) {
                var filteredSlotMap = this.nemosInventorySorting$containerFilterBox.filterSlots(getMenu().slots, filter);
                var leftPosOffset = leftPos + 8;
                var topPosOffset = topPos;

                nemosInventorySorting$markSlots(filteredSlotMap.get(true), leftPosOffset, topPosOffset, poseStack, HIGHLIGHTED_SLOT);
                nemosInventorySorting$markSlots(filteredSlotMap.get(false), leftPosOffset, topPosOffset, poseStack, DIMMED_SLOT);
            }
        }
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    void renderHighlight(PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        super.render(poseStack, mouseX, mouseY, partialTick);

        if (nemosInventorySorting$shouldHaveSearchBox()) {
            this.nemosInventorySorting$searchBox.render(poseStack, mouseX, mouseY, partialTick);
            var filter = this.nemosInventorySorting$searchBox.getValue();

            if (!filter.isEmpty()) {
                var filteredSlotMap = this.nemosInventorySorting$containerFilterBox.filterSlots(getMenu().slots, filter);
                var leftPosOffset = leftPos + 8;
                var topPosOffset = topPos;

                for (int slotIndex : filteredSlotMap.get(false)) {
                    var column = slotIndex % 9;
                    var row = (int) Math.ceil((double) (slotIndex + 1) / 9);
                    var xPos = leftPosOffset + (18 * column);
                    var yPos = nemosInventorySorting$calculateYPos(nemosInventorySorting$containerRows, row, topPosOffset);

                    fillGradient(poseStack, xPos, yPos, xPos + 16, yPos + 16, -2139062142, -2139062142, 0);
                }
            }
        }
    }

    @Unique
    private boolean nemosInventorySorting$shouldHaveSearchBox() {
        return getMenu() instanceof ChestMenu || getMenu() instanceof ShulkerBoxMenu;
    }

    @Unique
    private void nemosInventorySorting$markSlots(
            List<Integer> slots,
            int leftPosOffset,
            int topPosOffset,
            PoseStack poseStack,
            ResourceLocation texture
    ) {
        for (int slotIndex : slots) {
            var column = slotIndex % 9;
            var row = (int) Math.ceil((double) (slotIndex + 1) / 9);
            var xPos = leftPosOffset + (18 * column);
            var yPos = nemosInventorySorting$calculateYPos(nemosInventorySorting$containerRows, row, topPosOffset);

            RenderSystem.setShaderTexture(0, texture);
            blit(poseStack, xPos, yPos, 0, 0, 16, 16, 0, 0);
        }
    }

    @Unique
    private int nemosInventorySorting$calculateYPos(int rowCount, int row, int topPosOffset) {
        var base = topPosOffset + (18 * row);

        return row <= rowCount ? base : row <= rowCount + 3 ? base + 13 : base + 17;
    }
}
