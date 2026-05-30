package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.box.PackageStyles;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.Map;

public class MailShotAssetGenerator {
    public static final Logger LOGGER = LogUtils.getLogger();

    private static void generateTopTexture(MailShotAssetFinder.PackageFace face, String output) {
        MailShotAssetPlanner.planTopTexture(face.texture(), face.start_x(), face.start_y(), face.size_x(), face.size_y(), output);
    }

    private static void generateSideTexture(MailShotAssetFinder.PackageFace face, String output) {
        MailShotAssetPlanner.planSideTexture(face.texture(), face.start_x(), face.start_y(), face.size_x(), face.size_y(), output);
    }

    public static void generateStyle(ResourceLocation model, String output) {
        Map<Direction, MailShotAssetFinder.PackageFace> faces = MailShotAssetFinder.getMailShotFaces(model);

        generateTopTexture(faces.getOrDefault(Direction.UP, new MailShotAssetFinder.PackageFace()), output + "_top");

        generateSideTexture(faces.getOrDefault(Direction.NORTH, new MailShotAssetFinder.PackageFace()), output + "_front");
        generateSideTexture(faces.getOrDefault(Direction.EAST, new MailShotAssetFinder.PackageFace()), output + "_right");
        generateSideTexture(faces.getOrDefault(Direction.SOUTH, new MailShotAssetFinder.PackageFace()), output + "_back");
        generateSideTexture(faces.getOrDefault(Direction.WEST, new MailShotAssetFinder.PackageFace()), output + "_left");
    }

    public static void generateAllStyles(String output) {
        for (PackageItem box : PackageStyles.ALL_BOXES) {
            ResourceLocation item_id = BuiltInRegistries.ITEM.getKey(box);
            generateStyle(item_id, output + "_" + box.style.type());
            LOGGER.info("Generated textures for mail shot using package style: " + item_id);
        }
    }
}
