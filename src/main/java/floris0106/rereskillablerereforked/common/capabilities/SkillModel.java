package floris0106.rereskillablerereforked.common.capabilities;

import floris0106.rereskillablerereforked.common.Config;
import floris0106.rereskillablerereforked.common.network.SkillWarning;
import floris0106.rereskillablerereforked.common.skills.Requirement;
import floris0106.rereskillablerereforked.common.skills.Skill;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class SkillModel implements INBTSerializable<CompoundNBT>
{
    public int[] skillLevels = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
    
    public int getSkillLevel(Skill skill)
    {
        return skillLevels[skill.index];
    }
    
    public void setSkillLevel(Skill skill, int level)
    {
        skillLevels[skill.index] = level;
    }
    
    public void increaseSkillLevel(Skill skill)
    {
        skillLevels[skill.index]++;
    }

    public boolean canUseItem(PlayerEntity player, ItemStack item)
    {
        return canUse(player, item.getItem().getRegistryName());
    }
    
    public boolean canUseBlock(PlayerEntity player, Block block)
    {
        return canUse(player, block.getBlock().getRegistryName());
    }
    
    public boolean canUseEntity(PlayerEntity player, Entity entity)
    {
        return canUse(player, entity.getType().getRegistryName());
    }
    
    private boolean canUse(PlayerEntity player, ResourceLocation resource)
    {
        Requirement[] requirements = Config.getRequirements(resource);
        
        if (requirements != null)
        {
            for (Requirement requirement : requirements)
            {
                if (getSkillLevel(requirement.skill) < requirement.level)
                {
                    if (player instanceof ServerPlayerEntity)
                    {
                        SkillWarning.send(player, resource);
                    }
                
                    return false;
                }
            }
        }
    
        return true;
    }
    
    public static SkillModel get(PlayerEntity player)
    {
        return player.getCapability(SkillCapability.INSTANCE).orElseThrow(() ->
            new IllegalArgumentException("Player " + player.getName().getContents() + " does not have a Skill Model!")
        );
    }
    
    public static SkillModel get()
    {
        return Minecraft.getInstance().player.getCapability(SkillCapability.INSTANCE).orElseThrow(() ->
            new IllegalArgumentException("Player does not have a Skill Model!")
        );
    }
    
    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("mining", skillLevels[0]);
        compound.putInt("gathering", skillLevels[1]);
        compound.putInt("attack", skillLevels[2]);
        compound.putInt("defense", skillLevels[3]);
        compound.putInt("building", skillLevels[4]);
        compound.putInt("farming", skillLevels[5]);
        compound.putInt("agility", skillLevels[6]);
        compound.putInt("magic", skillLevels[7]);
        
        return compound;
    }
    
    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        skillLevels[0] = nbt.getInt("mining");
        skillLevels[1] = nbt.getInt("gathering");
        skillLevels[2] = nbt.getInt("attack");
        skillLevels[3] = nbt.getInt("defense");
        skillLevels[4] = nbt.getInt("building");
        skillLevels[5] = nbt.getInt("farming");
        skillLevels[6] = nbt.getInt("agility");
        skillLevels[7] = nbt.getInt("magic");
    }

    public void copyFrom(SkillModel oldStore) {
        this.skillLevels = oldStore.skillLevels;
    }
}