package foxie.peltier.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxie.lib.FoxyBlockTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockPeltier extends FoxyBlockTE {
   IIcon iconSide;
   IIcon iconHot;
   IIcon iconCold;

   public BlockPeltier() {
      super(Material.iron);
      setCreativeTab(CreativeTabs.tabRedstone);
      setBlockName("peltier");
   }

   @Override
   public TileEntity createNewTileEntity(World world, int meta) {
      return new TileEntityPeltier();
   }

   @Override
   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister registrar) {
      iconSide = registerIcon(registrar, "side");
      iconHot = registerIcon(registrar, "hot");
      iconCold = registerIcon(registrar, "cold");
   }

   @Override
   public IIcon getIcon(int side, int meta) {
      // orientation is based on metadata.
      // metadata's orientation points to
      //  the hot side
      if (meta == side) {
         return iconHot;
      } else if (ForgeDirection.values()[meta].getOpposite().ordinal() == side) {
         return iconCold;
      }

      return iconSide;
   }

   @Override
   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
      int l = BlockPistonBase.determineOrientation(world, x, y, z, entity);
      world.setBlockMetadataWithNotify(x, y, z, l, 2);
      TileEntityPeltier peltier = (TileEntityPeltier) world.getTileEntity(x, y, z);
      peltier.updateRecipe();
   }

   @Override
   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if (world.isRemote)
         return true;

      TileEntityPeltier peltier = (TileEntityPeltier) world.getTileEntity(x, y, z);
      player.addChatComponentMessage(new ChatComponentText("Power: " + peltier.getEnergyStored(ForgeDirection.UNKNOWN)));
      player.addChatComponentMessage(new ChatComponentText("Heat: " + peltier.getStoredHeat()));

      return true;
   }

   @Override
   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      TileEntityPeltier peltier = (TileEntityPeltier) world.getTileEntity(x, y, z);
      peltier.updateRecipe();
   }
}
