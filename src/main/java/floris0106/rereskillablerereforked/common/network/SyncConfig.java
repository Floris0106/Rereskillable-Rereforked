package floris0106.rereskillablerereforked.common.network;

import floris0106.rereskillablerereforked.RereskillableRereforked;
import floris0106.rereskillablerereforked.common.Config;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class SyncConfig
{
    private final Config config;

    public SyncConfig(Config config)
    {
        this.config = config;
    }

    public SyncConfig(FriendlyByteBuf buffer)
    {
        config = new Config(buffer.readNbt());
    }

    public void encode(FriendlyByteBuf buffer)
    {
        System.out.println("encode");

        CompoundTag nbt = new CompoundTag();
        Config.get().encode(nbt);
        buffer.writeNbt(nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() -> Config.set(config));
        context.get().setPacketHandled(true);
    }

    public static void send(Player player)
    {
        RereskillableRereforked.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new SyncConfig(Config.get()));
    }
}