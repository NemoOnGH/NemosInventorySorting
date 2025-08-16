package com.devnemo.nemos.inventory.sorting.mixin;

import com.devnemo.nemos.inventory.sorting.gui.components.FilterBox;
import com.devnemo.nemos.inventory.sorting.gui.components.RecipeBookUpdatable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractRecipeBookScreenMixin extends Screen {

    @Shadow protected int leftPos;

    protected AbstractRecipeBookScreenMixin(Component component) {
        super(component);
    }

    /**
     * Updates the X position of all child widgets that implement {@link RecipeBookUpdatable}
     * after the screen has finished initializing.
     * <p>
     * This ensures that custom widgets are correctly aligned with the final value of {@link #leftPos},
     * when the recipe book is toggled.
     */
    @Inject(method = "init", at = @At("TAIL"))
    private void updateXPosition(CallbackInfo ci) {
        children().stream()
                .filter(widget -> widget instanceof RecipeBookUpdatable)
                .forEach(widget -> ((RecipeBookUpdatable) widget).updateXPosition(leftPos));
    }

    /**
     * Prevents the recipe book from handling key input when a {@link FilterBox} exists and is focused.
     */
    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        var optionalFilterBox = children().stream()
                .filter(widget -> widget instanceof FilterBox)
                .findFirst();

        if (optionalFilterBox.isPresent() && optionalFilterBox.get().isFocused()) {
            cir.setReturnValue(super.keyPressed(keyCode, scanCode, modifiers));
        }
    }
}
