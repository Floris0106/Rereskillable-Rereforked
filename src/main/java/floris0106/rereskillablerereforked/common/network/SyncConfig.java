package floris0106.rereskillablerereforked.common.network;

import floris0106.rereskillablerereforked.RereskillableRereforked;
import floris0106.rereskillablerereforked.common.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class SyncConfig
{
    private final Config config;

    public SyncConfig(Config config)
    {
        this.config = config;
    }

    public SyncConfig(PacketBuffer buffer)
    {
        config = new Config(buffer.readNbt());
    }

    public void encode(PacketBuffer buffer)
    {
        System.out.println("encode");

        CompoundNBT nbt = new CompoundNBT();
        Config.get().encode(nbt);
        buffer.writeNbt(nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() -> Config.set(config));
        context.get().setPacketHandled(true);
    }

    public static void send(PlayerEntity player)
    {
        RereskillableRereforked.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SyncConfig(Config.get()));
    }
}