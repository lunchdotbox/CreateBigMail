package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import javax.annotation.Nonnull;

import com.simibubi.create.content.logistics.box.PackageEntity;
import gay.lunch.createbigmail.index.CBMBlocks;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.InertBigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class MailShotProjectile extends AbstractBigCannonProjectile {
    private static final EntityDataAccessor<ItemStack> PACKAGE = SynchedEntityData.defineId(MailShotProjectile.class, EntityDataSerializers.ITEM_STACK);

    public MailShotProjectile(EntityType<? extends MailShotProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PACKAGE, ItemStack.EMPTY);
    }

    public boolean hasPackage() {
        return !this.getTracer().isEmpty();
    }

    public void setPackage(ItemStack stack) {
        this.entityData.set(PACKAGE, stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack);
    }

    public ItemStack getPackage() { return this.entityData.get(PACKAGE); }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Package", this.getPackage().saveOptional(this.level().registryAccess()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setPackage(ItemStack.parseOptional(this.level().registryAccess(), tag.getCompound("Package")));
    }

    public boolean isPickable() {
        return true;
    }

    public boolean skipAttackInteraction(Entity entity) {
        if (entity instanceof Player player) {
            return this.hurt(this.damageSources().playerAttack(player), 0.0F);
        } else {
            return false;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Level world = level();
            if (!this.isRemoved() && !world.isClientSide) {
                ItemStack box = this.getPackage();
                if (!box.isEmpty()) {
                    Vec3 pos = this.position();
                    PackageEntity packageEntity = new PackageEntity(world, pos.x, pos.y, pos.z);
                    packageEntity.setBox(box.copy());
                    world.addFreshEntity(packageEntity);
                }

                this.playSound(SoundEvents.ITEM_FRAME_BREAK, 1.0f, 1.0f);

                this.kill();
                this.markHurt();
            }

            return true;
        }
    }

    @Override
    public BlockState getRenderedBlockState() {
        return CBMBlocks.MAIL_SHOT.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
    }

    @Nonnull
    @Override
    public EntityDamagePropertiesComponent getDamageProperties() {
        return this.getAllProperties().damage();
    }

    @Nonnull
    @Override
    protected BigCannonProjectilePropertiesComponent getBigCannonProjectileProperties() {
        return this.getAllProperties().bigCannonProperties();
    }

    @Nonnull
    @Override
    protected BallisticPropertiesComponent getBallisticProperties() {
        return this.getAllProperties().ballistics();
    }

    protected InertBigCannonProjectileProperties getAllProperties() {
        return CBCMunitionPropertiesHandlers.INERT_BIG_CANNON_PROJECTILE.getPropertiesOf(this);
    }
}