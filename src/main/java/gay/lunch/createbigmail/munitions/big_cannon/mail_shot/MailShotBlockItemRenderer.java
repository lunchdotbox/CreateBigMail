package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import gay.lunch.createbigmail.CreateBigMail;
import gay.lunch.createbigmail.index.CBMBlocks;
import gay.lunch.createbigmail.index.CBMDataComponents;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public class MailShotBlockItemRenderer extends CustomRenderedItemModelRenderer {
    private static final PartialModel BLOCK = PartialModel.of(CreateBigMail.resource("block/mail_shot"));

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack posestack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
        ItemContainerContents items = stack.getOrDefault(CBMDataComponents.PACKAGE, ItemContainerContents.EMPTY);
        MailShotBlockCommonRenderer.render(items.copyOne(), CBMBlocks.MAIL_SHOT.getDefaultState(), posestack, buffers, false, packedLight);
        renderer.render(BLOCK.get(), packedLight);
    }
}
