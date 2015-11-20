package foxie.peltier.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import foxie.peltier.Peltier;

public class NEIPeltierConfig implements IConfigureNEI {
   @Override
   public void loadConfig() {
      API.registerRecipeHandler(new NEIPeltierHandler());
   }

   @Override
   public String getName() {
      return Peltier.NAME;
   }

   @Override
   public String getVersion() {
      return Peltier.VERSION;
   }
}
