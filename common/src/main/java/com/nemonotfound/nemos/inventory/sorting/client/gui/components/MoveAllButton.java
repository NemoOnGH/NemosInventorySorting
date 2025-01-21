package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ClickType;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

public class MoveAllButton extends AbstractSingleClickButton<MoveAllButton> {

    private final ResourceLocation buttonTexture = new ResourceLocation(MOD_ID, "textures/gui/sprites/move_all_button.png");
    private final ResourceLocation buttonHoverTexture = new ResourceLocation(MOD_ID, "textures/gui/sprites/move_all_button_highlighted.png");

    public MoveAllButton(Builder<MoveAllButton> builder) {
        super(builder);
    }

    @Override
    protected ResourceLocation getButtonHoverTexture() {
        return buttonHoverTexture;
    }

    @Override
    protected ResourceLocation getButtonTexture() {
        return buttonTexture;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        interactWithAllItems(ClickType.QUICK_MOVE, 0);
    }
}
