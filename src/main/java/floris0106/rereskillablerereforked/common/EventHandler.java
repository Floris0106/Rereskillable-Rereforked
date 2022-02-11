package floris0106.rereskillablerereforked.common;

import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.capabilities.SkillProvider;
import floris0106.rereskillablerereforked.common.item.Items;
import floris0106.rereskillablerereforked.common.network.SyncConfig;
import floris0106.rereskillablerereforked.common.network.SyncSkills;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import sun.security.pkcs11.wrapper.CK_INFO;

public class EventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event)
    {
        PlayerEntity player = event.getPlayer();
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
        PlayerEntity player = event.getPlayer();
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
        PlayerEntity player = event.getPlayer();
        ItemStack item = event.getItemStack();
        
        if (!player.isCreative() && !SkillModel.get(player).canUseItem(player, item))
        {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickEntity(PlayerInteractEvent.EntityInteract event)
    {
        PlayerEntity player = event.getPlayer();
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
        PlayerEntity player = event.getPlayer();
        
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
        if (event.getEntity() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            
            if (!player.isCreative() && event.getSlot().getType() == EquipmentSlotType.Group.ARMOR)
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
        if (Config.getDisableWool() && event.getEntity() instanceof SheepEntity)
        {
            event.getDrops().removeIf(item -> ItemTags.getAllTags().getTag(new ResourceLocation("minecraft", "wool")).contains(item.getItem().getItem()));
        }
    }
    
    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event)
    {
        if (event.getEntity() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            for (int i = 0; i < 8; i++)
            {
                SkillModel.get(player).skillLevels[i] -= player.getRandom().nextInt(Config.getDeathLevelLossMax() + 1 - Config.getDeathLevelLossMin());
                SkillModel.get(player).skillLevels[i] = Math.max(SkillModel.get(player).skillLevels[i] - Config.getDeathLevelLossMin(), 1);
            }
        }
    }
    
    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof PlayerEntity)
        {
            SkillModel skillModel = new SkillModel();
            SkillProvider provider = new SkillProvider(skillModel);
            
            event.addCapability(new ResourceLocation("rereskillable", "cap_skills"), provider);
            event.addListener(provider::invalidate);
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
                switch (display.getFrame())
                {
                    case TASK:
                        fragments = Config.getSkillFragmentsFromTaskAdvancements();
                        break;
                    case CHALLENGE:
                        fragments = Config.getSkillFragmentsFromChallengeAdvancements();
                        break;
                    case GOAL:
                        fragments = Config.getSkillFragmentsFromGoalAdvancements();
                        break;
                    default:
                        fragments = 0;
                        break;
                }

            event.getPlayer().addItem(new ItemStack(Items.SKILL_FRAGMENT.get(), fragments));
        }
    }
    
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event)
    {
        SkillModel.get(event.getPlayer()).skillLevels = SkillModel.get(event.getOriginal()).skillLevels;
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
