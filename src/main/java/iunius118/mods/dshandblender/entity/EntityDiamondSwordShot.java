package iunius118.mods.dshandblender.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityDiamondSwordShot extends EntityThrowable {

	double prevMotionX;
	double prevMotionY;
	double prevMotionZ;

	public EntityDiamondSwordShot(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityDiamondSwordShot(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
		setHeadingFromThrower(throwerIn, throwerIn.rotationPitch, throwerIn.rotationYaw, 0.0F, 3.0F, 0.0F);
	}

	public EntityDiamondSwordShot(World worldIn) {
		super(worldIn);
	}

	@Override
	protected float getGravityVelocity() {
		return 0.0F;
	}

	@Override
	public void onUpdate() {
		prevMotionX = this.motionX;
		prevMotionY = this.motionY;
		prevMotionZ = this.motionZ;

		super.onUpdate();

		this.motionX = prevMotionX;
		this.motionY = prevMotionY;
		this.motionZ = prevMotionZ;

		if (!this.isDead && this.ticksExisted > 50) {
			onImpact(new RayTraceResult(this));
		}

		this.worldObj.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	}

	@Override
	protected void onImpact(RayTraceResult result) {

		if (!this.worldObj.isRemote) {
			((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, this.posX, this.posY, this.posZ, 1, 0.0D, 0.0D, 0.0D, 0.0D, new int[0]);
			this.worldObj.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

			double range = 5.0D;
			double x1 = result.hitVec.xCoord - range;
			double x2 = result.hitVec.xCoord + range;
			double y1 = result.hitVec.yCoord - range;
			double y2 = result.hitVec.yCoord + range;
			double z1 = result.hitVec.zCoord - range;
			double z2 = result.hitVec.zCoord + range;
	        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.getThrower(), new AxisAlignedBB(x1, y1, z1, x2, y2, z2));

			for(Entity entity : list) {
				float damage = 50.0F;

				if (entity instanceof EntityDragonPart) {
					entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.getThrower()), damage);

				} else if (entity instanceof EntityEnderman) {
					entity.attackEntityFrom(DamageSource.causeExplosionDamage(this.getThrower()), damage);

				} else if (entity instanceof EntityLivingBase) {
					entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.getThrower()), damage);
				}
			}
		}

		this.setDead();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		compound.setInteger("ticksExisted", this.ticksExisted);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		this.ticksExisted = compound.getInteger("ticksExisted");
	}

}
