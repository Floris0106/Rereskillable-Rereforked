package floris0106.rereskillablerereforked.common.item;

import floris0106.rereskillablerereforked.RereskillableRereforked;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Items
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RereskillableRereforked.MOD_ID);

    public static final RegistryObject<Item> SKILL_FRAGMENT = ITEMS.register("skill_fragment", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static void register()
    {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}