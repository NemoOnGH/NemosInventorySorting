package com.nemonotfound.nemos.inventory.sorting.gui.components.buttons;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ClickType;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

public class MoveAllButton extends AbstractSingleClickButton<MoveAllButton> {

    private final ResourceLocation buttonTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "move_all_button");
    private final ResourceLocation buttonHoverTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "move_all_button_highlighted");

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
        interactWithAllItems(ClickType.QUICK_MOVE, 0);
    }
}
