package com.example;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class CustomMobEntity extends PathfinderMob implements GeoEntity {
    private int soundTimer = 0;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public CustomMobEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }
    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            soundTimer++;
            if (soundTimer >= 200) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                        ModSounds.MOB_CUSTOM_IDLE, SoundSource.NEUTRAL, 1.0F, 1.0F);
                soundTimer = 0;
            }

            if (this.tickCount % 10 == 0 && this.onGround()) {
                if (isDiamondNearby()) {
                    this.jumpFromGround();
                }
            }
        }
    }

    private boolean isDiamondNearby() {
        BlockPos mobPos = this.blockPosition();

        for (BlockPos pos : BlockPos.betweenClosed(mobPos.offset(-5, -5, -5), mobPos.offset(5, 5, 5))) {
            Block block = this.level().getBlockState(pos).getBlock();
            if (block == Blocks.DIAMOND_BLOCK || block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE) {
                return true;
            }
        }

        List<ItemEntity> items = this.level().getEntitiesOfClass(
                ItemEntity.class,
                this.getBoundingBox().inflate(5.0D),
                item -> item.getItem().is(Items.DIAMOND)
        );
        return !items.isEmpty();
    }

    @Override
    protected void jumpFromGround() {
        super.jumpFromGround();

        if (!this.level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            serverLevel.sendParticles(ParticleTypes.SPLASH,
                    this.getX(), this.getY(), this.getZ(),
                    15, 0.5D, 0.1D, 0.5D, 0.1D);
        }
    }


    // Внутри CustomMobEntity.java
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new software.bernie.geckolib.core.animation.AnimationController<>(
                this, "controller", 0, state -> {
            // Если моб движется - запускаем анимацию "animation" (имя из твоего .animation.json)
            if (state.isMoving()) {
                return state.setAndContinue(software.bernie.geckolib.core.animation.RawAnimation.begin().thenLoop("animation"));
            }
            // Если стоит на месте
            return state.setAndContinue(software.bernie.geckolib.core.animation.RawAnimation.begin().thenLoop("animation"));
        }
        ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        // Звук, когда моб "просто существует"
        return ModSounds.MOB_CUSTOM_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // Звук, когда моба бьют
        return ModSounds.MOB_CUSTOM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        // Звук смерти (по желанию)
        return ModSounds.MOB_CUSTOM_HURT; // Можно использовать тот же или добавить новый
    }
}