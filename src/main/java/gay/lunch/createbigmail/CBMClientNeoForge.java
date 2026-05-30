package gay.lunch.createbigmail;

import com.simibubi.create.AllItems;
import gay.lunch.createbigmail.munitions.big_cannon.mail_shot.MailShotAssetFinder;
import gay.lunch.createbigmail.munitions.big_cannon.mail_shot.MailShotAssetGenerator;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(value = CreateBigMail.MOD_ID, dist = Dist.CLIENT)
public class CBMClientNeoForge {
    public static final Logger LOGGER = LogManager.getLogger();

    public CBMClientNeoForge(IEventBus modEventBus) {
        modEventBus.addListener(CBMClientNeoForge::onClientSetup);
        modEventBus.addListener(CBMClientNeoForge::onRegisterAdditional);
        NeoForge.EVENT_BUS.addListener(CBMClientNeoForge::onPlayerJoin);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        CBMClientCommon.onClientSetup();
    }

    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {

    }

    public static void onPlayerJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        MailShotAssetGenerator.generateAllStyles("assets/textures/block/projectile/mail_shot");

    }
}
