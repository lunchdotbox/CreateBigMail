package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import javax.annotation.Nonnull;
import javax.swing.*;

import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.content.logistics.packagePort.postbox.PostboxBlock;
import com.simibubi.create.content.logistics.packagePort.postbox.PostboxBlockEntity;
import gay.lunch.createbigmail.CreateBigMail;
import gay.lunch.createbigmail.index.CBMBlocks;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
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

    @Override
    protected AABB makeBoundingBox() {
        AABB box = super.makeBoundingBox();
        return box.deflate(0.5);
    }

    @Override
    protected ImpactResult calculateBlockPenetration(ProjectileContext projectileContext, BlockState state, BlockHitResult blockHitResult) {
        if (state.getBlock() instanceof PostboxBlock block) {
            PostboxBlockEntity be = block.getBlockEntity(level(), blockHitResult.getBlockPos());
            if (be != null && be.acceptsPackages) {
                ItemStack box = this.getPackage();
                if (!box.isEmpty()) be.inventory.insertItem(0, box, false);
                remove(RemovalReason.DISCARDED);
                return new ImpactResult(ImpactResult.KinematicOutcome.STOP, false);
            }
        }

        ImpactResult result = super.calculateBlockPenetration(new ProjectileContext(this, CBCCfgMunitions.GriefState.NO_DAMAGE), state, blockHitResult);
        return new ImpactResult(result.kinematics() == ImpactResult.KinematicOutcome.PENETRATE ? ImpactResult.KinematicOutcome.STOP : result.kinematics(), false);
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
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (!pPlayer.getItemInHand(pHand).isEmpty())
            return super.interact(pPlayer, pHand);
        if (pPlayer.level().isClientSide)
            return InteractionResult.SUCCESS;

        pPlayer.setItemInHand(pHand, this.getPackage());
        this.playSound(SoundEvents.ITEM_FRAME_BREAK, 1.0f, 1.0f);

        remove(RemovalReason.DISCARDED);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void tick() {
        super.tick();
        inGroundTime = 0;
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