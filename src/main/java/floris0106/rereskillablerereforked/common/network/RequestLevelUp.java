package floris0106.rereskillablerereforked.common.network;

import floris0106.rereskillablerereforked.common.Config;
import floris0106.rereskillablerereforked.RereskillableRereforked;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.item.Items;
import floris0106.rereskillablerereforked.common.skills.Skill;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestLevelUp
{
    private final int skill;
    
    public RequestLevelUp(Skill skill)
    {
        this.skill = skill.index;
    }
    
    public RequestLevelUp(PacketBuffer buffer)
    {
        skill = buffer.readInt();
    }
    
    public void encode(PacketBuffer buffer)
    {
        buffer.writeInt(skill);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = context.get().getSender();
            SkillModel skillModel = SkillModel.get(player);
            Skill skill = Skill.values()[this.skill];
            
            int cost = Config.getStartCost() + (skillModel.getSkillLevel(skill) - 1) * Config.getCostIncrease();

            if (skillModel.getSkillLevel(skill) < Config.getMaxLevel())
            {
                if (Config.getUseSkillFragments())
                {
                    int fragments = ItemStackHelper.clearOrCountMatchingItems(player.inventory, stack -> stack.getItem() == Items.SKILL_FRAGMENT.get(), 0, true);
                    if (player.isCreative() || fragments >= cost)
                    {
                        if (!player.isCreative())
                            ItemStackHelper.clearOrCountMatchingItems(player.inventory, stack -> stack.getItem() == Items.SKILL_FRAGMENT.get(), cost, false);

                        skillModel.increaseSkillLevel(skill);

                        SyncSkills.send(player);
                    }
                }
                else
                {
                    if (player.isCreative() || player.experienceLevel >= cost)
                    {
                        if (!player.isCreative())
                            player.giveExperienceLevels(-cost);

                        skillModel.increaseSkillLevel(skill);

                        SyncSkills.send(player);
                    }
                }
            }
        });
        
        context.get().setPacketHandled(true);
    }
    
    public static void send(Skill skill)
    {
        RereskillableRereforked.network.sendToServer(new RequestLevelUp(skill));
    }
}