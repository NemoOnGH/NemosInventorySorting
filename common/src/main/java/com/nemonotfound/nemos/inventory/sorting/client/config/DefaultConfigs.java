package com.nemonotfound.nemos.inventory.sorting.client.config;

import java.util.List;

import static com.nemonotfound.nemos.inventory.sorting.Constants.*;

public class DefaultConfigs {

    private static DefaultConfigs INSTANCE;

    public static DefaultConfigs getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultConfigs();
        }

        return INSTANCE;
    }

    public void setupDefaultConfigs() {
        ComponentConfig sortAlphabeticallyContainerConfig = new ComponentConfig(
                SORT_ALPHABETICALLY_CONTAINER,
                true,
                X_OFFSET_SORT_ALPHABETICALLY_CONTAINER,
                Y_OFFSET_CONTAINER,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        ComponentConfig sortAlphabeticallyDescendingContainerConfig = new ComponentConfig(
                SORT_ALPHABETICALLY_DESCENDING_CONTAINER,
                true,
                X_OFFSET_SORT_ALPHABETICALLY_DESCENDING_CONTAINER,
                Y_OFFSET_CONTAINER,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        ComponentConfig moveSameContainerConfig = new ComponentConfig(
                MOVE_SAME_CONTAINER,
                true,
                X_OFFSET_MOVE_SAME_CONTAINER,
                Y_OFFSET_CONTAINER,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        ComponentConfig moveAllContainerConfig = new ComponentConfig(
                MOVE_ALL_CONTAINER,
                true,
                X_OFFSET_MOVE_ALL_CONTAINER,
                Y_OFFSET_CONTAINER,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        ComponentConfig dropAllContainerConfig = new ComponentConfig(
                DROP_ALL_CONTAINER,
                true,
                X_OFFSET_DROP_ALL_CONTAINER,
                Y_OFFSET_CONTAINER,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        ComponentConfig sortAlphabeticallyContainerInventoryConfig = new ComponentConfig(
                SORT_ALPHABETICALLY_CONTAINER_INVENTORY,
                true,
                X_OFFSET_SORT_ALPHABETICALLY_CONTAINER,
                null,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        ComponentConfig sortAlphabeticallyDescendingContainerInventoryConfig = new ComponentConfig(
                SORT_ALPHABETICALLY_DESCENDING_CONTAINER_INVENTORY,
                true,
                X_OFFSET_SORT_ALPHABETICALLY_DESCENDING_CONTAINER,
                null,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        ComponentConfig moveSameContainerInventoryConfig = new ComponentConfig(
                MOVE_SAME_CONTAINER_INVENTORY,
                true,
                X_OFFSET_MOVE_SAME_CONTAINER,
                null,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        ComponentConfig moveAllContainerInventoryConfig = new ComponentConfig(
                MOVE_ALL_CONTAINER_INVENTORY,
                true,
                X_OFFSET_MOVE_ALL_CONTAINER,
                null,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        ComponentConfig dropAllContainerInventoryConfig = new ComponentConfig(
                DROP_ALL_CONTAINER_INVENTORY,
                true,
                X_OFFSET_DROP_ALL_CONTAINER,
                null,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        ComponentConfig sortAlphabeticallyInventoryConfig = new ComponentConfig(
                SORT_ALPHABETICALLY_INVENTORY,
                true,
                X_OFFSET_SORT_ALPHABETICALLY_INVENTORY,
                Y_OFFSET_INVENTORY,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        ComponentConfig sortAlphabeticallyDescendingInventoryConfig = new ComponentConfig(
                SORT_ALPHABETICALLY_DESCENDING_INVENTORY,
                true,
                X_OFFSET_SORT_ALPHABETICALLY_DESCENDING_INVENTORY,
                Y_OFFSET_INVENTORY,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        ComponentConfig dropAllInventoryConfig = new ComponentConfig(
                DROP_ALL_INVENTORY,
                true,
                X_OFFSET_DROP_ALL_INVENTORY,
                Y_OFFSET_INVENTORY,
                BUTTON_SIZE,
                BUTTON_SIZE
        );
        ComponentConfig itemFilterConfig = new ComponentConfig(
                ITEM_FILTER,
                true,
                X_OFFSET_ITEM_FILTER,
                Y_OFFSET_ITEM_FILTER,
                ITEM_FILTER_WIDTH,
                ITEM_FILTER_HEIGHT
        );

        ConfigUtil.writeConfigs(List.of(
                sortAlphabeticallyContainerConfig,
                sortAlphabeticallyDescendingContainerConfig,
                moveSameContainerConfig,
                moveAllContainerConfig,
                dropAllContainerConfig,
                sortAlphabeticallyContainerInventoryConfig,
                sortAlphabeticallyDescendingContainerInventoryConfig,
                moveSameContainerInventoryConfig,
                dropAllContainerInventoryConfig,
                moveAllContainerInventoryConfig,
                sortAlphabeticallyInventoryConfig,
                sortAlphabeticallyDescendingInventoryConfig,
                dropAllInventoryConfig,
                itemFilterConfig
        ));
    }
}
