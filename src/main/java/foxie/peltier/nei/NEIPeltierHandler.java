package foxie.peltier.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import foxie.peltier.PeltierRecipes;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class NEIPeltierHandler extends TemplateRecipeHandler {
   @Override
   public String getGuiTexture() {
      return null;
   }

   @Override
   public String getRecipeName() {
      return "peltier";
   }

   @Override
   public void loadCraftingRecipes(String outputId, Object... results) {
      if (outputId.equals("item") && results.length >= 1 && results[0] instanceof ItemStack && ((ItemStack) results[0]).getItem() instanceof ItemBlock) {
         for (PeltierRecipes.PeltierRecipe recipe : PeltierRecipes.recipes) {
            ItemBlock ib = ((ItemBlock) (((ItemStack) results[0]).getItem()));
            if (recipe.coldSideIn == ib.field_150939_a || recipe.hotSideIn == ib.field_150939_a)
               arecipes.add(new NEIPeltierRecipe(recipe));
         }
      }


      super.loadCraftingRecipes(outputId, results);
   }

   @Override
   public void loadUsageRecipes(String inputId, Object... ingredients) {
      if (inputId.equals("peltier"))
         System.out.println("usage");
      // TODO things

      super.loadUsageRecipes(inputId, ingredients);
   }



   public class NEIPeltierRecipe extends CachedRecipe {
      public PeltierRecipes.PeltierRecipe recipe;

      public NEIPeltierRecipe(PeltierRecipes.PeltierRecipe recipe) {
         this.recipe = recipe;
      }

      @Override
      public PositionedStack getResult() {
         return null;
      }
   }
}
