package floris0106.rereskillablerereforked.common.item;

import floris0106.rereskillablerereforked.RereskillableRereforked;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Items
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RereskillableRereforked.MOD_ID);

    public static final RegistryObject<Item> SKILL_FRAGMENT = ITEMS.register("skill_fragment", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));

    public static void register()
    {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}