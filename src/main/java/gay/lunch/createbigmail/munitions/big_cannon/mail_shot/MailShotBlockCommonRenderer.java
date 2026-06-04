package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

public class MailShotBlockCommonRenderer {
    public static void render(ItemStack box, BlockState state, PoseStack posestack, MultiBufferSource buffers, int packedLight) {
        Direction facing = state.getValue(BlockStateProperties.FACING);

        if (!box.isEmpty()) {
            Map<Direction, MailShotAssetFinder.PackageFace> faces = MailShotAssetFinder.face_maps.getOrDefault(BuiltInRegistries.ITEM.getKey(box.getItem()), new HashMap<>());

            renderFace(faces, Direction.NORTH, facing, posestack, buffers, packedLight);
            renderFace(faces, Direction.EAST, facing, posestack, buffers, packedLight);
            renderFace(faces, Direction.SOUTH, facing, posestack, buffers, packedLight);
            renderFace(faces, Direction.WEST, facing, posestack, buffers, packedLight);

            renderFace(faces, Direction.UP, facing, posestack, buffers, packedLight);
        }
    }

    private static void renderFace(Map<Direction, MailShotAssetFinder.PackageFace> faces, Direction direction, Direction facing, PoseStack posestack, MultiBufferSource buffers, int packedLight) {
        renderFace(faces.getOrDefault(direction, new MailShotAssetFinder.PackageFace()), direction, facing, posestack, buffers, packedLight);
    }

    private static void renderFace(MailShotAssetFinder.PackageFace face, Direction direction, Direction facing, PoseStack posestack, MultiBufferSource buffers, int packedLight) {
        RenderType renderType = RenderType.entityCutoutNoCull(face.texture());

        posestack.pushPose();
        posestack.mulPose(direction.getRotation());

        PoseStack.Pose lastPose = posestack.last();
        Matrix4f pose = lastPose.pose();
        VertexConsumer builder = buffers.getBuffer(renderType);

        vertex(builder, pose, packedLight, -0.25f, -0.25f, face.uvs()[0].x / 4, face.uvs()[0].y / 4);
        vertex(builder, pose, packedLight, -0.25f, 0.25f, face.uvs()[1].x / 4, face.uvs()[1].y / 4);
        vertex(builder, pose, packedLight, 0.25f, 0.25f, face.uvs()[2].x / 4, face.uvs()[2].y / 4);
        vertex(builder, pose, packedLight, 0.25f, -0.25f, face.uvs()[3].x / 4, face.uvs()[3].y / 4);

        posestack.popPose();
    }

    private static void vertex(VertexConsumer builder, Matrix4f pose, int packedLight, float x, float y, float u, float v) {
        builder.addVertex(pose, x, y, 5.0f / 16.0f)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(0.0f, 1.0f, 0.0f);
    }
}
