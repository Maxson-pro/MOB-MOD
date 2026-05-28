package com.example;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CustomMobModel extends GeoModel<CustomMobEntity> {

    @Override
    public ResourceLocation getModelResource(CustomMobEntity animatable) {
        return new ResourceLocation(ExampleMod.MOD_ID, "geo/custom_mob.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CustomMobEntity animatable) {
        // Убедись, что файл лежит именно по этому пути!
        return new ResourceLocation(ExampleMod.MOD_ID, "textures/entity/custom_mob.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CustomMobEntity animatable) {
        return new ResourceLocation(ExampleMod.MOD_ID, "animations/custom_mob.animation.json");
    }
}