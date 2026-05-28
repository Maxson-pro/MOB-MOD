package com.example;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.InteractionResult;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class CustomMobEntity extends PathfinderMob implements GeoEntity {
    private int soundTimer = 0;
    private boolean isTamed = false;
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
        List<ItemEntity> items = this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(5.0D), item -> item.getItem().is(Items.DIAMOND));
        return !items.isEmpty();
    }

    @Override
    protected void jumpFromGround() {
        super.jumpFromGround();
        if (!this.level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            // Частицы на мобе
            serverLevel.sendParticles(ParticleTypes.SPLASH, this.getX(), this.getY() + 0.5, this.getZ(), 15, 0.3D, 0.3D, 0.3D, 0.1D);
            // Частицы вокруг игроков
            List<Player> players = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(10.0D));
            for (Player player : players) {
                serverLevel.sendParticles(ParticleTypes.SPLASH, player.getX(), player.getY() + 1, player.getZ(), 5, 0.2D, 0.2D, 0.2D, 0.05D);
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        // Приручение
        if (stack.is(Items.DIAMOND) && !isTamed) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            this.isTamed = true;
            this.setPersistenceRequired();
            this.level().broadcastEntityEvent(this, (byte) 7);
            return InteractionResult.SUCCESS;
        }
        // Езда
        if (this.isTamed && !player.isSecondaryUseActive()) {
            if (!this.level().isClientSide()) player.startRiding(this);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) this.spawnHearts();
        else super.handleEntityEvent(id);
    }

    private void spawnHearts() {
        for (int i = 0; i < 7; ++i) {
            this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
        }
    }

    @Override
    public void positionRider(net.minecraft.world.entity.Entity passenger, net.minecraft.world.entity.Entity.MoveFunction moveFunction) {
        super.positionRider(passenger, moveFunction);

        // Сейчас у тебя там, скорее всего, написано что-то вроде + 1.5 или + 1.2
        // Попробуй уменьшить это число. Например, начни с 0.5 или 0.6
        passenger.setPos(this.getX(), this.getY() + 0.2, this.getZ());
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.getFirstPassenger() instanceof Player player) return player;
        return super.getControllingPassenger();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new software.bernie.geckolib.core.animation.AnimationController<>(this, "controller", 0, state -> {
            return state.setAndContinue(software.bernie.geckolib.core.animation.RawAnimation.begin().thenLoop("animation"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return this.cache; }

    @Nullable @Override protected SoundEvent getAmbientSound() { return ModSounds.MOB_CUSTOM_IDLE; }
    @Override protected SoundEvent getHurtSound(DamageSource source) { return ModSounds.MOB_CUSTOM_HURT; }
    @Override protected SoundEvent getDeathSound() { return ModSounds.MOB_CUSTOM_HURT; }
}