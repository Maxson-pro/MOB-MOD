package com.example;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final SoundEvent MOB_CUSTOM_IDLE = registerSoundEvent("mob_custom_idle");
    public static final SoundEvent MOB_CUSTOM_HURT = registerSoundEvent("mob_custom_hurt");

    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(ExampleMod.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerSounds() {
        ExampleMod.LOGGER.info("Registering ModSounds for " + ExampleMod.MOD_ID);
    }
}