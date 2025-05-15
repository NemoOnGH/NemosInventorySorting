package com.nemonotfound.nemos.inventory.sorting.client.service;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.stream.Collectors;

public class TooltipService {

    private static TooltipService INSTANCE;

    private final Minecraft minecraft;

    public TooltipService(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public static TooltipService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new TooltipService(Minecraft.getInstance());
        }

        return INSTANCE;
    }

    public String retrieveEnchantmentNames(ItemStack itemStack) {
        return retrieveTooltipLines(itemStack).stream()
                .filter(component -> component.toString().contains("enchantment"))
                .map(Component::getString)
                .collect(Collectors.joining(","));
    }

    public List<Component> retrieveTooltipLines(ItemStack itemStack) {
        return itemStack.getTooltipLines(
                Item.TooltipContext.of(minecraft.level),
                minecraft.player,
                minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL
        );

    }
}
