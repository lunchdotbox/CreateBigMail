package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import java.util.List;

import gay.lunch.createbigmail.index.CBMDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlockItem;

public class MailShotBlockItem extends ProjectileBlockItem {
    public MailShotBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);
        ItemContainerContents items = stack.getOrDefault(CBMDataComponents.PACKAGE, ItemContainerContents.EMPTY);
        ItemStack box = items.copyOne();
        if (!box.isEmpty())
            tooltip.add(Component.translatable("tooltip.createbigmail.package"));
    }
}