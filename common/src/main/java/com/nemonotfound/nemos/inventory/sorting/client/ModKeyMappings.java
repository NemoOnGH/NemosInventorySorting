package com.nemonotfound.nemos.inventory.sorting.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.nemonotfound.nemos.inventory.sorting.NemosInventorySortingClientCommon;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

public class ModKeyMappings {

    private static final String category = String.format("category.%s.nemos_inventory_sorting", MOD_ID);

    public static Supplier<KeyMapping> DROP_ALL = registerKeyMapping(new KeyMapping(
            String.format("key.%s.drop_all", MOD_ID),
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> DROP_ALL_INVENTORY = registerKeyMapping(new KeyMapping(
            String.format("key.%s.drop_all_inventory", MOD_ID),
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> MOVE_ALL = registerKeyMapping(new KeyMapping(
            String.format("key.%s.move_all", MOD_ID),
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> MOVE_ALL_INVENTORY = registerKeyMapping(new KeyMapping(
            String.format("key.%s.move_all_inventory", MOD_ID),
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> MOVE_SAME = registerKeyMapping(new KeyMapping(
            String.format("key.%s.move_same", MOD_ID),
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> MOVE_SAME_INVENTORY = registerKeyMapping(new KeyMapping(
            String.format("key.%s.move_same_inventory", MOD_ID),
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> SORT_ALPHABETICALLY = registerKeyMapping(new KeyMapping(
            String.format("key.%s.sort_alphabetically", MOD_ID),
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> SORT_ALPHABETICALLY_INVENTORY = registerKeyMapping(new KeyMapping(
            String.format("key.%s.sort_alphabetically_inventory", MOD_ID),
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> SORT_ALPHABETICALLY_DESCENDING = registerKeyMapping(new KeyMapping(
            String.format("key.%s.sort_alphabetically_descending", MOD_ID),
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));
    public static Supplier<KeyMapping> SORT_ALPHABETICALLY_DESCENDING_INVENTORY = registerKeyMapping(new KeyMapping(
            String.format("key.%s.sort_alphabetically_descending_inventory", MOD_ID),
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            category
    ));

    private static Supplier<KeyMapping> registerKeyMapping(KeyMapping keyMapping) {
        return NemosInventorySortingClientCommon.REGISTRY_HELPER.registerKeyMapping(keyMapping);
    }

    public static void init() {}
}
