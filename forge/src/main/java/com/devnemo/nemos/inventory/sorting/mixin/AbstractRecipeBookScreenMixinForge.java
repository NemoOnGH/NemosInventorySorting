package com.devnemo.nemos.inventory.sorting.mixin;

import com.devnemo.nemos.inventory.sorting.gui.components.RecipeBookUpdatable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractRecipeBookScreenMixinForge extends Screen {

    @Shadow protected int leftPos;

    protected AbstractRecipeBookScreenMixinForge(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(method = "render", at = @At(value = "HEAD"))
    private void updateXPosition(CallbackInfo ci) {
        children().stream()
                .filter(widget -> widget instanceof RecipeBookUpdatable)
                .forEach(widget -> ((RecipeBookUpdatable) widget).updateXPosition(leftPos));
    }
}
