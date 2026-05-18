package gay.lunch.createbigmail;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import gay.lunch.createbigmail.index.CBMBlockEntities;
import gay.lunch.createbigmail.index.CBMBlocks;
import gay.lunch.createbigmail.index.CBMEntityTypes;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import rbasamoyai.createbigcannons.utils.CBCUtils;

public class CreateBigMail {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "createbigmail";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public static void init() {
        ModGroup.register();
        ModGroup.setDefaultTabToNull();

        CBMBlocks.register();
        CBMBlockEntities.register();
        CBMEntityTypes.register();
    }

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public static ResourceLocation resource(String path) {
        return CBCUtils.location(MOD_ID, path);
    }
}
