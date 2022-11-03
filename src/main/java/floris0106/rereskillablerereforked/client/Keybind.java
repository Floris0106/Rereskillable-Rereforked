package floris0106.rereskillablerereforked.client;

import com.mojang.blaze3d.platform.InputConstants;
import floris0106.rereskillablerereforked.client.screen.SkillScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Keybind
{
    private static final KeyMapping openKey = new KeyMapping("key.skills", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 71, "Rereskillable");

    public Keybind()
    {
    }

    public static KeyMapping getOpenKey()
    {
        return openKey;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.InteractionKeyMappingTriggered event)
    {
        Minecraft minecraft = Minecraft.getInstance();

        if (openKey.consumeClick() && minecraft.screen == null)
            minecraft.setScreen(new SkillScreen());
    }
}