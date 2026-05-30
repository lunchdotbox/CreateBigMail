package gay.lunch.createbigmail;

import com.mojang.logging.LogUtils;
import gay.lunch.createbigmail.index.CBMDataComponents;
import gay.lunch.createbigmail.munitions.big_cannon.mail_shot.MailShotAssetFinder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

import java.util.List;

@Mod(CreateBigMail.MOD_ID)
public class CreateBigMailNeoForge {
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateBigMailNeoForge(IEventBus modEventBus) {
        ModGroup.registerNeoForge(modEventBus);

        CreateBigMail.REGISTRATE.registerEventListeners(modEventBus);
        CreateBigMail.init();

        modEventBus.addListener(this::onRegister);
    }

    private void onRegister(RegisterEvent evt) {
        CBMDataComponents.init();
    }
}
