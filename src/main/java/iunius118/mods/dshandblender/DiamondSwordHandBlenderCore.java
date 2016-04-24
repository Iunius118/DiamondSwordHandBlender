package iunius118.mods.dshandblender;

import iunius118.mods.dshandblender.client.ClientEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = DiamondSwordHandBlenderCore.MOD_ID,
	name = DiamondSwordHandBlenderCore.MOD_NAME,
	version = DiamondSwordHandBlenderCore.MOD_VERSION,
	dependencies = DiamondSwordHandBlenderCore.MOD_DEPENDENCIES,
	acceptedMinecraftVersions = DiamondSwordHandBlenderCore.MOD_ACCEPTED_MC_VERSIONS,
	useMetadata = true)
public class DiamondSwordHandBlenderCore {

	public static final String MOD_ID = "dshandblender";
	public static final String MOD_NAME = "DiamondSwordHandBlender";
	public static final String MOD_VERSION = "0.0.1";
	public static final String MOD_DEPENDENCIES = "required-after:Forge@[1.9-12.16.0.1865,)";
	public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.9]";

	@SideOnly(Side.CLIENT)
	public static float renderPartialTicks;
	@SideOnly(Side.CLIENT)
	public static int clientTicks;

	@Mod.Instance(MOD_ID)
	public static DiamondSwordHandBlenderCore INSTANCE;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		DiamondSwordHandBlenderRegistry.registerItems();
		DiamondSwordHandBlenderRegistry.resisterEntities();

		if (event.getSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
			DiamondSwordHandBlenderRegistry.registerItemModels();
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

	}

}