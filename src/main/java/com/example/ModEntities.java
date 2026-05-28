package com.example;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final EntityType<CustomMobEntity> CUSTOM_MOB = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(ExampleMod.MOD_ID, "custom_mob"),
            FabricEntityTypeBuilder.create(MobCategory.CREATURE, CustomMobEntity::new)
                    // Меняй здесь: 0.6f - ширина, 1.0f - высота
                    .dimensions(EntityDimensions.fixed(0.6f, 1.0f)).build()
    );

    public static void registerEntities() {
        ExampleMod.LOGGER.info("Registering Entities");
    }
}