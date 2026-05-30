package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import com.mojang.logging.LogUtils;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags.AllItemTags;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.box.PackageStyles;
import com.simibubi.create.content.logistics.box.PackageStyles.PackageStyle;
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
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MailShotAssetFinder {
    private static final ModelBakery MODEL_BAKERY = Minecraft.getInstance().getModelManager().getModelBakery();

    public record PackageFace (ResourceLocation texture, int start_x, int start_y, int size_x, int size_y) {
        public PackageFace() {
            this(MissingTextureAtlasSprite.getLocation(), 0, 0, 16, 16);
        }
    }

    public static Map<Direction, PackageFace> getMailShotFaces(ResourceLocation path) {
        UnbakedModel unbaked_model = MODEL_BAKERY.getModel(path.withPath("item/" + path.getPath()));

        Map<Direction, PackageFace> faces = new HashMap<>();

        if (unbaked_model instanceof BlockModel model) {
            BlockElement element = model.getElements().getFirst();

            for (Map.Entry<Direction, BlockElementFace> face : element.faces.entrySet()) {
                Material material = model.getMaterial(face.getValue().texture());
                SpriteContents contents = material.sprite().contents();
                ResourceLocation texture = material.texture();
                BlockFaceUV uv = face.getValue().uv();
                float size_u = Math.abs(uv.getU(1) - uv.getU(0));
                float size_v = Math.abs(uv.getV(1) - uv.getV(0));
                int start_x = (int) (uv.getU(0) * contents.width());
                int start_y = (int) (uv.getV(0) * contents.height());
                int size_x = (int) (size_u * contents.width());
                int size_y = (int) (size_v * contents.height());
                faces.put(face.getKey(), new PackageFace(texture, start_x, start_y, size_x, size_y));
            }
        }

        return faces;
    }
}
