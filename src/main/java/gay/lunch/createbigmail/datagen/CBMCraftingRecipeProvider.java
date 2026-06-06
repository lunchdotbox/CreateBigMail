package gay.lunch.createbigmail.datagen;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.tterrag.registrate.providers.ProviderType;
import gay.lunch.createbigmail.CreateBigMail;
import gay.lunch.createbigmail.index.CBMBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public abstract class CBMCraftingRecipeProvider extends RecipeProvider {
    private CBMCraftingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void register() {
        CreateBigMail.REGISTRATE.addDataGenerator(ProviderType.RECIPE, CBMCraftingRecipeProvider::buildCraftingRecipes);
    }

    public static void buildCraftingRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBMBlocks.MAIL_SHOT.get())
                .define('I', AllItems.ANDESITE_ALLOY).define('i', CommonMetal.IRON.nuggets).define('S', ItemTags.WOODEN_SLABS)
                .pattern("iIi")
                .pattern("I I")
                .pattern(" S ")
                .unlockedBy("has_andesite_alloy", has(AllItems.ANDESITE_ALLOY))
                    .save(recipeOutput);
    }
}
