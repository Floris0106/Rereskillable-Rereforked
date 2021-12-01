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

public class SkillWarning
{
    private final ResourceLocation resource;
    
    public SkillWarning(ResourceLocation resource)
    {
        this.resource = resource;
    }
    
    public SkillWarning(PacketBuffer buffer)
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
        RereskillableRereforked.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SkillWarning(resource));
    }
}