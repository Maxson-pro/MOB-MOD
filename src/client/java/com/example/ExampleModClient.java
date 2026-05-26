package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Связываем моба с его рендерером
        EntityRendererRegistry.register(ModEntities.CUSTOM_MOB, CustomMobRenderer::new);
    }
}
