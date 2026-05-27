package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
    public static final String MOD_ID = "modid";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // 1. Создаём и регистрируем собственную креативную вкладку
    public static final CreativeModeTab CUSTOM_MOB_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(MOD_ID, "custom_mob_tab"), // Поменяли ID
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup.modid.custom_mob_tab")) // Новый ключ
                    .icon(() -> new ItemStack(ModItems.CUSTOM_MOB_SPAWN_EGG))
                    .displayItems((displayContext, entries) -> {
                        entries.accept(ModItems.CUSTOM_MOB_SPAWN_EGG);
                    })
                    .build()
    );

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Mod...");

        // Регистрируем моба
        ModEntities.registerEntities();

        // Регистрируем дефолтные атрибуты моба
        FabricDefaultAttributeRegistry.register(ModEntities.CUSTOM_MOB, com.example.CustomMobEntity.createMobAttributes());

        // Регистрируем звуки
        ModSounds.registerSounds();

        // Регистрируем предметы
        ModItems.registerItems();
    }
}