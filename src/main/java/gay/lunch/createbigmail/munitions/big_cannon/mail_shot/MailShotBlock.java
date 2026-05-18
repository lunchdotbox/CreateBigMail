package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import com.mojang.serialization.MapCodec;

import com.simibubi.create.AllTags.AllItemTags;
import com.simibubi.create.foundation.block.IBE;
import gay.lunch.createbigmail.index.CBMBlockEntities;
import gay.lunch.createbigmail.index.CBMDataComponents;
import gay.lunch.createbigmail.index.CBMEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

import java.util.List;

public class MailShotBlock extends ProjectileBlock<MailShotProjectile> implements IBE<MailShotBlockEntity> {
    private static final MapCodec<ProjectileBlock> CODEC = simpleCodec(MailShotBlock::new);

    public MailShotBlock(Properties properties) {
        super(properties);
    }

    public static ItemStack getPackageFromItemStack(ItemStack stack) {
        ItemContainerContents items = stack.getOrDefault(CBMDataComponents.PACKAGE, ItemContainerContents.EMPTY);
        return items.copyOne();
    }

    public static ItemStack getPackageFromBlocks(List<StructureBlockInfo> blocks, HolderLookup.Provider registries) {
        if (blocks.isEmpty())
            return ItemStack.EMPTY;
        StructureBlockInfo info = blocks.get(0);
        if (info.nbt() == null)
            return ItemStack.EMPTY;
        Tag tag = info.nbt().getCompound("components").get("createbigmail:package");
        ItemContainerContents contents = ItemContainerContents.CODEC
                .parse(registries.createSerializationContext(NbtOps.INSTANCE), tag)
                .resultOrPartial()
                .orElse(ItemContainerContents.EMPTY);
        return contents.getSlots() > 0 ? contents.getStackInSlot(0) : ItemStack.EMPTY;
    }

    public static ItemStack getPackageFromBlock(Level level, BlockPos pos, BlockState state) {
        return level.getBlockEntity(pos) instanceof MailShotBlockEntity projectile ? projectile.getPackage() : ItemStack.EMPTY;
    }

    @Override
    public MailShotProjectile getProjectile(Level level, ItemStack itemStack) {
        MailShotProjectile projectile = this.getAssociatedEntityType().create(level);
        projectile.setTracer(getTracerFromItemStack(itemStack));
        projectile.setPackage(getPackageFromItemStack(itemStack));
        return projectile;
    }

    @Override
    public MailShotProjectile getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
        MailShotProjectile projectile = this.getAssociatedEntityType().create(level);
        projectile.setTracer(getTracerFromBlocks(projectileBlocks, level.registryAccess()));
        projectile.setPackage(getPackageFromBlocks(projectileBlocks, level.registryAccess()));
        return projectile;
    }

    @Override
    public MailShotProjectile getProjectile(Level level, BlockPos pos, BlockState state) {
        MailShotProjectile projectile = this.getAssociatedEntityType().create(level);
        projectile.setTracer(getTracerFromBlock(level, pos, state));
        projectile.setPackage(getPackageFromBlock(level, pos, state));
        return projectile;
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand == InteractionHand.OFF_HAND)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION; //todo: check behavior
        MailShotBlockEntity projectileBlock = this.getBlockEntity(level, pos);
        if (projectileBlock == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        int slot = -1;
        if (stack.is(AllItemTags.PACKAGES.tag)) {
            slot = 0;
            if (!projectileBlock.getPackage().isEmpty()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        } else if (CBCItems.TRACER_TIP.isIn(stack)) {
            slot = 1;
            if (!projectileBlock.getTracer().isEmpty()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (slot == -1) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!level.isClientSide) {
            ItemStack copy = player.getAbilities().instabuild ? stack.copy() : stack.split(1);
            copy.setCount(1);
            projectileBlock.setItem(slot, copy);
            projectileBlock.notifyUpdate();
        }
        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        MailShotBlockEntity projectileBlock = this.getBlockEntity(level, pos);
        if (projectileBlock == null)
            return InteractionResult.PASS;
        if (projectileBlock.getItem(0).isEmpty())
            return InteractionResult.PASS;
        if (!level.isClientSide) {
            ItemStack resultStack = projectileBlock.removeItem(0, 1);
            if (!player.addItem(resultStack) && !player.isCreative()) {
                ItemEntity item = player.drop(resultStack, false);
                if (item != null) {
                    item.setNoPickUpDelay();
                    item.setTarget(player.getUUID());
                }
            }
            projectileBlock.notifyUpdate();
        }
        level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override public Class<MailShotBlockEntity> getBlockEntityClass() { return MailShotBlockEntity.class; }
    @Override public BlockEntityType<? extends MailShotBlockEntity> getBlockEntityType() { return CBMBlockEntities.MAIL_SHOT_BLOCK.get(); }
    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public EntityType<? extends MailShotProjectile> getAssociatedEntityType() {
        return CBMEntityTypes.MAIL_SHOT.get();
    }
}