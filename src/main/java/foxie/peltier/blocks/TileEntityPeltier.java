package foxie.peltier.blocks;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import foxie.lib.Configurable;
import foxie.lib.FoxyTileEntity;
import foxie.peltier.PeltierRecipes;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityPeltier extends FoxyTileEntity implements IEnergyReceiver {
   @Configurable(max = "100000", min = "1", comment = "capacity of a Peltier cell")
   private static int peltier_capacity = 10000;

   @Configurable(max = "10000", min = "1", comment = "how much heat is used per tick")
   private static int peltier_use_per_tick = 10;

   @Configurable(comment = "wastes heat when no usable recipe is found")
   private static boolean wastes_power = true;

   int storedHeat = 0;
   PeltierRecipes.PeltierRecipe currentRecipe;
   EnergyStorage                storage;

   boolean has_ticked = false; // first tick = check recipe...

   public TileEntityPeltier() {
      storage = new EnergyStorage(peltier_capacity);
   }

   @Override
   public void readFromNBT(NBTTagCompound tag) {
      super.readFromNBT(tag);
      storedHeat = tag.getInteger("storedHeat");
      storage.readFromNBT(tag);
   }

   @Override
   public void writeToNBT(NBTTagCompound tag) {
      super.writeToNBT(tag);
      tag.setInteger("storedHeat", storedHeat);
      storage.writeToNBT(tag);
   }

   @Override
   public void updateEntity() {
      if (!has_ticked) {
         has_ticked = true;
         updateRecipe();
      }

      // heat up and cold up
      int extracted = storage.extractEnergy(peltier_use_per_tick, false);
      if (extracted >= peltier_use_per_tick)
         storedHeat++;

      // now do heat update
      if (currentRecipe == null) {
         if (wastes_power)
            storedHeat = 0;

         return;
      } else {
         if (storedHeat >= currentRecipe.heat) {
            PeltierRecipes.PeltierRecipe recipe = currentRecipe;
            int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            ForgeDirection direction = ForgeDirection.getOrientation(meta);
            worldObj.setBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, recipe.hotSideOut, recipe.hotMetaOut, 3);
            direction = direction.getOpposite();
            worldObj.setBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, recipe.coldSideOut, recipe.coldMetaOut, 3);
            updateRecipe();
            storedHeat = 0;
            worldObj.notifyBlockChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
         }
      }
   }

   public int getStoredHeat() {
      return storedHeat;
   }

   public void updateRecipe() {
      if (worldObj.isRemote)
         return;

      int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
      // get hot side == meta side
      ForgeDirection direction = ForgeDirection.getOrientation(meta);
      Block blockHot = worldObj.getBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
      int metaHot = worldObj.getBlockMetadata(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
      direction = direction.getOpposite();
      Block blockCold = worldObj.getBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
      int metaCold = worldObj.getBlockMetadata(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);

      System.out.println("hot side: " + blockHot.getUnlocalizedName() + ", cold: " + blockCold.getUnlocalizedName());

      currentRecipe = PeltierRecipes.findRecipe(blockCold, metaCold, blockHot, metaHot);
   }

   @Override
   public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
      return storage.receiveEnergy(maxReceive, simulate);
   }

   @Override
   public int getEnergyStored(ForgeDirection from) {
      return storage.getEnergyStored();
   }

   @Override
   public int getMaxEnergyStored(ForgeDirection from) {
      return storage.getMaxEnergyStored();
   }

   @Override
   public boolean canConnectEnergy(ForgeDirection from) {
      return true;
   }
}
