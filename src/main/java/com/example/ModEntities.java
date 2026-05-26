package com.example;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    // Регистрация моба с хитбоксом: ширина 1.0 блок, высота 2.0 блока
    public static final EntityType<CustomMobEntity> CUSTOM_MOB = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ExampleMod.MOD_ID, "custom_mob"),
            FabricEntityTypeBuilder.create(MobCategory.CREATURE, CustomMobEntity::new)
                    .dimensions(EntityDimensions.fixed(1.0f, 2.0f)).build()
    );

    public static void registerEntities() {
        ExampleMod.LOGGER.info("Registering ModEntities for " + ExampleMod.MOD_ID);
    }
}