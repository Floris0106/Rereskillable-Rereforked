package floris0106.rereskillablerereforked.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class SkillCapability
{
    @CapabilityInject(SkillModel.class)
    public static Capability<SkillModel> INSTANCE;
}