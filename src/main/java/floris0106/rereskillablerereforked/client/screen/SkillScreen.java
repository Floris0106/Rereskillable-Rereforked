package floris0106.rereskillablerereforked.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import floris0106.rereskillablerereforked.client.screen.buttons.SkillButton;
import floris0106.rereskillablerereforked.common.skills.Skill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class SkillScreen extends Screen
{
    public static final ResourceLocation RESOURCES = new ResourceLocation("rereskillablerereforked", "textures/gui/skills.png");
    
    public SkillScreen()
    {
        super(new TranslatableComponent("container.skills"));
    }
    
    @Override
    protected void init()
    {
        int left = (width - 162) / 2;
        int top = (height - 128) / 2;
        
        for (int i = 0; i < 8; i++)
        {
            int x = left + i % 2 * 83;
            int y = top + i / 2 * 36;

            addWidget(new SkillButton(x, y, Skill.values()[i]));
        }
    }
    
    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks)
    {
        Minecraft.getInstance().textureManager.bindForSetup(RESOURCES);
        
        int left = (width - 176) / 2;
        int top = (height - 166) / 2;
        
        renderBackground(stack);
        
        blit(stack, left, top, 0, 0, 176, 166);
        font.draw(stack, title, width / 2 - font.width(title) / 2, top + 6, 0x3F3F3F);
        
        super.render(stack, mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}