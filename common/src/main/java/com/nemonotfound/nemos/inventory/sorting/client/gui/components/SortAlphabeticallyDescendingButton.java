package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.Map;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

public class SortAlphabeticallyDescendingButton extends AbstractSortAlphabeticallyButton {

    private final ResourceLocation buttonTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "sort_button_alphabetically_dec");
    private final ResourceLocation buttonHoverTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "sort_button_alphabetically_dec_highlighted");

    public SortAlphabeticallyDescendingButton(Builder<SortAlphabeticallyDescendingButton> builder) {
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
    protected Comparator<Map.Entry<Integer, ItemStack>> compare() {
        return super.compare().reversed();
    }
}
