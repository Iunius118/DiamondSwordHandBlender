package iunius118.mods.dshandblender.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDiamondSwordShot extends EntityThrowable {

	double prevMotionX;
	double prevMotionY;
	double prevMotionZ;

	public EntityDiamondSwordShot(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityDiamondSwordShot(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
		func_184538_a(throwerIn, throwerIn.rotationPitch, throwerIn.rotationYaw, 0.0F, 3.0F, 0.0F);
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

		this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		this.worldObj.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.entity_generic_explode, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

		if (!this.worldObj.isRemote) {
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

				} else if (entity instanceof EntityLivingBase) {
					entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.getThrower()), damage);
				}
			}
		}

		this.setDead();
	}

}
