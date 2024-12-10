package com.nemonotfound.nemos.inventory.sorting.client.gui.components;

import com.nemonotfound.nemos.inventory.sorting.interfaces.GuiPosition;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSortButton extends AbstractWidget {

    protected final AbstractContainerScreen<?> containerScreen;
    protected final Integer startIndex;
    protected final Integer endIndex;
    private final int x;
    private final int y;
    private final int xOffset;

    public AbstractSortButton(Builder<? extends  AbstractSortButton> builder) {
        super(builder.x, builder.y, builder.width, builder.height, builder.component);
        this.setTooltip(Tooltip.create(builder.component));
        this.containerScreen = builder.containerScreen;
        this.startIndex = builder.startIndex;
        this.endIndex = builder.endIndex;
        this.x = builder.x;
        this.y = builder.y;
        this.xOffset = builder.xOffset;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.setPosition(x, y);

        if (containerScreen instanceof CreativeModeInventoryScreen creativeModeInventoryScreen && !creativeModeInventoryScreen.isInventoryOpen()) {
            this.setPosition(-10, -10);
            return;
        }

        if (containerScreen instanceof InventoryScreen) {
            int leftPos = ((GuiPosition) containerScreen).nemosInventorySorting$getLeftPos();
            int imageWidth = ((GuiPosition) containerScreen).nemosInventorySorting$getImageWidth();

            this.setX(leftPos + imageWidth - this.xOffset);
        }

        if (this.isHovered()) {
            guiGraphics.blitSprite(RenderType::guiTextured, getButtonHoverTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        } else {
            guiGraphics.blitSprite(RenderType::guiTextured, getButtonTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
        }
    }

    protected abstract ResourceLocation getButtonHoverTexture();
    protected abstract ResourceLocation getButtonTexture();

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
        private Component component;
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

        public Builder<T> component(Component component) {
            this.component = component;
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
                    || height == null || component == null || containerScreen == null) {
                throw new IllegalArgumentException("Not all fields were set!");
            }
        }
    }
}
