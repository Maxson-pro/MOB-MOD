package com.example;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public class ModItems {

    // Регистрация яйца призыва с кастомными цветами (первый HEX - фон, второй - точки)
    public static final Item CUSTOM_MOB_SPAWN_EGG = Registry.register(
            BuiltInRegistries.ITEM,
            new ResourceLocation(ExampleMod.MOD_ID, "custom_mob_spawn_egg"),
            new SpawnEggItem(ModEntities.CUSTOM_MOB, 0xC4AA79, 0x7A5A3F, new Item.Properties())
    );

    public static void registerItems() {
        ExampleMod.LOGGER.info("Registering ModItems for " + ExampleMod.MOD_ID);
    }
}