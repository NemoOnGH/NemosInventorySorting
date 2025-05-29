package com.nemonotfound.nemos.inventory.sorting.config;

import com.nemonotfound.nemos.inventory.sorting.config.model.ComponentConfig;
import com.nemonotfound.nemos.inventory.sorting.config.model.FilterConfig;

import java.util.ArrayList;
import java.util.List;

import static com.nemonotfound.nemos.inventory.sorting.config.DefaultConfigValues.*;

public class DefaultConfigs {


    public static final List<ComponentConfig> DEFAULT_COMPONENT_CONFIGS = new ArrayList<>();
    public static final FilterConfig DEFAULT_FILTER_CONFIG = new FilterConfig();

    private DefaultConfigs() {}

    public static void setupDefaultConfigs() {
        setupDefaultComponentConfigs();
    }

    private static void setupDefaultComponentConfigs() {
        createAndAddComponentConfig(
                SORT_ALPHABETICALLY_CONTAINER,
                X_OFFSET_SORT_ALPHABETICALLY_CONTAINER,
                Y_OFFSET_CONTAINER,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        createAndAddComponentConfig(
                SORT_ALPHABETICALLY_DESCENDING_CONTAINER,
                X_OFFSET_SORT_ALPHABETICALLY_DESCENDING_CONTAINER,
                Y_OFFSET_CONTAINER,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        createAndAddComponentConfig(
                MOVE_SAME_CONTAINER,
                X_OFFSET_MOVE_SAME_CONTAINER,
                Y_OFFSET_CONTAINER,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        createAndAddComponentConfig(
                MOVE_ALL_CONTAINER,
                X_OFFSET_MOVE_ALL_CONTAINER,
                Y_OFFSET_CONTAINER,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        createAndAddComponentConfig(
                DROP_ALL_CONTAINER,
                X_OFFSET_DROP_ALL_CONTAINER,
                Y_OFFSET_CONTAINER,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        createAndAddComponentConfig(
                SORT_ALPHABETICALLY_CONTAINER_INVENTORY,
                X_OFFSET_SORT_ALPHABETICALLY_CONTAINER,
                null,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        createAndAddComponentConfig(
                SORT_ALPHABETICALLY_DESCENDING_CONTAINER_INVENTORY,
                X_OFFSET_SORT_ALPHABETICALLY_DESCENDING_CONTAINER,
                null,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        createAndAddComponentConfig(
                MOVE_SAME_CONTAINER_INVENTORY,
                X_OFFSET_MOVE_SAME_CONTAINER,
                null,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        createAndAddComponentConfig(
                MOVE_ALL_CONTAINER_INVENTORY,
                X_OFFSET_MOVE_ALL_CONTAINER,
                null,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        createAndAddComponentConfig(
                DROP_ALL_CONTAINER_INVENTORY,
                X_OFFSET_DROP_ALL_CONTAINER,
                null,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        createAndAddComponentConfig(
                SORT_ALPHABETICALLY_INVENTORY,
                X_OFFSET_SORT_ALPHABETICALLY_INVENTORY,
                Y_OFFSET_INVENTORY,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        createAndAddComponentConfig(
                SORT_ALPHABETICALLY_DESCENDING_INVENTORY,
                X_OFFSET_SORT_ALPHABETICALLY_DESCENDING_INVENTORY,
                Y_OFFSET_INVENTORY,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        createAndAddComponentConfig(
                DROP_ALL_INVENTORY,
                X_OFFSET_DROP_ALL_INVENTORY,
                Y_OFFSET_INVENTORY,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        createAndAddComponentConfig(
                ITEM_FILTER,
                X_OFFSET_ITEM_FILTER,
                Y_OFFSET_ITEM_FILTER,
                ITEM_FILTER_WIDTH,
                ITEM_FILTER_HEIGHT
        );
    }

    private static void createAndAddComponentConfig(String name, int xOffset, Integer yOffset, int width, int height) {
        var config = new ComponentConfig(name, true, xOffset, yOffset, width, height);

        DEFAULT_COMPONENT_CONFIGS.add(config);
    }
}
