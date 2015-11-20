package foxie.peltier;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.ArrayList;
import java.util.List;

public class PeltierRecipes {
   public static List<PeltierRecipe> recipes = new ArrayList<PeltierRecipe>();

   public static void addRecipe(Block coldSideIn, Block hotSideIn, Block coldSideOut, Block hotSideOut, int power) {
      recipes.add(new PeltierRecipe(coldSideIn, hotSideIn, coldSideOut, hotSideOut, power));
   }

   public static void addRecipe(Block coldSideIn, int coldMetaIn, Block hotSideIn, int hotMetaIn,
                                Block coldSideOut, int coldMetaOut, Block hotSideOut, int hotMetaOut, int power) {
      recipes.add(new PeltierRecipe(coldSideIn, coldMetaIn, hotSideIn, hotMetaIn,
              coldSideOut, coldMetaOut, hotSideOut, hotMetaOut, power));
   }

   public static void initBaseRecipes() {
      addRecipe(Blocks.ice, Blocks.cobblestone, Blocks.water, Blocks.lava, 100);
      addRecipe(Blocks.packed_ice, Blocks.cobblestone, Blocks.water, Blocks.lava, 100);
   }

   public static PeltierRecipe findRecipe(Block coldSide, int coldMeta, Block hotSide, int hotMeta) {
      for (PeltierRecipe recipe : recipes)
         if (recipe.matches(coldSide, coldMeta, hotSide, hotMeta))
            return recipe;

      return null;
   }

   public static class PeltierRecipe {
      public Block coldSideIn;
      public Block hotSideIn;

      public Block coldSideOut;
      public Block hotSideOut;

      public int coldMetaIn = -1;
      public int hotMetaIn  = -1;

      public int coldMetaOut = 0;
      public int hotMetaOut  = 0;

      public int heat;

      public PeltierRecipe(Block coldSideIn, Block hotSideIn, Block coldSideOut, Block hotSideOut, int heat) {
         this.coldSideIn = coldSideIn;
         this.hotSideIn = hotSideIn;

         this.coldSideOut = coldSideOut;
         this.hotSideOut = hotSideOut;

         this.heat = heat;
      }

      public PeltierRecipe(Block coldSideIn, int coldMetaIn, Block hotSideIn, int hotMetaIn,
                           Block coldSideOut, int coldMetaOut, Block hotSideOut, int hotMetaOut, int heat) {
         this(coldSideIn, hotSideIn, coldSideOut, hotSideOut, heat);

         this.coldMetaIn = coldMetaIn;
         this.hotMetaIn = hotMetaIn;
         this.coldMetaOut = coldMetaOut;
         this.hotMetaOut = hotMetaOut;
      }

      public boolean matches(Block coldSide, int coldMeta, Block hotSide, int hotMeta) {
         return coldSide == coldSideIn && (coldMeta == coldMetaIn || coldMetaIn == -1) &&
                 hotSide == hotSideIn && (hotMeta == hotMetaIn || hotMetaIn == -1);
      }
   }
}
