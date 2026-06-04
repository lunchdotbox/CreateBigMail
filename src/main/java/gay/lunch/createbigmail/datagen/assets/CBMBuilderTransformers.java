package gay.lunch.createbigmail.datagen.assets;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import gay.lunch.createbigmail.CreateBigMail;
import gay.lunch.createbigmail.index.CBMDataComponents;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;

public class CBMBuilderTransformers {
    public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> packageProjectileLoot() {
        return packageProjectileLoot(t -> t);
    }

    public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> packageProjectileLoot(
            NonNullFunction<CopyComponentsFunction.Builder, CopyComponentsFunction.Builder> additionalCopyData) {
        return (t, u) -> t.add(u, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(u)
                                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                                .apply(additionalCopyData.apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                        .include(CBMDataComponents.PACKAGE))))));
    }

    public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> projectile(String pathAndMaterial) {
        return projectile(pathAndMaterial, false);
    }

    public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> projectile(String pathAndMaterial, boolean useStandardModel) {
        ResourceLocation baseLoc = CreateBigMail.resource(String.format("block/%sprojectile_block", useStandardModel ? "standard_" : ""));
        ResourceLocation sideLoc = CreateBigMail.resource("block/" + pathAndMaterial);
        ResourceLocation topLoc = CreateBigMail.resource("block/" + pathAndMaterial + "_top");
        ResourceLocation bottomLoc = CreateBigMail.resource("block/" + pathAndMaterial + "_bottom");
        return b -> b.properties(p -> p.noOcclusion())
                .addLayer(() -> RenderType::solid)
                .blockstate((c, p) -> {
                    BlockModelBuilder builder = p.models().withExistingParent(c.getName(), baseLoc).renderType("minecraft:cutout")
                            .texture("side", sideLoc)
                            .texture("top", topLoc)
                            .texture("particle", topLoc);
                    if (!useStandardModel) builder.texture("bottom", bottomLoc);
                    p.directionalBlock(c.get(), builder);
                });
    }
}
