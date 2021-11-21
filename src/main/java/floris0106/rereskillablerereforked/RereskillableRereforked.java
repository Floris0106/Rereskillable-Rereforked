package floris0106.rereskillablerereforked;

import floris0106.rereskillablerereforked.client.Keybind;
import floris0106.rereskillablerereforked.client.Overlay;
import floris0106.rereskillablerereforked.client.screen.InventoryTabs;
import floris0106.rereskillablerereforked.client.Tooltip;
import floris0106.rereskillablerereforked.common.Config;
import floris0106.rereskillablerereforked.common.compat.CuriosCompat;
import floris0106.rereskillablerereforked.common.EventHandler;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.capabilities.SkillStorage;
import floris0106.rereskillablerereforked.common.commands.Commands;
import floris0106.rereskillablerereforked.common.item.Items;
import floris0106.rereskillablerereforked.common.network.NotifyWarning;
import floris0106.rereskillablerereforked.common.network.RequestLevelUp;
import floris0106.rereskillablerereforked.common.network.SyncToClient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

@Mod(RereskillableRereforked.MOD_ID)
public class RereskillableRereforked
{
    public static final String MOD_ID = "rereskillablerereforked";

    public static SimpleChannel NETWORK;

    public RereskillableRereforked()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        Config.register();
        Items.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        CapabilityManager.INSTANCE.register(SkillModel.class, new SkillStorage(), () -> { throw new UnsupportedOperationException("No Implementation!"); });
        Config.load();

        NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "main_channel"), () -> "1.0", s -> true, s -> true);
        NETWORK.registerMessage(1, SyncToClient.class, SyncToClient::encode, SyncToClient::new, SyncToClient::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        NETWORK.registerMessage(2, RequestLevelUp.class, RequestLevelUp::encode, RequestLevelUp::new, RequestLevelUp::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        NETWORK.registerMessage(3, NotifyWarning.class, NotifyWarning::encode, NotifyWarning::new, NotifyWarning::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.register(new Commands());

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