package iunius118.mods.dshandblender.client.model;

import java.util.Collections;
import java.util.List;

import javax.vecmath.Matrix4f;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.Pair;

@SideOnly(Side.CLIENT)
public class ModelDiamondSwordHandBlender implements IPerspectiveAwareModel {

	public IBakedModel modelOriginal;
	public ItemStack itemStack;
	public World world;
	public EntityLivingBase entity;
	public TransformType cameraTransformType = TransformType.NONE;
	public float rotationHead;

	public ModelDiamondSwordHandBlender(IBakedModel bakedModelIn) {
		if (bakedModelIn instanceof ModelDiamondSwordHandBlender) {
			modelOriginal = ((ModelDiamondSwordHandBlender)bakedModelIn).modelOriginal;
		} else {
			modelOriginal = bakedModelIn;
		}
	}

	public void handleItemState(ItemStack itemStackIn, World worldIn, EntityLivingBase entityLivingBaseIn) {
		itemStack = itemStackIn;
		world = worldIn;
		entity = entityLivingBaseIn;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return Collections.emptyList();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return true;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return null;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return modelOriginal.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new DiamondSwordHandBlenderItemOverrideList();
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType transformTypeIn) {
		cameraTransformType = transformTypeIn;

		Matrix4f matrix;

		if (modelOriginal != null && modelOriginal instanceof IPerspectiveAwareModel) {
			matrix = ((IPerspectiveAwareModel) modelOriginal).handlePerspective(transformTypeIn).getValue();
		} else {
			matrix = TRSRTransformation.identity().getMatrix();
		}

		return Pair.of(this, matrix);
	}

	public class DiamondSwordHandBlenderItemOverrideList extends ItemOverrideList {

		public DiamondSwordHandBlenderItemOverrideList() {
			super(Collections.EMPTY_LIST);
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
			if (originalModel instanceof ModelDiamondSwordHandBlender) {
				((ModelDiamondSwordHandBlender)originalModel).handleItemState(stack, world, entity);
			}

			return originalModel;
		}

	}

}
