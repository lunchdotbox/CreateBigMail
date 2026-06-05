package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileRenderer;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class MailShotProjectileRenderer extends BigCannonProjectileRenderer<MailShotProjectile> {
    public MailShotProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MailShotProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        Vec3 vel = entity.getOrientation();
        if (vel.lengthSqr() < 1e-4d)
            vel = new Vec3(0, -1, 0);

        poseStack.pushPose();
        if (vel.horizontalDistanceSqr() > 1e-4d && Math.abs(vel.y) > 1e-2d) {
            Vec3 horizontal = new Vec3(vel.x, 0, vel.z).normalize();
            poseStack.mulPose(CBCUtils.mat4x4fFacing(vel.normalize().reverse(), horizontal));
            poseStack.mulPose(CBCUtils.mat4x4fFacing(horizontal));
        } else {
            poseStack.mulPose(CBCUtils.mat4x4fFacing(vel.normalize()));
        }

        MailShotBlockCommonRenderer.render(entity.getPackage(), entity.getRenderedBlockState(), poseStack, buffers, false, entity.hasTracer() ? LightTexture.FULL_BRIGHT : packedLight);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
    }
}
