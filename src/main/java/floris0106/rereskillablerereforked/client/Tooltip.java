package floris0106.rereskillablerereforked.client;

import floris0106.rereskillablerereforked.common.Config;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.skills.Requirement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class Tooltip
{
    @SubscribeEvent
    public void onTooltipDisplay(ItemTooltipEvent event)
    {
        if (Minecraft.getInstance().player != null)
        {
            Requirement[] requirements = Config.getRequirements(event.getItemStack().getItem().getRegistryName());
    
            if (requirements != null)
            {
                List<ITextComponent> tooltips = event.getToolTip();
                tooltips.add(StringTextComponent.EMPTY);
                tooltips.add(new TranslationTextComponent("tooltip.requirements").append(":").withStyle(TextFormatting.GRAY));
        
                for (Requirement requirement : requirements)
                {
                    TextFormatting colour = SkillModel.get().getSkillLevel(requirement.skill) >= requirement.level ? TextFormatting.GREEN : TextFormatting.RED;
                    tooltips.add(new TranslationTextComponent(requirement.skill.displayName).append(" " + requirement.level).withStyle(colour));
                }
            }
        }
    }
}