package com.nemonotfound.nemos.inventory.sorting;

import com.nemonotfound.nemos.inventory.sorting.client.ModKeyMappings;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.resource.PathPackResources;
import net.minecraftforge.fml.loading.FMLEnvironment;

import static com.nemonotfound.nemos.inventory.sorting.Constants.MOD_ID;

@Mod(MOD_ID)
public class NemosInventorySortingForge {

    public NemosInventorySortingForge(FMLJavaModLoadingContext context) {
        if (FMLEnvironment.dist.isClient()) {
            NemosInventorySortingClientCommon.init();

            context.getModEventBus().register(this);
        }
    }

    @SubscribeEvent
    public void addBuiltInResourcePack(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            var resourcePath = ModList.get().getModFileById(MOD_ID).getFile().findResource("resourcepacks/dark_mode");
            var pathPackResources = new PathPackResources(Component.translatable("resourcePack.nemos_inventory_sorting.dark_mode.name").toString(), resourcePath);
            Pack.PackConstructor packConstructor = ($$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8) -> new Pack($$1, Component.translatable("resourcePack.nemos_inventory_sorting.dark_mode.name"), $$3, $$4, $$5, PackType.CLIENT_RESOURCES, $$6, $$7);
            var pack = Pack.create(
                    "builtin/dark_mode",
                    false,
                    () -> pathPackResources,
                    packConstructor,
                    Pack.Position.TOP,
                    PackSource.BUILT_IN
            );

            event.addRepositorySource((repositorySource, source) -> repositorySource.accept(pack));
        }
    }

    @SubscribeEvent
    public void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ModKeyMappings.DROP_ALL.get());
        event.register(ModKeyMappings.DROP_ALL_INVENTORY.get());
        event.register(ModKeyMappings.MOVE_ALL.get());
        event.register(ModKeyMappings.MOVE_ALL_INVENTORY.get());
        event.register(ModKeyMappings.MOVE_SAME.get());
        event.register(ModKeyMappings.MOVE_SAME_INVENTORY.get());
        event.register(ModKeyMappings.SORT_ALPHABETICALLY.get());
        event.register(ModKeyMappings.SORT_ALPHABETICALLY_INVENTORY.get());
        event.register(ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING.get());
        event.register(ModKeyMappings.SORT_ALPHABETICALLY_DESCENDING_INVENTORY.get());
    }
}