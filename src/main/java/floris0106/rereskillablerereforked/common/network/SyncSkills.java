package floris0106.rereskillablerereforked.common.network;

import floris0106.rereskillablerereforked.RereskillableRereforked;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class SyncSkills
{
    private final CompoundTag skillModel;
    
    public SyncSkills(CompoundTag skillModel)
    {
        this.skillModel = skillModel;
    }
    
    public SyncSkills(FriendlyByteBuf buffer)
    {
        skillModel = buffer.readNbt();
    }
    
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeNbt(skillModel);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() -> SkillModel.get().deserializeNBT(skillModel));
        context.get().setPacketHandled(true);
    }
    
    public static void send(Player player)
    {
        RereskillableRereforked.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new SyncSkills(SkillModel.get(player).serializeNBT()));
    }
}