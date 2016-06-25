package iunius118.mods.dshandblender.item;

import iunius118.mods.dshandblender.entity.EntityDiamondSwordShot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemDiamondSwordHandBlender extends Item {

	public ItemDiamondSwordHandBlender() {
		setCreativeTab(CreativeTabs.TOOLS);
		setMaxStackSize(1);
		this.setFull3D();
	}

	public void setCooldown(ItemStack itemStackIn, EntityPlayer playerIn, int cooldownTicks, int swingTicks) {
		playerIn.getCooldownTracker().setCooldown(itemStackIn.getItem(), cooldownTicks);

		NBTTagCompound tag = itemStackIn.getTagCompound();

		if (tag != null) {
			tag.setInteger("swingTicks", swingTicks);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (hand == EnumHand.OFF_HAND) {
			return  new ActionResult(EnumActionResult.PASS, itemStackIn);
		}

		worldIn.playSound((EntityPlayer)null,  playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 0.5F, 0.4F);
		worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.8F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!worldIn.isRemote) {
			EntityDiamondSwordShot entity = new EntityDiamondSwordShot(worldIn, playerIn);
			worldIn.spawnEntityInWorld(entity);
		}

		setCooldown(itemStackIn, playerIn, 4, 5);
		playerIn.addStat(StatList.getObjectUseStats(this));

		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		NBTTagCompound tag = stack.getTagCompound();

		if (tag != null) {
			if (entityIn instanceof EntityPlayer) {
				EntityPlayer playerIn = (EntityPlayer)entityIn;
				int swingTicks = tag.getInteger("swingTicks");

				if (swingTicks > 0) {
					playerIn.swingProgressInt = 3;
					playerIn.isSwingInProgress = true;
					tag.setInteger("swingTicks", --swingTicks);
				} else {
					tag.setInteger("swingTicks", 0);
				}
			}
		}
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();

		if (tag != null) {
			if (entityLiving instanceof EntityPlayer) {
				EntityPlayer playerIn = (EntityPlayer)entityLiving;
				int swingTicks = tag.getInteger("swingTicks");

				if (swingTicks == 0) {
					entityLiving.worldObj.playSound((EntityPlayer)null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.NEUTRAL, 0.1F, 1.4F);
				}
			}
		}

		entityLiving.worldObj.playSound((EntityPlayer)null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, SoundEvents.ENTITY_IRONGOLEM_DEATH, SoundCategory.NEUTRAL, 1.0F, 1.4F);
		entityLiving.worldObj.playSound((EntityPlayer)null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, SoundEvents.ENTITY_BLAZE_DEATH, SoundCategory.NEUTRAL, 0.8F, 1.0F);

		if (entityLiving instanceof EntityPlayer) {
			setCooldown(stack, (EntityPlayer)entityLiving, 40, 40);
		}

		return super.onEntitySwing(entityLiving, stack);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		float damage = 150.0F;

		if (target instanceof EntityDragon) {
			((EntityDragon)target).dragonPartHead.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage);
		} else {
			target.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage);
		}

		return false;
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		return 1500.0F;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return true;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		return 3;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}

}
