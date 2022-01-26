package floris0106.rereskillablerereforked.client.screen.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import floris0106.rereskillablerereforked.common.Config;
import floris0106.rereskillablerereforked.client.screen.SkillScreen;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.item.Items;
import floris0106.rereskillablerereforked.common.network.RequestLevelUp;
import floris0106.rereskillablerereforked.common.skills.Skill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;

public class SkillButton extends AbstractButton
{
    private final Skill skill;
    
    public SkillButton(int x, int y, Skill skill)
    {
        super(x, y, 79, 32, TextComponent.EMPTY);
        
        this.skill = skill;
    }
    
    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks)
    {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.textureManager.bindForSetup(SkillScreen.RESOURCES);
    
        int level = SkillModel.get().getSkillLevel(skill);
        int maxLevel = Config.getMaxLevel();
        
        int u = ((int) Math.ceil((double) level * 4 / maxLevel) - 1) * 16 + 176;
        int v = skill.index * 16 + 128;
        
        blit(stack, x, y, 176, (level == maxLevel ? 64 : 0) + (isMouseOver(mouseX, mouseY) ? 32 : 0), width, height);
        blit(stack, x + 6, y + 8, u, v, 16, 16);
        
        minecraft.font.draw(stack, new TranslatableComponent(skill.displayName), x + 25, y + 7, 0xFFFFFF);
        minecraft.font.draw(stack, level + "/" + maxLevel, x + 25, y + 18, 0xBEBEBE);
        
        if (isMouseOver(mouseX, mouseY) && level < maxLevel)
        {
            int cost = Config.getStartCost() + (level - 1) * Config.getCostIncrease();
            int available = Config.getUseSkillFragments() ? ContainerHelper.clearOrCountMatchingItems(minecraft.player.getInventory(), itemStack -> itemStack.getItem() == Items.SKILL_FRAGMENT.get(), 0, true) : minecraft.player.experienceLevel;
            int colour = available >= cost ? 0x7EFC20 : 0xFC5454;
            String text = Integer.toString(cost);
            
            minecraft.font.drawShadow(stack, text, x + 73 - minecraft.font.width(text), y + 18, colour);
        }
    }
    
    @Override
    public void onPress()
    {
        RequestLevelUp.send(skill);
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_)
    {

    }
}