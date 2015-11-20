package foxie.peltier;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import foxie.lib.Config;
import foxie.lib.IFoxieMod;
import foxie.peltier.blocks.BlockReg;
import foxie.peltier.items.ItemReg;
import foxie.peltier.proxy.ProxyCommon;

@Mod(modid = Peltier.MODID, name = Peltier.NAME, version = Peltier.VERSION, dependencies = "required-after:FoxieLib@[1.0,)")
public class Peltier implements IFoxieMod {
   public static final String MODID   = "peltier";
   public static final String NAME    = "Peltier";
   public static final String VERSION = "@VERSION@";

   @SidedProxy(clientSide = "foxie.peltier.proxy.ProxyClient", serverSide = "foxie.peltier.proxy.ProxyCommon")
   public static ProxyCommon proxy;

   @Mod.Instance(MODID)
   public static Peltier INSTANCE;

   public static Config config;

   public Peltier() {
   }

   @Mod.EventHandler
   public void preinit(FMLPreInitializationEvent event) {
      config = new Config(event.getSuggestedConfigurationFile().getAbsolutePath());
      proxy.preinit(event);
      ItemReg.preinit();
      BlockReg.preinit();
   }

   @Mod.EventHandler
   public void init(FMLInitializationEvent event) {
      proxy.init(event);
      ItemReg.init();
      BlockReg.init();

      PeltierRecipes.initBaseRecipes();
   }

   @Mod.EventHandler
   public void postinit(FMLPostInitializationEvent event) {
      proxy.postinit(event);
      ItemReg.postinit();
      BlockReg.postinit();
   }

   @Override
   public Config getConfig() {
      return config;
   }
}
