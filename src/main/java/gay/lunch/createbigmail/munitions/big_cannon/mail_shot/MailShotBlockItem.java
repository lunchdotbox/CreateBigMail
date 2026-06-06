package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.simibubi.create.content.equipment.zapper.terrainzapper.WorldshaperItemRenderer;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import gay.lunch.createbigmail.base.CBMTooltip;
import gay.lunch.createbigmail.index.CBMDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlockItem;

public class MailShotBlockItem extends ProjectileBlockItem {
    public MailShotBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new MailShotBlockItemRenderer()));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);
        ItemContainerContents items = stack.getOrDefault(CBMDataComponents.PACKAGE, ItemContainerContents.EMPTY);
        ItemStack box = items.copyOne();
        if (!box.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.createbigmail.package"));

            List<Component> subTooltip = new ArrayList<>();
            box.getItem().appendHoverText(box, ctx, subTooltip, flag);
            subTooltip.replaceAll(sibling -> Component.literal("  ").append(sibling));
            tooltip.addAll(subTooltip);
        }
    }
}