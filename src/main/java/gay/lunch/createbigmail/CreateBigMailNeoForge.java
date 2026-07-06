package gay.lunch.createbigmail;

import com.mojang.logging.LogUtils;
import gay.lunch.createbigmail.index.CBMDataComponents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

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
