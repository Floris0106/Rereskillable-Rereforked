package floris0106.rereskillablerereforked.common.network;

import floris0106.rereskillablerereforked.RereskillableRereforked;
import floris0106.rereskillablerereforked.client.Overlay;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class NotifyWarning
{
    private final ResourceLocation resource;
    
    public NotifyWarning(ResourceLocation resource)
    {
        this.resource = resource;
    }
    
    public NotifyWarning(PacketBuffer buffer)
    {
        resource = buffer.readResourceLocation();
    }
    
    public void encode(PacketBuffer buffer)
    {
        buffer.writeResourceLocation(resource);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() -> Overlay.showWarning(resource));
        context.get().setPacketHandled(true);
    }
    
    public static void send(PlayerEntity player, ResourceLocation resource)
    {
        RereskillableRereforked.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new NotifyWarning(resource));
    }
}