package floris0106.rereskillablerereforked.common.network;

import floris0106.rereskillablerereforked.common.Config;
import floris0106.rereskillablerereforked.RereskillableRereforked;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.item.Items;
import floris0106.rereskillablerereforked.common.skills.Skill;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestLevelUp
{
    private final int skill;
    
    public RequestLevelUp(Skill skill)
    {
        this.skill = skill.index;
    }
    
    public RequestLevelUp(FriendlyByteBuf buffer)
    {
        skill = buffer.readInt();
    }
    
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeInt(skill);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() ->
        {
            ServerPlayer player = context.get().getSender();
            SkillModel skillModel = SkillModel.get(player);
            Skill skill = Skill.values()[this.skill];
            
            int cost = Config.getStartCost() + (skillModel.getSkillLevel(skill) - 1) * Config.getCostIncrease();

            if (Config.getUseXpPoints())
                cost = getXpNeededForLevel(cost);

            if (skillModel.getSkillLevel(skill) < Config.getMaxLevel())
            {
                if (Config.getUseSkillFragments())
                {
                    int fragments = ContainerHelper.clearOrCountMatchingItems(player.getInventory(), stack -> stack.getItem() == Items.SKILL_FRAGMENT.get(), 0, true);
                    if (player.isCreative() || fragments >= cost)
                    {
                        if (!player.isCreative())
                            ContainerHelper.clearOrCountMatchingItems(player.getInventory(),stack -> stack.getItem() == Items.SKILL_FRAGMENT.get(), cost, false);

                        skillModel.increaseSkillLevel(skill);

                        SyncSkills.send(player);
                    }
                }
                else
                {
                    int xp = Config.getUseXpPoints() ? player.totalExperience : player.experienceLevel;
                    if (player.isCreative() || xp >= cost)
                    {
                        if (!player.isCreative())
                            if (Config.getUseXpPoints())
                                player.giveExperiencePoints(-cost);
                            else
                                player.giveExperienceLevels(-cost);

                        skillModel.increaseSkillLevel(skill);

                        SyncSkills.send(player);
                    }
                }
            }
        });
        
        context.get().setPacketHandled(true);
    }

    private static int getXpNeededForLevel(int level)
    {
        int xp = 0;
        for (int i = 0; i < level; i++)
            if (level >= 30)
                xp += 112 + (level - 30) * 9;
            else
                xp += level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;

        return xp;
    }
    
    public static void send(Skill skill)
    {
        RereskillableRereforked.network.sendToServer(new RequestLevelUp(skill));
    }
}