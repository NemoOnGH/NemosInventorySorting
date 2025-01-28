package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ClickType;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

public class DropAllButton extends AbstractSingleClickButton<DropAllButton> {

    private final ResourceLocation buttonTexture = new ResourceLocation(MOD_ID, "textures/gui/sprites/drop_all_button.png");
    private final ResourceLocation buttonHoverTexture = new ResourceLocation(MOD_ID, "textures/gui/sprites/drop_all_button_highlighted.png");

    public DropAllButton(Builder<DropAllButton> builder) {
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
        interactWithAllItems(ClickType.THROW, 1);
    }
}
