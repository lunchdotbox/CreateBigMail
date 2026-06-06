package gay.lunch.createbigmail;

import gay.lunch.createbigmail.datagen.CBMCraftingRecipeProvider;
import gay.lunch.createbigmail.datagen.assets.CBMLangGen;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = CreateBigMail.MOD_ID)
public class CBMDataNeoForge {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onGatherRegistrateData(GatherDataEvent evt) {
        if (!evt.getMods().contains(CreateBigMail.MOD_ID))
            return;

        CBMLangGen.prepare();
        CBMCraftingRecipeProvider.register();
    }
}
