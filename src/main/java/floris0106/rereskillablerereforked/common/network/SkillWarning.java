package floris0106.rereskillablerereforked.common.network;

import floris0106.rereskillablerereforked.RereskillableRereforked;
import floris0106.rereskillablerereforked.client.Overlay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class SkillWarning
{
    private final ResourceLocation resource;
    
    public SkillWarning(ResourceLocation resource)
    {
        this.resource = resource;
    }
    
    public SkillWarning(FriendlyByteBuf buffer)
    {
        resource = buffer.readResourceLocation();
    }
    
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeResourceLocation(resource);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() -> Overlay.showWarning(resource));
        context.get().setPacketHandled(true);
    }
    
    public static void send(Player player, ResourceLocation resource)
    {
        RereskillableRereforked.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new SkillWarning(resource));
    }
}