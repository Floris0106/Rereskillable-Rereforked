package floris0106.rereskillablerereforked.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import floris0106.rereskillablerereforked.common.Config;
import floris0106.rereskillablerereforked.client.screen.SkillScreen;
import floris0106.rereskillablerereforked.common.capabilities.SkillCapability;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.skills.Requirement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
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
        if (event.getResult() != Event.Result.DENY && showTicks > 0)
        {
            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.player.getCapability(SkillCapability.INSTANCE).isPresent()) {
                Tesselator tessellator = Tesselator.getInstance();

                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.1f); // I set the opacity of the draw here

                RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);

                // Base coordinates for background (top left)
                int width = 150;
                int height = 40;
                int cx = event.getWindow().getGuiScaledWidth() / 2;
                int cy = event.getWindow().getGuiScaledHeight() / 4;
                int tlx = cx - (width / 2);
                int tly = cy;

                float unit = 1.0f / 255.0f;

                // Do a blit
                BufferBuilder builder = tessellator.getBuilder();
                builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                builder.vertex(tlx, tly + height, -90.0D).uv(0.0F, 233.0f / 255.0f).endVertex();                                // bottom left
                builder.vertex(tlx + width, tly + height, -90.0D).uv((141.0F / 255.0F) + unit, 233.0f / 255.0f).endVertex();    // bottom right
                builder.vertex(tlx + width, tly, -90.0D).uv((141.0F / 255.0F) + unit, (194.0F / 255.0F) - unit).endVertex();    // top right
                builder.vertex(tlx, tly, -90.0D).uv(0.0F, (194.0F / 255.0F) - unit).endVertex();                                // top left
                tessellator.end();

                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                RenderSystem.disableBlend();

                // Background drawn, time or text or other textures
                PoseStack stack = event.getPoseStack();

                // Draw the "You can't use this item yet!" text
                String message = Component.translatable("overlay.message").getString();
                minecraft.font.drawShadow(stack, message, cx - minecraft.font.width(message) / 2f, cy+2, 0xFF5555);

                // Draw the icon and required level text, shouldn't need to do it the RenderSystem/Tessellator way, but that would be cleaner
                for (int i = 0; i < requirements.size(); i++)
                {
                    Requirement requirement = requirements.get(i);
                    int maxLevel = Config.getMaxLevel();

                    int x = cx + i * 20 - requirements.size() * 10 + 2;
                    int y = cy + 12;
                    int u = Math.min(requirement.level, maxLevel - 1) / (maxLevel / 4) * 16 + 176;
                    int v = requirement.skill.index * 16 + 128;

                    RenderSystem.setShaderTexture(0, SkillScreen.RESOURCES);
                    blit(stack, x, y, u, v, 16, 16);

                    String level = Integer.toString(requirement.level);
                    boolean met = SkillModel.get().getSkillLevel(requirement.skill) >= requirement.level;
                    minecraft.font.drawShadow(stack, level, x + 17 - minecraft.font.width(level), y + 17, met ? 0x55FF55 : 0xFF5555);
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