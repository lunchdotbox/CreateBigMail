package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import com.simibubi.create.AllTags.AllItemTags;
import com.simibubi.create.foundation.utility.CreateLang;
import gay.lunch.createbigmail.index.CBMDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileBlockEntity;

import java.util.List;

public class MailShotBlockEntity extends BigCannonProjectileBlockEntity {
    public MailShotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public CompoundTag writeClient(CompoundTag tag, HolderLookup.Provider registries) {
        super.writeClient(tag, registries);
        tag.put("Package", this.getPackage().saveOptional(registries));
        return tag;
    }

    @Override
    public void readClient(CompoundTag tag, HolderLookup.Provider registries) {
        super.readClient(tag, registries);
        this.setPackage(ItemStack.parseOptional(registries, tag.getCompound("Package")));
    }

    @Override
    public void writeSafe(CompoundTag tag, HolderLookup.Provider registries) {
        PatchedDataComponentMap restore = new PatchedDataComponentMap(this.components());
        PatchedDataComponentMap copy = new PatchedDataComponentMap(DataComponentMap.EMPTY);
        this.writeSafeComponents(copy);
        this.setComponents(copy);
        super.saveAdditional(tag, registries);
        this.setComponents(restore);
    }

    protected void writeSafeComponents(PatchedDataComponentMap safeComponents) {

    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return this.getPackage().isEmpty() && this.getTracer().isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot == 0) {
            if (this.getPackage().isEmpty()) return this.getTracer();
            else return this.getPackage();
        } else if (slot == 1) return this.getTracer();
        else return ItemStack.EMPTY;
    }

    public ItemStack getPackage() {
        ItemContainerContents contents = this.components().getOrDefault(CBMDataComponents.PACKAGE, ItemContainerContents.EMPTY);
        return contents.getSlots() > 0 ? contents.getStackInSlot(0) : ItemStack.EMPTY;
    }

    public void setPackage(ItemStack itemStack) {
        PatchedDataComponentMap components = new PatchedDataComponentMap(this.components());
        if (itemStack.isEmpty()) {
            components.remove(CBMDataComponents.PACKAGE);
        } else {
            components.set(CBMDataComponents.PACKAGE, ItemContainerContents.fromItems(List.of(itemStack)));
        }
        this.setComponents(components);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (this.isEmpty() || amount < 1)
            return ItemStack.EMPTY;
        if (slot == 0) {
            if (!this.getPackage().isEmpty()) {
                ItemStack originalCopy = this.getPackage();
                ItemStack result = originalCopy.split(amount);
                this.setPackage(originalCopy);
                return result;
            } else if (!this.getTracer().isEmpty()) {
                ItemStack originalCopy = this.getTracer();
                ItemStack result = originalCopy.split(amount);
                this.setTracer(originalCopy);
                return result;
            } else return ItemStack.EMPTY;
        } else if (slot == 1) {
            if (!this.getTracer().isEmpty()) {
                ItemStack originalCopy = this.getTracer();
                ItemStack result = originalCopy.split(amount);
                this.setTracer(originalCopy);
                return result;
            } else return ItemStack.EMPTY;
        } else return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (this.isEmpty())
            return ItemStack.EMPTY;
        if (slot == 0) {
            if (!this.getPackage().isEmpty()) {
                ItemStack result = this.getPackage();
                this.setPackage(ItemStack.EMPTY);
                return result;
            } else if (!this.getTracer().isEmpty()) {
                ItemStack result = this.getTracer();
                this.setTracer(ItemStack.EMPTY);
                return result;
            } else return ItemStack.EMPTY;
        } else if (slot == 1) {
            if (!this.getTracer().isEmpty()) {
                ItemStack result = this.getTracer();
                this.setTracer(ItemStack.EMPTY);
                return result;
            } else return ItemStack.EMPTY;
        } else return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == 0) this.setPackage(stack);
        else if (slot == 1) this.setTracer(stack);
        else return;
        if (stack.getCount() > this.getMaxStackSize())
            stack.setCount(this.getMaxStackSize());
        this.setChanged();
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return (index == 0 || index == 1) && (stack.is(AllItemTags.PACKAGES.tag) || CBCItems.TRACER_TIP.isIn(stack)) && (this.getPackage().isEmpty() || this.getTracer().isEmpty());
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        this.setPackage(ItemStack.EMPTY);
        this.setTracer(ItemStack.EMPTY);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!this.getPackage().isEmpty())
            CreateLang.builder("tooltip")
                    .translate("createbigmail.package")
                    .forGoggles(tooltip);
        if (!this.getTracer().isEmpty())
            CreateLang.builder("tooltip")
                    .translate("createbigcannons.tracer")
                    .forGoggles(tooltip);
        return true;
    }
}