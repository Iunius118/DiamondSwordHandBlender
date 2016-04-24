package iunius118.mods.dshandblender.client.model;

import iunius118.mods.dshandblender.DiamondSwordHandBlenderRegistry;

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
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelDiamondSwordHandBlender implements IPerspectiveAwareModel {

	public IBakedModel modelOriginal;
	public float rotationHead;
	public boolean isAnimated;
	public boolean isFirstPerson;

	public ModelDiamondSwordHandBlender(IBakedModel modelGUIIn) {
		this(modelGUIIn, true, false);
	}

	public ModelDiamondSwordHandBlender(IBakedModel modelGUIIn, float angleRotation, boolean isFirsePersonView) {
		this(modelGUIIn, true, isFirsePersonView);
		rotationHead = angleRotation;
	}

	public ModelDiamondSwordHandBlender(IBakedModel modelGUIIn, boolean isAnimatedModel, boolean isFirsePersonView) {
		modelOriginal = modelGUIIn;
		isFirstPerson = isFirsePersonView;
		isAnimated = isAnimatedModel;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		if (side == null) {
			VertexBuffer renderer = Tessellator.getInstance().getBuffer();
			Tessellator.getInstance().draw();
			GlStateManager.popMatrix();

			DiamondSwordHandBlenderRegistry.Renderers.renderDiamondSwordHandBlender.doRender(this);

			GlStateManager.pushMatrix();
			renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
		}

		return Collections.EMPTY_LIST;
	}

	@Override
	public boolean isAmbientOcclusion() {	// ブロック関連
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {	// ブロック関連
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
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		Matrix4f matrix;

		if (modelOriginal != null && modelOriginal instanceof IPerspectiveAwareModel) {
			matrix = ((IPerspectiveAwareModel) modelOriginal).handlePerspective(cameraTransformType).getValue();
		} else {
			matrix = TRSRTransformation.identity().getMatrix();
		}

		switch (cameraTransformType) {
		case THIRD_PERSON_LEFT_HAND:
		case THIRD_PERSON_RIGHT_HAND:
			isFirstPerson = false;
			return Pair.of(this, matrix);

		case FIRST_PERSON_LEFT_HAND:
		case FIRST_PERSON_RIGHT_HAND:
			isFirstPerson = true;
			return Pair.of(new ModelDiamondSwordHandBlender(modelOriginal, rotationHead, true), matrix);

		default:
			return Pair.of(new ModelDiamondSwordHandBlender(modelOriginal, false, false), matrix);
		}
	}

	public class DiamondSwordHandBlenderItemOverrideList extends ItemOverrideList {

		public DiamondSwordHandBlenderItemOverrideList() {
			super(Collections.EMPTY_LIST);
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
			ModelDiamondSwordHandBlender model = (ModelDiamondSwordHandBlender)originalModel;

			if (!model.isAnimated) {
				return model;
			}

			NBTTagCompound tag = stack.getTagCompound();
			float rotation = 0.2F;

			if (tag != null) {
				final float interval = 0.8F;

				long prevRenderNanoTime = 0L;
				float prevRotation = 0.0F;
				float speed = 0.2F;

				if (tag.hasKey("renderTime", NBT.TAG_LONG)) {
					prevRenderNanoTime = tag.getLong("renderTime");
				}

				if (tag.hasKey("rotation", NBT.TAG_FLOAT)) {
					prevRotation = tag.getFloat("rotation");
				}

				if (entity instanceof EntityPlayer) {
					float cooldown = ((EntityPlayer)entity).getCooldownTracker().getCooldown(stack.getItem(), 0.0F);
					speed += (cooldown == 0.0D ? 0 : cooldown + 0.5F);
				}

				long renderNanoTime = System.nanoTime();
				rotation = ((renderNanoTime - prevRenderNanoTime) / 1000000.0F / interval * speed + prevRotation) % 360.0F;

				tag.setLong("renderTime", renderNanoTime);
				tag.setFloat("rotation", rotation);
			} else {
				stack.setTagCompound(new NBTTagCompound());
			}

			return new ModelDiamondSwordHandBlender(model.modelOriginal, rotation, model.isFirstPerson);
		}

	}

}
