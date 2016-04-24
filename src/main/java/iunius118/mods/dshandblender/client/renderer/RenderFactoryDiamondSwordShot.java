package iunius118.mods.dshandblender.client.renderer;

import iunius118.mods.dshandblender.DiamondSwordHandBlenderCore;
import iunius118.mods.dshandblender.client.model.ModelDiamondSwordHandBlender;
import iunius118.mods.dshandblender.entity.EntityDiamondSwordShot;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import org.lwjgl.opengl.GL11;

public class RenderFactoryDiamondSwordShot<T extends EntityDiamondSwordShot> implements IRenderFactory<T> {

	@Override
	public Render<? super T> createRenderFor(RenderManager manager) {
		return new RenderDiamondSwordShot(manager);
	}

	public class RenderDiamondSwordShot extends Render<T> {

		protected RenderDiamondSwordShot(RenderManager renderManager) {
			super(renderManager);
		}

		@Override
		public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
			if (entity.ticksExisted < 2) {
				return;
			}

			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem()
					.getItemModelMesher();

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.translate((float) x, (float) y, (float) z);
			GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
	        GlStateManager.disableLighting();
	        GlStateManager.enableBlend();
	        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

			//GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			RenderHelper.disableStandardItemLighting();
			float lastBrightnessX = OpenGlHelper.lastBrightnessX;
			float lastBrightnessY = OpenGlHelper.lastBrightnessY;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

			vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

			ModelDiamondSwordHandBlender model = (ModelDiamondSwordHandBlender)mesher.getItemModel(new ItemStack(DiamondSwordHandBlenderCore.Items.itemDSHandBlender));
			List<BakedQuad> quads =	model.modelOriginal.getQuads(null, null, 0L);
			int size = quads.size();

			for (int i = 0; i < size; ++i) {
				net.minecraftforge.client.model.pipeline.LightUtil
						.renderQuadColor(vertexbuffer, quads.get(i), -1);
			}

			GlStateManager.rotate(-45.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);

			if (this.renderOutlines) {
				GlStateManager.enableColorMaterial();
				GlStateManager.enableOutlineMode(this.getTeamColor(entity));
			}

			tessellator.draw();

			GlStateManager.disableBlend();
	        GlStateManager.enableLighting();

			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
			RenderHelper.enableStandardItemLighting();
			//GL11.glPopAttrib();

			GlStateManager.disableRescaleNormal();
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
			super.doRender(entity, x, y, z, entityYaw, partialTicks);

		}

		@Override
		protected ResourceLocation getEntityTexture(T entity) {
			return null;
		}

	}

}
