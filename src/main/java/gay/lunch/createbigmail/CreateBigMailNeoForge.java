package gay.lunch.createbigmail;

import gay.lunch.createbigmail.index.CBMDataComponents;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(CreateBigMail.MOD_ID)
public class CreateBigMailNeoForge {
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
