package gay.lunch.createbigmail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.simibubi.create.Create;

import gay.lunch.createbigmail.index.CBMBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModGroup {
    public static final ResourceKey<CreativeModeTab> MAIN_TAB_KEY = makeKey("base");

    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateBigMail.MOD_ID);
    private static final Map<ResourceKey<CreativeModeTab>, DeferredHolder<CreativeModeTab, CreativeModeTab>> TABS = new HashMap<>();

    public static final Supplier<CreativeModeTab> GROUP = wrapGroup("base", () -> createBuilder()
            .title(Component.translatable("itemGroup." + CreateBigMail.MOD_ID))
            .icon(CBMBlocks.MAIL_SHOT::asStack)
            .displayItems((param, output) -> {
                output.acceptAll(Arrays.asList(CBMBlocks.MAIL_SHOT.asStack()));
            })
            .build());

    public static Supplier<CreativeModeTab> wrapGroup(String id, Supplier<CreativeModeTab> sup) {
        DeferredHolder<CreativeModeTab, CreativeModeTab> obj = TAB_REGISTER.register(id, sup);
        TABS.put(ModGroup.makeKey(id), obj);
        return obj;
    }

    public static CreativeModeTab.Builder createBuilder() {
        return CreativeModeTab.builder().withTabsBefore(Create.asResource("palettes"));
    }

    public static void registerNeoForge(IEventBus modBus) {
        TAB_REGISTER.register(modBus);
    }

    public static void useModTab(ResourceKey<CreativeModeTab> key) {
        CreateBigMail.REGISTRATE.setCreativeTab(TABS.get(key));
    }

    public static void setDefaultTabToNull() {
        CreateBigMail.REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }

    public static ResourceKey<CreativeModeTab> makeKey(String id) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, CreateBigMail.resource(id));
    }

    public static void register() {
        CreateBigMail.REGISTRATE.addRawLang("itemGroup." + CreateBigMail.MOD_ID, "Create Big Mail");
    }
}
