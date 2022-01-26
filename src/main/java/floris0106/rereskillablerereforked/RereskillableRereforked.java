package floris0106.rereskillablerereforked;

import floris0106.rereskillablerereforked.client.Keybind;
import floris0106.rereskillablerereforked.client.Overlay;
import floris0106.rereskillablerereforked.client.screen.InventoryTabs;
import floris0106.rereskillablerereforked.client.Tooltip;
import floris0106.rereskillablerereforked.common.Config;
import floris0106.rereskillablerereforked.common.compat.CuriosCompat;
import floris0106.rereskillablerereforked.common.EventHandler;
import floris0106.rereskillablerereforked.common.item.Items;
import floris0106.rereskillablerereforked.common.network.SkillWarning;
import floris0106.rereskillablerereforked.common.network.RequestLevelUp;
import floris0106.rereskillablerereforked.common.network.SyncConfig;
import floris0106.rereskillablerereforked.common.network.SyncSkills;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

@Mod(RereskillableRereforked.MOD_ID)
public class RereskillableRereforked
{
    public static final String MOD_ID = "rereskillablerereforked";

    public static SimpleChannel network;

    public RereskillableRereforked()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        Config.register();
        Items.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        Config.load();

        network = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "network"), () -> "1.0", s -> true, s -> true);
        network.registerMessage(0, SyncSkills.class, SyncSkills::encode, SyncSkills::new, SyncSkills::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        network.registerMessage(1, RequestLevelUp.class, RequestLevelUp::encode, RequestLevelUp::new, RequestLevelUp::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        network.registerMessage(2, SkillWarning.class, SkillWarning::encode, SkillWarning::new, SkillWarning::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        network.registerMessage(3, SyncConfig.class, SyncConfig::encode, SyncConfig::new, SyncConfig::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        MinecraftForge.EVENT_BUS.register(new EventHandler());

        if (ModList.get().isLoaded("curios"))
            MinecraftForge.EVENT_BUS.register(new CuriosCompat());
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new InventoryTabs());
        MinecraftForge.EVENT_BUS.register(new Tooltip());
        MinecraftForge.EVENT_BUS.register(new Keybind());
        MinecraftForge.EVENT_BUS.register(new Overlay());
    }
}