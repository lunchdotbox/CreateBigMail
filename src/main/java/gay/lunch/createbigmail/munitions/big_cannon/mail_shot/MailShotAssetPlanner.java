package gay.lunch.createbigmail.munitions.big_cannon.mail_shot;

import dev.lukebemish.dynamicassetgenerator.api.ResourceCache;
import dev.lukebemish.dynamicassetgenerator.api.client.AssetResourceCache;
import dev.lukebemish.dynamicassetgenerator.api.client.generators.TextureGenerator;
import dev.lukebemish.dynamicassetgenerator.api.client.generators.texsources.OverlaySource;
import dev.lukebemish.dynamicassetgenerator.api.client.generators.texsources.TextureReaderSource;
import gay.lunch.createbigmail.CreateBigMail;
import net.minecraft.resources.ResourceLocation;
import dev.lukebemish.dynamicassetgenerator.api.client.generators.texsources.CropSource;

import java.util.List;

public class MailShotAssetPlanner {
    private static final AssetResourceCache ASSET_CACHE = ResourceCache.register(new AssetResourceCache(CreateBigMail.resource("assets")));
    private static final ResourceLocation SIDE_OVERLAY = CreateBigMail.resource("assets/textures/block/mail_shot_overlay_side.png");
    private static final ResourceLocation TOP_OVERLAY = CreateBigMail.resource("assets/textures/block/mail_shot_overlay_top.png");

    public static void init() {}

    private static void planTexture(ResourceLocation base, int start_x, int start_y, int size_x, int size_y, ResourceLocation overlay, String output) {
        ASSET_CACHE.planSource(
                new TextureGenerator(
                        CreateBigMail.resource(output + ".png"),
                        new OverlaySource.Builder().setSources(List.of(
                                new TextureReaderSource.Builder().setPath(overlay).build(),
                                new CropSource.Builder()
                                        .setInput(new TextureReaderSource.Builder().setPath(base).build())
                                        .setStartX(start_x).setStartY(start_y).setSizeX(size_x).setSizeY(size_y)
                                        .build()
                        )).build()
                )
        );
    }

    public static void planSideTexture(ResourceLocation base, int start_x, int start_y, int size_x, int size_y, String output) {
        planTexture(base, start_x, start_y, size_x, size_y, SIDE_OVERLAY, output);
    }

    public static void planTopTexture(ResourceLocation base, int start_x, int start_y, int size_x, int size_y, String output) {
        planTexture(base, start_x, start_y, size_x, size_y, TOP_OVERLAY, output);
    }
}
