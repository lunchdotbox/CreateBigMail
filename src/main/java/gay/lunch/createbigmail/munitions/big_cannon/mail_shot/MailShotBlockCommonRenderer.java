package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class MailShotBlockCommonRenderer {
    public static void render(ItemStack box, BlockState state, PoseStack posestack, MultiBufferSource buffers, boolean offset, int packedLight) {
        Direction facing = state.getValue(BlockStateProperties.FACING);

        if (!box.isEmpty()) {
            Map<Direction, MailShotAssetFinder.PackageFace> faces = MailShotAssetFinder.face_maps.getOrDefault(BuiltInRegistries.ITEM.getKey(box.getItem()), new HashMap<>());

            renderFace(faces, Direction.NORTH, facing, posestack, buffers, offset, packedLight);
            renderFace(faces, Direction.EAST, facing, posestack, buffers, offset, packedLight);
            renderFace(faces, Direction.SOUTH, facing, posestack, buffers, offset, packedLight);
            renderFace(faces, Direction.WEST, facing, posestack, buffers, offset, packedLight);

            renderFace(faces, Direction.UP, facing, posestack, buffers, offset, packedLight);
        }
    }

    private static void renderFace(Map<Direction, MailShotAssetFinder.PackageFace> faces, Direction direction, Direction facing, PoseStack posestack, MultiBufferSource buffers, boolean offset, int packedLight) {
        renderFace(faces.getOrDefault(direction, new MailShotAssetFinder.PackageFace()), direction, facing, posestack, buffers, offset, packedLight);
    }

    private static void renderFace(MailShotAssetFinder.PackageFace face, Direction direction, Direction facing, PoseStack posestack, MultiBufferSource buffers, boolean offset, int packedLight) {
        RenderType renderType = RenderType.entitySolid(face.texture());

        posestack.pushPose();
        if (offset) posestack.translate(0.5, 0.5, 0.5);
        posestack.mulPose(facing.getRotation());
        posestack.translate(0.0, 3.0 / 16.0, 0.0);

        PoseStack.Pose lastPose = posestack.last();
        VertexConsumer builder = buffers.getBuffer(renderType);

        Quaternionf rotation = direction.getRotation().normalize();
        Vec3i normal = direction.getNormal();

        vertex(builder, lastPose, packedLight, rotation, normal, 0.251f, 0.251f, face.uvs()[3].x / 4, face.uvs()[3].y / 4);
        vertex(builder, lastPose, packedLight, rotation, normal, 0.251f, -0.251f, face.uvs()[2].x / 4, face.uvs()[2].y / 4);
        vertex(builder, lastPose, packedLight, rotation, normal, -0.251f, -0.251f, face.uvs()[1].x / 4, face.uvs()[1].y / 4);
        vertex(builder, lastPose, packedLight, rotation, normal, -0.251f, 0.251f, face.uvs()[0].x / 4, face.uvs()[0].y / 4);
        posestack.popPose();
    }

    private static void vertex(VertexConsumer builder, PoseStack.Pose pose, int packedLight, Quaternionf rotation, Vec3i normal, float x, float y, float u, float v) {
        Vector3f vector = rotation.transform(new Vector3f(x, 5.0f / 16.0f, y));
        builder.addVertex(pose.pose(), vector.x, vector.y, vector.z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, normal.getX(), normal.getY(), normal.getZ());
    }
}
