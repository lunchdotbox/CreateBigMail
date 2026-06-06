package gay.lunch.createbigmail.datagen.assets;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import gay.lunch.createbigmail.CreateBigMail;
import gay.lunch.createbigmail.index.CBMBlocks;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.base.CBCTooltip;

import static gay.lunch.createbigmail.CreateBigMail.REGISTRATE;

public class CBMLangGen {
    public static void prepare() {
        tooltip(CBMBlocks.MAIL_SHOT)
                .header("MAIL SHOT")
                .summary("Can hold _Create Packages._ _Zero penetrating force._ _Cannot be fuzed and detonated._ Useful for _sending items long distances._");

        REGISTRATE.addLang("tooltip", CreateBigMail.resource("package"), "+ Package");

        REGISTRATE.addLang("key", CreateBigMail.resource("category"), "Create Big Mail");
    }

    private static class TooltipBuilder {
        private final ResourceLocation loc;
        private final String type;
        private int cbCount = 1;
        private int caCount = 1;

        public TooltipBuilder(ItemProviderEntry<?, ?> provider, boolean item) {
            this.loc = provider.getId();
            this.type = item ? "item" : "block";
        }

        public TooltipBuilder header(String enUS) {
            REGISTRATE.addLang(this.type, this.loc, "tooltip", enUS);
            return this;
        }

        public TooltipBuilder summary(String enUS) {
            REGISTRATE.addLang(this.type, this.loc, "tooltip.summary", enUS);
            return this;
        }

        public TooltipBuilder conditionAndBehavior(String enUSCondition, String enUSBehaviour) {
            REGISTRATE.addLang(this.type, this.loc, String.format("tooltip.condition%d", this.cbCount), enUSCondition);
            REGISTRATE.addLang(this.type, this.loc, String.format("tooltip.behaviour%d", this.cbCount), enUSBehaviour);
            this.cbCount++;
            return this;
        }

        public TooltipBuilder controlAndAction(String enUSControl, String enUSAction) {
            REGISTRATE.addLang(this.type, this.loc, String.format("tooltip.control%d", this.caCount), enUSControl);
            REGISTRATE.addLang(this.type, this.loc, String.format("tooltip.action%d", this.caCount), enUSAction);
            this.caCount++;
            return this;
        }
    }

    private static TooltipBuilder tooltip(BlockEntry<?> provider) {
        return new TooltipBuilder(provider, false);
    }

    private static TooltipBuilder tooltip(ItemEntry<?> provider) {
        return new TooltipBuilder(provider, true);
    }
}
