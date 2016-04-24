package iunius118.mods.dshandblender.client;

import iunius118.mods.dshandblender.DiamondSwordHandBlenderRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		DiamondSwordHandBlenderRegistry.registerBakedModels(event);
		DiamondSwordHandBlenderRegistry.registerRenderers();
	}

}
