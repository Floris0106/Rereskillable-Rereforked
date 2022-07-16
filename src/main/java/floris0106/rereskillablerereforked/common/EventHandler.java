package floris0106.rereskillablerereforked.common;

import com.mojang.brigadier.CommandDispatcher;
import floris0106.rereskillablerereforked.RereskillableRereforked;
import floris0106.rereskillablerereforked.common.capabilities.SkillCapability;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.capabilities.SkillProvider;
import floris0106.rereskillablerereforked.common.commands.SkillsCommand;
import floris0106.rereskillablerereforked.common.item.Items;
import floris0106.rereskillablerereforked.common.network.SyncConfig;
import floris0106.rereskillablerereforked.common.network.SyncSkills;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event)
    {
        Player player = event.getPlayer();
        ItemStack item = event.getItemStack();
        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
        SkillModel model = SkillModel.get(player);
        
        if (!player.isCreative() && (!model.canUseItem(player, item) || !model.canUseBlock(player, block)))
        {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        Player player = event.getPlayer();
        ItemStack item = event.getItemStack();
        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
        SkillModel model = SkillModel.get(player);
        
        if (!player.isCreative() && (!model.canUseItem(player, item) || !model.canUseBlock(player, block)))
        {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        Player player = event.getPlayer();
        ItemStack item = event.getItemStack();
        
        if (!player.isCreative() && !SkillModel.get(player).canUseItem(player, item))
        {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickEntity(PlayerInteractEvent.EntityInteract event)
    {
        Player player = event.getPlayer();
        Entity entity = event.getTarget();
        ItemStack item = event.getItemStack();
        
        if (!player.isCreative())
        {
            if (!SkillModel.get(player).canUseEntity(player, entity) || !SkillModel.get(player).canUseItem(player, item))
            {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onAttackEntity(AttackEntityEvent event)
    {
        Player player = event.getPlayer();
        
        if (player != null)
        {
            ItemStack item = player.getMainHandItem();
    
            if (!player.isCreative() && !SkillModel.get(player).canUseItem(player, item))
            {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChangeEquipment(LivingEquipmentChangeEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();

            if (!player.isCreative() && event.getSlot().getType() == EquipmentSlot.Type.ARMOR)
            {
                ItemStack item = event.getTo();
                
                if (!SkillModel.get(player).canUseItem(player, item))
                {
                    player.drop(item.copy(), false);
                    item.setCount(0);
                }
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityDrops(LivingDropsEvent event)
    {
        if (Config.getDisableWool() && event.getEntity() instanceof Sheep)
        {
            event.getDrops().removeIf(item -> item.getItem().is(ItemTags.WOOL));
        }
    }
    
    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event)
    {
        if (event.getEntity() instanceof Player player)
        {
            for (int i = 0; i < 8; i++)
            {
                SkillModel.get(player).skillLevels[i] -= player.getRandom().nextInt(Config.getDeathLevelLossMax() + 1 - Config.getDeathLevelLossMin());
                SkillModel.get(player).skillLevels[i] = Math.max(SkillModel.get(player).skillLevels[i] - Config.getDeathLevelLossMin(), 1);
            }
        }
    }

    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(SkillModel.class);
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof Player)
        {
            SkillModel skillModel = new SkillModel();
            SkillProvider provider = new SkillProvider(skillModel);
            
            event.addCapability(new ResourceLocation(RereskillableRereforked.MOD_ID, "cap_skills"), provider);
        }
    }

    @SubscribeEvent
    public void onAdvancement(AdvancementEvent event)
    {
        if (Config.getUseSkillFragments())
        {
            int fragments;

            DisplayInfo display = event.getAdvancement().getDisplay();

            if (display == null)
                fragments = Config.getSkillFragmentsFromTaskAdvancements();
            else
                fragments = switch (display.getFrame())
                {
                    case TASK -> Config.getSkillFragmentsFromTaskAdvancements();
                    case CHALLENGE -> Config.getSkillFragmentsFromChallengeAdvancements();
                    case GOAL -> Config.getSkillFragmentsFromGoalAdvancements();
                };

            event.getPlayer().addItem(new ItemStack(Items.SKILL_FRAGMENT.get(), fragments));
        }
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event)
    {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        SkillsCommand.register(dispatcher);
    }
    
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event)
    {
        SkillModel.get(event.getPlayer()).skillLevels = SkillModel.get(event.getOriginal()).skillLevels;
        event.getOriginal().getCapability(SkillCapability.INSTANCE).invalidate();
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e)
    {
        SyncSkills.send(e.getPlayer());
        SyncConfig.send(e.getPlayer());
    }
    
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent e)
    {
        SyncSkills.send(e.getPlayer());
    }
    
    @SubscribeEvent
    public void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent e)
    {
        SyncSkills.send(e.getPlayer());
    }
}
