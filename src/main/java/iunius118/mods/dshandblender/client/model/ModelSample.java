package iunius118.mods.dshandblender.client.model;

import java.util.Collections;
import java.util.List;

import javax.vecmath.Matrix4f;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.common.model.TRSRTransformation;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

public class ModelSample implements IPerspectiveAwareModel {

	/* Original */
	public IBakedModel originalModel;

	/* SampleItemOverrideList#handleItemState */
	public ItemStack itemStack;
	public World world;
	public EntityLivingBase entity;

	/* handlePerspective */
	public TransformType cameraTransformType;

	/* getQuads */
	public IBlockState state;
	public EnumFacing side;
	public long rand;
	public float partialTicks;

	/* Constructor for the model register */
	public ModelSample(IBakedModel bakedModelIn) {
		this(bakedModelIn, null, null, null);
	}

	/* Constructor for  SampleItemOverrideList#handleItemState */
	public ModelSample(IBakedModel bakedModelIn, ItemStack itemStackIn, World worldIn, EntityLivingBase entityLivingBaseIn) {
		if (bakedModelIn instanceof ModelSample) {
			originalModel = ((ModelSample)bakedModelIn).originalModel;
		} else {
			originalModel = bakedModelIn;
		}

		itemStack = itemStackIn;
		world = worldIn;
		entity = entityLivingBaseIn;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState blockStateIn, EnumFacing enumFacingIn, long longRand) {
		state = blockStateIn;
		side = enumFacingIn;
		rand = longRand;
		partialTicks = Animation.getPartialTickTime();

		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer renderer = tessellator.getBuffer();
		tessellator.draw();
		GlStateManager.popMatrix();

		/* TODO: Render quads */

		/*
 		//	Example:
 		SomeRenderer.doRender(this);
		 */

		/*
 		//	Another example:
		List<BakedQuad> quads = originalModel.getQuads(blockStateIn, enumFacingIn, longRand);
		int size = quads.size();

		GlStateManager.pushMatrix();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		for (int i = 0; i < size; ++i) {
			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, quads.get(i), -1);
		}

		tessellator.draw();
		GlStateManager.popMatrix();
		 */

		GlStateManager.pushMatrix();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		return Collections.EMPTY_LIST;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return originalModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return originalModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return originalModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return originalModel.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return originalModel.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new SampleItemOverrideList(Collections.EMPTY_LIST);
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType transformTypeIn) {
		Matrix4f matrix;

		if (originalModel != null && originalModel instanceof IPerspectiveAwareModel) {
			matrix = ((IPerspectiveAwareModel) originalModel).handlePerspective(cameraTransformType).getValue();
		} else {
			matrix = TRSRTransformation.identity().getMatrix();
		}

		cameraTransformType = transformTypeIn;

		return Pair.of(this, matrix);
	}

	public class SampleItemOverrideList extends ItemOverrideList {

		public SampleItemOverrideList(List<ItemOverride> overridesIn) {
			super(overridesIn);
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
			return new ModelSample(originalModel, stack, world, entity);
		}
	}

}
