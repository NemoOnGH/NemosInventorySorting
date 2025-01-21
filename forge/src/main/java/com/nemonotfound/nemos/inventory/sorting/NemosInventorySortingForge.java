package com.nemonotfound.nemos.inventory.sorting;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

@Mod(MOD_ID)
public class NemosInventorySortingForge {

    public NemosInventorySortingForge(FMLJavaModLoadingContext context) {
        NemosInventorySortingCommon.init();

        context.getModEventBus().register(this);
    }

    @SubscribeEvent
    public void addBuiltInResourcePack(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            var resourcePath = ModList.get().getModFileById(MOD_ID).getFile().findResource("resourcepacks/dark_mode");
            var pathPackResources = new PathPackResources("dark_mode", resourcePath, true);
            var pack = Pack.readMetaAndCreate(
                    "builtin/dark_mode",
                    Component.translatable("resourcePack.nemos_inventory_sorting.dark_mode.name"),
                    false,
                    (supplier) -> pathPackResources,
                    PackType.CLIENT_RESOURCES,
                    Pack.Position.TOP,
                    PackSource.BUILT_IN
            );

            event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
        }
    }
}