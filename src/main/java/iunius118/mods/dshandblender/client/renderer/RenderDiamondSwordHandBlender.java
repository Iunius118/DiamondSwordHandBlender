package iunius118.mods.dshandblender.client.renderer;

import iunius118.mods.dshandblender.client.model.ModelDiamondSwordHandBlender;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderDiamondSwordHandBlender {

	public void doRender(ModelDiamondSwordHandBlender model) {
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
			GlStateManager.scale(0.45F, 0.45F, 0.45F);
			GlStateManager.translate(0.0F, -0.6F, 0.0F);
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
			List<BakedQuad> quads = mesher.getItemModel(new ItemStack(Blocks.iron_block)).getQuads(null, face, 0L);
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
			List<BakedQuad> quads = mesher.getItemModel(new ItemStack(Blocks.obsidian)).getQuads(null, face, 0L);
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
			List<BakedQuad> quads = mesher.getItemModel(new ItemStack(Blocks.redstone_block)).getQuads(null, face, 0L);
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
