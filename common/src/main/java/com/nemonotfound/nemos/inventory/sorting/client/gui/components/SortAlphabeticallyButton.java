package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import net.minecraft.resources.ResourceLocation;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

public class SortAlphabeticallyButton extends AbstractSortAlphabeticallyButton {

    private final ResourceLocation buttonTexture = new ResourceLocation(MOD_ID, "textures/gui/sprites/sort_button_alphabetically_inc.png");
    private final ResourceLocation buttonHoverTexture = new ResourceLocation(MOD_ID, "textures/gui/sprites/sort_button_alphabetically_inc_highlighted.png");

    public SortAlphabeticallyButton(Builder<SortAlphabeticallyButton> builder) {
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
}
