package floris0106.rereskillablerereforked.client;

import floris0106.rereskillablerereforked.client.screen.SkillScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class Keybind
{
    public static final KeyMapping openKey = new KeyMapping("key.skills", GLFW.GLFW_KEY_G, "Rereskillable");

    public Keybind() {}

    @SubscribeEvent
    public void onKeyInput(TickEvent.ClientTickEvent evt)
    {
        if (evt.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (openKey.consumeClick() && minecraft.screen == null) {
            minecraft.setScreen(new SkillScreen());
        }
    }
}