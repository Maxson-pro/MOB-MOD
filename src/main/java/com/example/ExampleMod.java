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
    public static final CreativeModeTab ALEXANDRITE_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(MOD_ID, "alexandrite_tab"),
            FabricItemGroup.builder()
                    // Имя вкладки для файла перевода (lang)
                    .title(Component.translatable("itemGroup.modid.alexandrite_tab"))
                    // Иконка вкладки (в качестве иконки будет наше яйцо призыва)
                    .icon(() -> new ItemStack(ModItems.CUSTOM_MOB_SPAWN_EGG))
                    // Добавляем предметы внутрь этой вкладки
                    .displayItems((displayContext, entries) -> {
                        entries.accept(ModItems.CUSTOM_MOB_SPAWN_EGG); // Наше яйцо
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