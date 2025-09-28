package com.devnemo.nemos.inventory.sorting.gui.components.buttons.filter;

import com.devnemo.nemos.inventory.sorting.client.InventorySortingKeyMappings;
import com.devnemo.nemos.inventory.sorting.config.service.ConfigService;
import com.devnemo.nemos.inventory.sorting.gui.components.buttons.AbstractFilterToggleButton;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static com.devnemo.nemos.inventory.sorting.Constants.MOD_ID;
import static com.devnemo.nemos.inventory.sorting.config.DefaultConfigValues.FILTER_CONFIG_PATH;

public class ToggleFilterPersistenceButton extends AbstractFilterToggleButton {

    private final ResourceLocation toggleOffTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "filter_persistence_toggle_off");
    private final ResourceLocation toggleOnTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "filter_persistence_toggle_on");
    private final ResourceLocation toggleOffHoverTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "filter_persistence_toggle_off_highlighted");
    private final ResourceLocation toggleOnHoverTexture = ResourceLocation.fromNamespaceAndPath(MOD_ID, "filter_persistence_toggle_on_highlighted");
    private final Component toggleOnComponent = Component.translatable("nemos_inventory_sorting.gui.toggleFilterPersistence.toggleOn");
    private final Component toggleOffComponent = Component.translatable("nemos_inventory_sorting.gui.toggleFilterPersistence.toggleOff");
    private final ConfigService configService;

    public ToggleFilterPersistenceButton(Builder<? extends AbstractFilterToggleButton> builder) {
        super(builder);
        configService = ConfigService.getInstance();
        setTooltip();
    }

    @Override
    protected ResourceLocation getToggleOffHoverTexture() {
        return toggleOffHoverTexture;
    }

    @Override
    protected ResourceLocation getToggleOnHoverTexture() {
        return toggleOnHoverTexture;
    }

    @Override
    protected ResourceLocation getToggleOffTexture() {
        return toggleOffTexture;
    }

    @Override
    protected ResourceLocation getToggleOnTexture() {
        return toggleOnTexture;
    }

    @Override
    protected void setTooltip() {
        var tooltipComponent = filterConfig.isFilterPersistent() ? toggleOffComponent : toggleOnComponent;

        setTooltip(Tooltip.create(tooltipComponent));
    }

    @Override
    public void onClick(@NotNull MouseButtonEvent mouseButtonEvent, boolean bl) {
        filterConfig.toggleFilterPersistence();

        configService.writeConfig(true, FILTER_CONFIG_PATH, filterConfig);
        setTooltip();
    }

    @Override
    protected KeyMapping getKeyMapping() {
        return InventorySortingKeyMappings.TOGGLE_FILTER_PERSISTENCE.get();
    }
}
