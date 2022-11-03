package floris0106.rereskillablerereforked.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import floris0106.rereskillablerereforked.client.screen.buttons.SkillButton;
import floris0106.rereskillablerereforked.common.skills.Skill;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SkillScreen extends Screen
{
    public static final ResourceLocation RESOURCES = new ResourceLocation("rereskillablerereforked", "textures/gui/skills.png");
    
    public SkillScreen()
    {
        super(Component.translatable("container.skills"));
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

            addRenderableWidget(new SkillButton(x, y, Skill.values()[i]));
        }
    }
    
    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.setShaderTexture(0, RESOURCES);
        
        int left = (width - 176) / 2;
        int top = (height - 166) / 2;
        
        renderBackground(stack);

        blit(stack, left, top, 0, 0, 176, 166);
        font.draw(stack, title, (width - font.width(title)) / 2f, top + 6, 0x3F3F3F);
        
        super.render(stack, mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}