package com.example;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CustomMobRenderer extends GeoEntityRenderer<CustomMobEntity> {
    public CustomMobRenderer(EntityRendererProvider.Context renderManager) {
        // Передаем менеджер рендеринга и создаем экземпляр нашей модели
        super(renderManager, new CustomMobModel());

        // Тут можно настроить размер круглой тени под мобом (0.5f — как у свиньи или зомби)
        this.shadowRadius = 0.5f;
    }
}