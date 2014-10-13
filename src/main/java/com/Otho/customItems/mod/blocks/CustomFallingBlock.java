package com.Otho.customItems.mod.blocks;

import java.util.ArrayList;
import java.util.Random;

import com.Otho.customItems.lib.ModReference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class CustomFallingBlock  extends BlockFalling
{	
	public CustomFallingBlock() {
        this(Material.sand);
    }
    public CustomFallingBlock(Material material) {
        super(material); 
    }

    private IIcon[] icons = new IIcon[6];
	private boolean canSilkHarvest = false;
	private boolean renderNormaly;
	private boolean isAlpha = true;

	private int maxStackSize = 64;

	private boolean dropsItem = false;
	
	private int maxItemDrop;
	
	private int minItemDrop;
	private int eachExtraItemDropChance;
	
	private Item dropItem;
	
	private String[] textureNames;
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered (IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        Block i1 = par1IBlockAccess.getBlock(par2, par3, par4);
        return i1 == (Block) this ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
    }
		
	private int getItemDropQuantity(World world, int fortune)
    {
    	int ret = 0;
    	int i;
    	for(i=0;i < this.maxItemDrop + fortune;i++)
    	{
    		boolean willDrop = world.rand.nextInt(100) < this.eachExtraItemDropChance;
    		if(willDrop)
    			ret++;
    	}
    	if(ret < this.minItemDrop)
    		ret = this.minItemDrop;
    	
    	return ret;
    }
	
	
	public void setMaxItemDrop(int maxItemDrop) {
		this.maxItemDrop = maxItemDrop;
	}

	public void setMinItemDrop(int minItemDrop) {
		this.minItemDrop = minItemDrop;
	}

	public void setEachExtraItemDropChance(int eachExtraItemDropChance) {
		this.eachExtraItemDropChance = eachExtraItemDropChance;
	}

	public void setDropItem(Item dropItem) {
		this.dropItem = dropItem;
	}
	public void setOpaque(boolean isOpaque)
	{
		this.opaque = isOpaque;
		this.lightOpacity = this.isOpaqueCube() ? 255 : 0;
	}    
    
    public void setCanSilkHarvest(boolean canSilkHarvest) {
		this.canSilkHarvest = canSilkHarvest;
	}

	public void setMaxStackSize(int maxStackSize) {
		this.maxStackSize = maxStackSize;
		
		maxStackSize = Math.max(maxStackSize, 0);
		maxStackSize = Math.min(maxStackSize, 64);
		
		Item itemBlock = Item.getItemFromBlock(this);
        itemBlock.setMaxStackSize(this.maxStackSize);
	}  
	
	public void setRenderNormaly(boolean renderNormaly) {
		this.renderNormaly = renderNormaly;
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		if(this.textureName != null)
		{
			return blockIcon;
		}else
		{
			return icons[side];
		}
	}
	
	@Override
	public boolean renderAsNormalBlock()
    {
        return this.renderNormaly;
    }
			
	@Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> drops = new ArrayList(); 
        
        if(dropItem == null)
        {
        	drops.add(new ItemStack(Item.getItemFromBlock(this)));
        }else
        {
        	int itemQuantity = getItemDropQuantity(world, fortune);
           	drops.add(new ItemStack(dropItem, itemQuantity));
        }          
        
        return drops;
    }
   
    @Override
    public String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)    
    public void registerBlockIcons(IIconRegister iconRegister) {
    	if(textureNames == null)
    	{
	        if(this.textureName == null)
	    	{
	        	blockIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".")+1));
	    	}else
	    	{
	    		blockIcon = iconRegister.registerIcon(ModReference.MOD_ID.toLowerCase() + ":" + this.textureName);
	    	}
    	}else
    	{
    		for (int i = 0; i < icons.length; i++) {
    	        icons[i] = iconRegister.registerIcon(ModReference.MOD_ID.toLowerCase() + ":" + textureNames[i]);
    	    }
    	}
    }
    
    public void registerBlockTextures(String[] textureNames)
    {
    	this.textureNames = textureNames;
    }
    
    @Override
    public boolean isOpaqueCube ()
    {
        return this.opaque;
    }
	
	
	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
		return this.canSilkHarvest;
    }

}