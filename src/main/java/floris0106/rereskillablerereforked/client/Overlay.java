package floris0106.rereskillablerereforked.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import floris0106.rereskillablerereforked.common.Config;
import floris0106.rereskillablerereforked.client.screen.SkillScreen;
import floris0106.rereskillablerereforked.common.capabilities.SkillCapability;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.skills.Requirement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

public class Overlay extends GuiComponent
{
    private static List<Requirement> requirements = null;
    private static int showTicks = 0;
    
    @SubscribeEvent
    public void onRenderOverlay(RenderGuiOverlayEvent.Post event)
    {
        if (event.getResult() == Event.Result.ALLOW && showTicks > 0)
        {
            Minecraft minecraft = Minecraft.getInstance();
            
            if (minecraft.player.getCapability(SkillCapability.INSTANCE).isPresent())
            {
                PoseStack stack = event.getPoseStack();

                RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);
                GL11.glEnable(GL11.GL_BLEND);
    
                int cx = event.getWindow().getGuiScaledWidth() / 2;
                int cy = event.getWindow().getGuiScaledHeight() / 4;
    
                String message = Component.translatable("overlay.message").getString();
                minecraft.font.drawShadow(stack, message, cx - minecraft.font.width(message) / 2f, cy, 0xFF5555);
    
                for (int i = 0; i < requirements.size(); i++)
                {
                    Requirement requirement = requirements.get(i);
                    int maxLevel = Config.getMaxLevel();
        
                    int x = cx + i * 20 - requirements.size() * 10 + 2;
                    int y = cy + 15;
                    int u = Math.min(requirement.level, maxLevel - 1) / (maxLevel / 4) * 16 + 176;
                    int v = requirement.skill.index * 16 + 128;

                    blit(stack, x, y, u, v, 16, 16);
        
                    String level = Integer.toString(requirement.level);
                    boolean met = SkillModel.get().getSkillLevel(requirement.skill) >= requirement.level;
                    minecraft.font.drawShadow(stack, level, x + 17 - minecraft.font.width(level), y + 9, met ? 0x55FF55 : 0xFF5555);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (showTicks > 0)
            showTicks--;
    }
    
    public static void showWarning(ResourceLocation resource)
    {
        requirements = Arrays.asList(Config.getRequirements(resource));
        showTicks = 60;
    }
}