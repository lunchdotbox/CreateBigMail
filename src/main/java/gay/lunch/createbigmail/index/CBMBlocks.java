package gay.lunch.createbigmail.index;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import gay.lunch.createbigmail.ModGroup;
import gay.lunch.createbigmail.datagen.assets.CBMBuilderTransformers;
import gay.lunch.createbigmail.munitions.big_cannon.mail_shot.MailShotBlock;
import gay.lunch.createbigmail.munitions.big_cannon.mail_shot.MailShotBlockItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.datagen.assets.CBCBuilderTransformers;

import static gay.lunch.createbigmail.CreateBigMail.REGISTRATE;

public class CBMBlocks {
    static { ModGroup.useModTab(ModGroup.MAIN_TAB_KEY); }

    public static final BlockEntry<MailShotBlock> MAIL_SHOT = REGISTRATE
            .block("mail_shot", MailShotBlock::new)
            .properties(p -> p.strength(2.0f, 3.0f))
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .transform(axeOrPickaxe())
            .transform(CBMBuilderTransformers.mailShotProjectile("projectile/mail_shot"))
            .loot(CBCBuilderTransformers.tracerProjectileLoot())
            .loot(CBMBuilderTransformers.packageProjectileLoot())
            .item(MailShotBlockItem::new)
            .tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
            .build()
            .register();

    private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> axeOrPickaxe() {
        return b -> b.tag(BlockTags.MINEABLE_WITH_AXE)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    public static void register() {
    }
}
