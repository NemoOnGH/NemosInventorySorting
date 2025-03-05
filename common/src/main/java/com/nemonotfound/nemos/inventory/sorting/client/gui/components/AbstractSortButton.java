package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nemonotfound.nemos.inventory.sorting.interfaces.GuiPosition;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSortButton extends AbstractWidget {

    protected final AbstractContainerScreen<?> containerScreen;
    protected final Integer startIndex;
    protected final Integer endIndex;
    private final int x;
    private final int y;
    private final int xOffset;
    private final Component buttonName;
    private final Component shiftButtonName;

    protected boolean isShiftKeyDown = false;

    public AbstractSortButton(Builder<? extends  AbstractSortButton> builder) {
        super(builder.x, builder.y, builder.width, builder.height, builder.buttonName);
        this.setTooltip(Tooltip.create(builder.buttonName));
        this.buttonName = builder.buttonName;
        this.shiftButtonName = builder.shiftButtonName;
        this.containerScreen = builder.containerScreen;
        this.startIndex = builder.startIndex;
        this.endIndex = builder.endIndex;
        this.x = builder.x;
        this.y = builder.y;
        this.xOffset = builder.xOffset;
    }

    @Override
    public void renderWidget(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.setPosition(x, y);

        if (containerScreen instanceof InventoryScreen) {
            int leftPos = ((GuiPosition) containerScreen).nemosInventorySorting$getLeftPos();

            this.setX(leftPos + this.xOffset);
        }

        if (this.isHovered()) {
            this.renderTexture(poseStack, getButtonHoverTexture(), this.getX(), this.getY(), 0, 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
        } else {
            this.renderTexture(poseStack, getButtonTexture(), this.getX(), this.getY(), 0, 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
        }
    }

    protected abstract ResourceLocation getButtonHoverTexture();
    protected abstract ResourceLocation getButtonTexture();

    public void setIsShiftKeyDown(boolean shiftKeyDown) {
        isShiftKeyDown = shiftKeyDown;
    }

    public void setTooltip(AbstractContainerMenu menu) {
        if (isButtonShiftable(menu)) {
            setTooltip(Tooltip.create(shiftButtonName));
        } else {
            setTooltip(Tooltip.create(buttonName));
        }
    }

    protected int calculateEndIndex(AbstractContainerMenu menu) {
        if (isButtonShiftable(menu)) {
            return endIndex + 9;
        }

        return endIndex;
    }

    private boolean isButtonShiftable(AbstractContainerMenu menu) {
        return isShiftKeyDown && (startIndex != 0 || menu instanceof InventoryMenu);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }

    public static class Builder<T extends AbstractSortButton> {
        private Integer startIndex;
        private Integer endIndex;
        private Integer x;
        private Integer y;
        private Integer xOffset;
        private Integer width;
        private Integer height;
        private Component buttonName;
        private Component shiftButtonName;
        private AbstractContainerScreen<?> containerScreen;
        private final Class<T> clazz;

        public Builder(Class<T> clazz) {
            this.clazz = clazz;
        }

        public Builder<T> startIndex(int startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        public Builder<T> endIndex(int endIndex) {
            this.endIndex = endIndex;
            return this;
        }

        public Builder<T> x(int x) {
            this.x = x;
            return this;
        }

        public Builder<T> y(int y) {
            this.y = y;
            return this;
        }

        public Builder<T> xOffset(int xOffset) {
            this.xOffset = xOffset;
            return this;
        }

        public Builder<T> width(int width) {
            this.width = width;
            return this;
        }

        public Builder<T> height(int height) {
            this.height = height;
            return this;
        }

        public Builder<T> buttonName(Component buttonName) {
            this.buttonName = buttonName;
            return this;
        }

        public Builder<T> shiftButtonName(Component shiftButtonName) {
            this.shiftButtonName = shiftButtonName;
            return this;
        }

        public Builder<T> containerScreen(AbstractContainerScreen<?> containerScreen) {
            this.containerScreen = containerScreen;
            return this;
        }

        public T build() {
            checkRequiredFields();

            try {
                return clazz.getDeclaredConstructor(Builder.class).newInstance(this);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create instance of " + clazz.getName(), e);
            }
        }

        private void checkRequiredFields() {
            if (startIndex == null || endIndex == null || x == null || y == null || xOffset == null || width == null
                    || height == null || buttonName == null || containerScreen == null) {
                throw new IllegalArgumentException("Not all fields were set!");
            }
        }
    }
}
