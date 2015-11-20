package foxie.peltier.blocks;

import foxie.lib.FoxyBlocks;
import foxie.lib.Registrator;

public class BlockReg {
   public static void preinit() {
      FoxyBlocks.add(new BlockPeltier());
      Registrator.registerTileEntity(TileEntityPeltier.class, "peltier_te");
   }

   public static void init() {

   }

   public static void postinit() {
   }
}
