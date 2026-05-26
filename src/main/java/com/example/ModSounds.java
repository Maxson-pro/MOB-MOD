package com.example;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    // ID твоего кастомного звука
    public static final SoundEvent MOB_CUSTOM_SOUND = registerSoundEvent("mob_custom_sound");

    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(ExampleMod.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerSounds() {
        ExampleMod.LOGGER.info("Registering ModSounds for " + ExampleMod.MOD_ID);
    }
}