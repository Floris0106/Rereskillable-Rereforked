package floris0106.rereskillablerereforked.common.network;

import floris0106.rereskillablerereforked.RereskillableRereforked;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class SyncToClient
{
    private final CompoundNBT skillModel;
    
    public SyncToClient(CompoundNBT skillModel)
    {
        this.skillModel = skillModel;
    }
    
    public SyncToClient(PacketBuffer buffer)
    {
        skillModel = buffer.readNbt();
    }
    
    public void encode(PacketBuffer buffer)
    {
        buffer.writeNbt(skillModel);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() -> SkillModel.get().deserializeNBT(skillModel));
        context.get().setPacketHandled(true);
    }
    
    public static void send(PlayerEntity player)
    {
        RereskillableRereforked.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SyncToClient(SkillModel.get(player).serializeNBT()));
    }
}