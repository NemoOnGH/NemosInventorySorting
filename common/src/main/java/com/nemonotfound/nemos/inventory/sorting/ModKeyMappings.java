package com.nemonotfound.nemos.inventory.sorting;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

public class ModKeyMappings {

    private static final String category = String.format("%s.category.nemosInventorySorting", MOD_ID);

    public static Supplier<KeyMapping> DROP_ALL = registerKeyMapping(new KeyMapping(
            String.format("%s.key.dropAll", MOD_ID),
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> DROP_ALL_INVENTORY = registerKeyMapping(new KeyMapping(
            String.format("%s.key.dropAllInventory", MOD_ID),
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> MOVE_ALL = registerKeyMapping(new KeyMapping(
            String.format("%s.key.moveAll", MOD_ID),
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> MOVE_ALL_INVENTORY = registerKeyMapping(new KeyMapping(
            String.format("%s.key.moveAllInventory", MOD_ID),
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> MOVE_SAME = registerKeyMapping(new KeyMapping(
            String.format("%s.key.moveSame", MOD_ID),
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> MOVE_SAME_INVENTORY = registerKeyMapping(new KeyMapping(
            String.format("%s.key.moveSameInventory", MOD_ID),
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> SORT_ALPHABETICALLY = registerKeyMapping(new KeyMapping(
            String.format("%s.key.sortAlphabetically", MOD_ID),
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> SORT_ALPHABETICALLY_INVENTORY = registerKeyMapping(new KeyMapping(
            String.format("%s.key.sortAlphabeticallyInventory", MOD_ID),
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> SORT_ALPHABETICALLY_DESCENDING = registerKeyMapping(new KeyMapping(
            String.format("%s.key.sortAlphabeticallyDescending", MOD_ID),
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> SORT_ALPHABETICALLY_DESCENDING_INVENTORY = registerKeyMapping(new KeyMapping(
            String.format("%s.key.sortAlphabeticallyDescendingInventory", MOD_ID),
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> TOGGLE_FILTER_PERSISTENCE = registerKeyMapping(new KeyMapping(
            String.format("%s.key.toggleFilterPersistence", MOD_ID),
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));

    private static Supplier<KeyMapping> registerKeyMapping(KeyMapping keyMapping) {
        return NemosInventorySortingClientCommon.REGISTRY_HELPER.registerKeyMapping(keyMapping);
    }

    public static void init() {}
}
