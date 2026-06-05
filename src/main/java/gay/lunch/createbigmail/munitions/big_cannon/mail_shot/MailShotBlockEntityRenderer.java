package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class MailShotBlockEntityRenderer extends SafeBlockEntityRenderer<MailShotBlockEntity> {
    public MailShotBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    protected void renderSafe(MailShotBlockEntity blockEntity, float partialTicks, PoseStack posestack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
        MailShotBlockCommonRenderer.render(blockEntity.getPackage(), blockEntity.getBlockState(), posestack, buffers, true, packedLight);
    }
}
