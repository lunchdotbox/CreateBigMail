package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import gay.lunch.createbigmail.CreateBigMail;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Direction;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

public class MailShotBlockEntityRenderer extends SafeBlockEntityRenderer<MailShotBlockEntity> {
    public MailShotBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    protected void renderSafe(MailShotBlockEntity blockEntity, float partialTicks, PoseStack posestack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
        MailShotBlockCommonRenderer.render(blockEntity.getPackage(), blockEntity.getBlockState(), posestack, buffers, packedLight);
    }
}
