package iunius118.mods.dshandblender;

import iunius118.mods.dshandblender.client.model.ModelDiamondSwordHandBlender;
import iunius118.mods.dshandblender.client.renderer.RenderDiamondSwordHandBlender;
import iunius118.mods.dshandblender.client.renderer.RenderFactoryDiamondSwordShot;
import iunius118.mods.dshandblender.entity.EntityDiamondSwordShot;
import iunius118.mods.dshandblender.item.ItemDiamondSwordHandBlender;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DiamondSwordHandBlenderRegistry {

	public static void resisterEntities() {
		EntityRegistry.registerModEntity(EntityDiamondSwordShot.class, "entity_freeze", EntityID.DIAMOND_SWORD_SHOT.ordinal(), DiamondSwordHandBlenderCore.INSTANCE, 256, 5, true);
	}

	public enum EntityID {
		DIAMOND_SWORD_SHOT,
	}

	public static void registerItems() {
		GameRegistry.register(Items.itemDSHandBlender);
	}

	public static class Items {
		private static String NAME_ITEM_DS_HAND_BLENDER = "ds_hand_blender";

		public static Item itemDSHandBlender = new ItemDiamondSwordHandBlender().setRegistryName(NAME_ITEM_DS_HAND_BLENDER).setUnlocalizedName(NAME_ITEM_DS_HAND_BLENDER);
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemModels() {
		ModelLoader.setCustomModelResourceLocation(Items.itemDSHandBlender, 0, ModelLocations.modelItemDSHandBlender);
	}

	@SideOnly(Side.CLIENT)
	public static class ModelLocations {
		public static ModelResourceLocation modelItemDSHandBlender = new ModelResourceLocation(Items.itemDSHandBlender.getRegistryName(), "inventory");
	}

	public static class Renderers {
		public static RenderDiamondSwordHandBlender renderDiamondSwordHandBlender;
		public static RenderFactoryDiamondSwordShot renderDiamondSwordShot;
	}

	@SideOnly(Side.CLIENT)
	public static void registerRenderers() {
		Renderers.renderDiamondSwordHandBlender = new RenderDiamondSwordHandBlender();
		Renderers.renderDiamondSwordShot = new RenderFactoryDiamondSwordShot();

		RenderingRegistry.registerEntityRenderingHandler(EntityDiamondSwordShot.class, Renderers.renderDiamondSwordShot);
	}

	@SideOnly(Side.CLIENT)
	public static void registerBakedModels(ModelBakeEvent event) {
		ModelDiamondSwordHandBlender modelDiamondSwordHandBlender = new ModelDiamondSwordHandBlender(
				event.getModelRegistry().getObject(ModelLocations.modelItemDSHandBlender));

		event.getModelRegistry().putObject(ModelLocations.modelItemDSHandBlender, modelDiamondSwordHandBlender);
	}

}
