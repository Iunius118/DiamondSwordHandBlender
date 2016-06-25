package iunius118.mods.dshandblender.client.renderer;

import iunius118.mods.dshandblender.DiamondSwordHandBlenderRegistry;
import iunius118.mods.dshandblender.client.model.ModelDiamondSwordHandBlender;
import iunius118.mods.dshandblender.tileentity.TileEntityRenderItem;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderDiamondSwordHandBlender extends TileEntitySpecialRenderer<TileEntityRenderItem> {

	@Override
	public void renderTileEntityAt(TileEntityRenderItem te, double x, double y, double z, float partialTicks, int destroyStage) {
		Minecraft mc = Minecraft.getMinecraft();
		IBakedModel model = mc.getRenderItem().getItemModelMesher().getModelManager().getModel(DiamondSwordHandBlenderRegistry.ModelLocations.modelItemDSHandBlender);

		if (model instanceof ModelDiamondSwordHandBlender) {
			GlStateManager.popMatrix();
			doRender((ModelDiamondSwordHandBlender)model);
			GlStateManager.pushMatrix();
		}
	}

	public void doRender(ModelDiamondSwordHandBlender model) {
		float rotation = 0.0F;

		if (	model.cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND ||
				model.cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND ||
				model.cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND ||
				model.cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND) {
			NBTTagCompound tag = model.itemStack.getTagCompound();

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

				if (model.entity instanceof EntityPlayer) {
					float cooldown = ((EntityPlayer)model.entity).getCooldownTracker().getCooldown(model.itemStack.getItem(), 0.0F);
					speed += (cooldown == 0.0D ? 0 : cooldown + 0.5F);
				}

				long renderNanoTime = System.nanoTime();
				rotation = ((renderNanoTime - prevRenderNanoTime) / 1000000.0F / interval * speed + prevRotation) % 360.0F;

				tag.setLong("renderTime", renderNanoTime);
				tag.setFloat("rotation", rotation);
			} else {
				model.itemStack.setTagCompound(new NBTTagCompound());
			}
		}

		model.rotationHead = rotation;

		VertexBuffer renderer = Tessellator.getInstance().getBuffer();

		switch (model.cameraTransformType) {
		case FIRST_PERSON_LEFT_HAND:
		case FIRST_PERSON_RIGHT_HAND:
			GlStateManager.translate(0F, -0.6F, -0.2F);
			GlStateManager.rotate(10.0F, 1.0F, 0.0F, 0.0F);
			break;

		case THIRD_PERSON_LEFT_HAND:
		case THIRD_PERSON_RIGHT_HAND:
			GlStateManager.translate(0.1F, -0.4F, -0.2F);
			GlStateManager.rotate(40.0F, -1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(25.0F, 0.0F, 0.0F, -1.0F);
			break;

		case GUI:
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		default:
			GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.scale(0.42F, 0.42F, 0.42F);
			GlStateManager.translate(-0.05F, -0.6F, 0.0F);
			break;
		}

		renderHandle(model, renderer);

		GlStateManager.rotate(model.rotationHead, 0.0F, 1.0F, 0.0F);

		renderShaft(model, renderer);
		renderBlades(model, renderer);
	}

	public void renderBlades(ModelDiamondSwordHandBlender model, VertexBuffer renderer) {
		List<BakedQuad> quads = model.modelOriginal.getQuads(null, null, 0L);
		int size = quads.size();

		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		RenderHelper.disableStandardItemLighting();
		float lastBrightnessX = OpenGlHelper.lastBrightnessX;
		float lastBrightnessY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

		for (int i = 0; i < 4; i++) {
			GlStateManager.pushMatrix();
			renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

			for (int j = 0; j < size; ++j) {
				LightUtil.renderQuadColor(renderer, quads.get(j), -1);
			}

			GlStateManager.scale(1.0F, 1.2F, 1.0F);
			GlStateManager.rotate(i * 90.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(0.0F, 0.95F, -0.2F);
			GlStateManager.rotate(-5.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);

			Tessellator.getInstance().draw();
			GlStateManager.popMatrix();
		}

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopAttrib();
	}

	public void renderShaft(ModelDiamondSwordHandBlender model, VertexBuffer renderer) {
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		// Render Iron shaft
		GlStateManager.pushMatrix();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		for (EnumFacing face : EnumFacing.VALUES) {
			List<BakedQuad> quads = mesher.getItemModel(new ItemStack(Blocks.IRON_BLOCK)).getQuads(null, face, 0L);
			int size = quads.size();

			for (int i = 0; i < size; ++i) {
				LightUtil.renderQuadColor(renderer, quads.get(i), -1);
			}

		}

		GlStateManager.scale(0.2F, 0.4F, 0.2F);
		GlStateManager.translate(-0.5F, 0.1F, -0.5F);

		Tessellator.getInstance().draw();
		GlStateManager.popMatrix();

		// Render Obsidian base
		GlStateManager.pushMatrix();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		for (EnumFacing face : EnumFacing.VALUES) {
			List<BakedQuad> quads = mesher.getItemModel(new ItemStack(Blocks.OBSIDIAN)).getQuads(null, face, 0L);
			int size = quads.size();

			for (int i = 0; i < size; ++i) {
				LightUtil.renderQuadColor(renderer, quads.get(i), -1);
			}

		}

		GlStateManager.scale(0.6F, 0.4F, 0.6F);
		GlStateManager.translate(-0.5F, 0.9F, -0.5F);

		Tessellator.getInstance().draw();
		GlStateManager.popMatrix();
	}

	public void renderHandle(ModelDiamondSwordHandBlender model, VertexBuffer renderer) {
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		// Render Red-stone handle
		GlStateManager.pushMatrix();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

		for (EnumFacing face : EnumFacing.VALUES) {
			List<BakedQuad> quads = mesher.getItemModel(new ItemStack(Blocks.REDSTONE_BLOCK)).getQuads(null, face, 0L);
			int size = quads.size();

			for (int i = 0; i < size; ++i) {
				LightUtil.renderQuadColor(renderer, quads.get(i), -1);
			}

		}

		GlStateManager.scale(0.5F, 1.0F, 0.5F);
		GlStateManager.translate(-0.5F, -0.75F, -0.5F);

		Tessellator.getInstance().draw();
		GlStateManager.popMatrix();
	}

}
