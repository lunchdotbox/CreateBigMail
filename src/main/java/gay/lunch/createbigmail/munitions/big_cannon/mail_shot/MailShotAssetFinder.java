package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import com.mojang.logging.LogUtils;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags.AllItemTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.box.PackageStyles;
import com.simibubi.create.content.logistics.box.PackageStyles.PackageStyle;
import gay.lunch.createbigmail.CreateBigMail;
import net.createmod.catnip.math.BlockFace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MailShotAssetFinder {
    private static final ModelBakery MODEL_BAKERY = Minecraft.getInstance().getModelManager().getModelBakery();
    public static final Map<ResourceLocation, Map<Direction, PackageFace>> face_maps = new HashMap<>();

    public static void init() {
        for (PackageItem box : PackageStyles.ALL_BOXES) {
            ResourceLocation item_id = BuiltInRegistries.ITEM.getKey(box);
            face_maps.put(item_id, getMailShotFaces(item_id));
        }

        CreateBigMail.LOGGER.info("faces are {}", face_maps);
    }

    private static Map<Direction, PackageFace> getMailShotFaces(ResourceLocation path) {
        UnbakedModel unbaked_model = MODEL_BAKERY.getModel(path.withPath("item/" + path.getPath()));

        Map<Direction, PackageFace> faces = new HashMap<>();

        if (unbaked_model instanceof BlockModel model) {
            BlockElement element = model.getElements().getFirst();

            for (Map.Entry<Direction, BlockElementFace> face : element.faces.entrySet()) {
                Material material = model.getMaterial(face.getValue().texture());
                SpriteContents contents = material.sprite().contents();
                ResourceLocation texture = material.texture();
                texture = texture.withPath("textures/" + texture.getPath() + ".png");
                BlockFaceUV uv = face.getValue().uv();

                float width = contents.width();
                float height = contents.height();

                float from_x = uv.getU(0) / 16.0f;
                float from_y = uv.getV(0) / 16.0f;
                float to_x = uv.getU(2) / 16.0f;
                float to_y = uv.getV(2) / 16.0f;
                if (Mth.abs(to_x - from_x) * width < 8 || Mth.abs(to_y - from_y) * height < 8) continue;
                boolean r_x = from_x > to_x;
                boolean r_y = from_y > to_y;
                float move_x = ((Mth.abs(to_x * width - from_x * width) - 8) / width) / 2;
                float move_y = ((Mth.abs(to_y * height - from_y * height) - 8) / height) / 2;
                from_x += r_x ? -move_x : move_x;
                from_y += r_y ? -move_y : move_y;
                to_x -= r_x ? -move_x : move_x;
                to_y -= r_y ? -move_y : move_y;

                faces.put(face.getKey(), new PackageFace(texture, new Vec2[]{new Vec2(from_x, to_y), new Vec2(from_x, from_y), new Vec2(to_x, from_y), new Vec2(to_x, to_y)}));
            }
        }

        return faces;
    }

    public record PackageFace (ResourceLocation texture, Vec2[] uvs) {
        public PackageFace() {
            this(MissingTextureAtlasSprite.getLocation(), new Vec2[]{new Vec2(0, 4), new Vec2(0, 0), new Vec2(4, 0), new Vec2(4, 4)});
        }
    }
}
