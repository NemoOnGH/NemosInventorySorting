package com.nemonotfound.nemos.inventory.sorting.client.config;

public class ComponentConfig {

    private final String componentName;
    private final boolean isEnabled;
    private final int xOffset;
    private final Integer yOffset;
    private final int width;
    private final int height;

    public ComponentConfig(String componentName, boolean isEnabled, int xOffset, Integer yOffset, int width, int height) {
        this.componentName = componentName;
        this.isEnabled = isEnabled;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.width = width;
        this.height = height;
    }

    public String getComponentName() {
        return componentName;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public int getxOffset() {
        return xOffset;
    }

    public Integer getyOffset() {
        return yOffset;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
