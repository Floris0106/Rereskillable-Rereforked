package floris0106.rereskillablerereforked.common;

import floris0106.rereskillablerereforked.common.skills.Requirement;
import floris0106.rereskillablerereforked.common.skills.Skill;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class Config
{
    private static final ForgeConfigSpec CONFIG_SPEC;
    private static final ForgeConfigSpec.BooleanValue DISABLE_WOOL;
    private static final ForgeConfigSpec.BooleanValue DEATH_RESET;
    private static final ForgeConfigSpec.BooleanValue USE_SKILL_FRAGMENTS;
    private static final ForgeConfigSpec.BooleanValue USE_XP_POINTS;
    private static final ForgeConfigSpec.IntValue SKILL_FRAGMENTS_FROM_ADVANCEMENTS_TASK;
    private static final ForgeConfigSpec.IntValue SKILL_FRAGMENTS_FROM_ADVANCEMENTS_CHALLENGE;
    private static final ForgeConfigSpec.IntValue SKILL_FRAGMENTS_FROM_ADVANCEMENTS_GOAL;
    private static final ForgeConfigSpec.IntValue STARTING_COST;
    private static final ForgeConfigSpec.IntValue COST_INCREASE;
    private static final ForgeConfigSpec.IntValue MAXIMUM_LEVEL;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> SKILL_LOCKS;

    private static Config config;

    private boolean disableWool = true;
    private boolean deathReset = false;
    private boolean useSkillFragments = false;
    private boolean useXpPoints = false;
    private int skillFragmentsFromTask = 1;
    private int skillFragmentsFromGoal = 2;
    private int skillFragmentsFromChallenge = 3;
    private int startingCost = 2;
    private int costIncrease = 1;
    private int maxLevel = 32;
    private final Map<ResourceLocation, Requirement[]> skillLocks = new HashMap<>();
    
    static
    {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        builder.comment("Disable wool drops to force the player to get shears.");
        DISABLE_WOOL = builder.define("disableWoolDrops", true);
    
        builder.comment("Reset all skills to 1 when a player dies.");
        DEATH_RESET = builder.define("deathSkillReset", false);

        builder.comment("Use skill fragments as the way of leveling up skills instead of XP levels.");
        USE_SKILL_FRAGMENTS = builder.define("useSkillFragments", false);

        builder.comment("Use XP points to level up instead of XP levels.");
        builder.comment("This makes it so that when you level up from a higher level, no more XP points will be consumed than when levelling up from a lower level");
        USE_XP_POINTS = builder.define("useXpPoints", false);

        builder.comment("Amount of skill fragments obtained by completing task advancements.");
        SKILL_FRAGMENTS_FROM_ADVANCEMENTS_TASK = builder.defineInRange("skillFragmentsFromTasks", 1, 0, 64);

        builder.comment("Amount of skill fragments obtained by completing goal advancements.");
        SKILL_FRAGMENTS_FROM_ADVANCEMENTS_GOAL = builder.defineInRange("skillFragmentsFromGoals", 2, 0, 64);

        builder.comment("Amount of skill fragments obtained by completing challenge advancements.");
        SKILL_FRAGMENTS_FROM_ADVANCEMENTS_CHALLENGE = builder.defineInRange("skillFragmentsFromChallenges", 3, 0, 64);

        builder.comment("Starting cost of upgrading to level 2, in levels.");
        STARTING_COST = builder.defineInRange("startingCost", 2, 0, 10);
        
        builder.comment("Amount of levels added to the cost with each upgrade (use 0 for constant cost).");
        COST_INCREASE = builder.defineInRange("costIncrease", 1, 0, 10);
        
        builder.comment("Maximum level each skill can be upgraded to.");
        MAXIMUM_LEVEL = builder.defineInRange("maximumLevel", 32, 2, 100);
        
        builder.comment("List of item and block skill requirements.", "Format: mod:id skill:level", "Valid skills: attack, defence/defense, mining, gathering, farming, building, agility, magic");
        SKILL_LOCKS = builder.defineList("skillLocks", Arrays.asList("minecraft:iron_sword attack:5", "minecraft:iron_shovel gathering:5", "minecraft:iron_pickaxe mining:5", "minecraft:iron_axe gathering:5", "minecraft:iron_hoe farming:5", "minecraft:iron_helmet defence:5", "minecraft:iron_chestplate defence:5", "minecraft:iron_leggings defence:5", "minecraft:iron_boots defence:5", "minecraft:diamond_sword attack:15", "minecraft:diamond_shovel gathering:15", "minecraft:diamond_pickaxe mining:15", "minecraft:diamond_axe gathering:15", "minecraft:diamond_hoe farming:15", "minecraft:diamond_helmet defence:15", "minecraft:diamond_chestplate defence:15", "minecraft:diamond_leggings defence:15", "minecraft:diamond_boots defence:15", "minecraft:netherite_sword attack:30", "minecraft:netherite_shovel gathering:30", "minecraft:netherite_pickaxe mining:30", "minecraft:netherite_axe gathering:30", "minecraft:netherite_hoe farming:30", "minecraft:netherite_helmet defence:30", "minecraft:netherite_chestplate defence:30", "minecraft:netherite_leggings defence:30", "minecraft:netherite_boots defence:30", "minecraft:fishing_rod gathering:5", "minecraft:shears gathering:5", "minecraft:lead farming:5", "minecraft:bow attack:5 agility:3", "minecraft:turtle_helmet defence:10", "minecraft:shield defence:5", "minecraft:crossbow attack:5 agility:5", "minecraft:trident attack:15 agility:10", "minecraft:golden_apple magic:5", "minecraft:enchanted_golden_apple magic:10", "minecraft:ender_pearl magic:5", "minecraft:ender_eye magic:10", "minecraft:piston building:5", "minecraft:sticky_piston building:10", "minecraft:tnt building:5", "minecraft:ender_chest magic:15", "minecraft:enchanting_table magic:10", "minecraft:anvil building:5", "minecraft:chipped_anvil building:5", "minecraft:damaged_anvil building:5", "minecraft:smithing_table building:10", "minecraft:end_crystal magic:20", "minecraft:boat agility:5", "minecraft:minecart agility:10", "minecraft:elytra agility:20", "minecraft:horse agility:10", "minecraft:donkey agility:10", "minecraft:mule agility:10", "minecraft:strider agility:15"), obj -> true);
        
        CONFIG_SPEC = builder.build();
    }

    public Config() { }

    public Config(CompoundTag nbt)
    {
        disableWool = nbt.getBoolean("disable_wool");
        deathReset = nbt.getBoolean("death_reset");
        useSkillFragments = nbt.getBoolean("use_skill_fragments");
        useXpPoints = nbt.getBoolean("use_xp_points");
        skillFragmentsFromTask = nbt.getInt("skill_fragments_from_task");
        skillFragmentsFromGoal = nbt.getInt("skill_fragments_from_goal");
        skillFragmentsFromChallenge = nbt.getInt("skill_fragments_from_challenge");
        startingCost = nbt.getInt("starting_cost");
        costIncrease = nbt.getInt("cost_increase");
        maxLevel = nbt.getInt("max_level");

        ListTag skillLocksNBT = nbt.getList("skill_locks", 8);
        skillLocksNBT.forEach(skillLock ->
        {
            StringTag string = (StringTag)skillLock;

            String[] entry = string.getAsString().split(" ");
            Requirement[] requirements = new Requirement[entry.length - 1];
            for (int i = 1; i < entry.length; i++)
            {
                String[] req = entry[i].split(":");

                if (req[0].equalsIgnoreCase("defense"))
                    req[0] = "defence";

                requirements[i - 1] = new Requirement(Skill.valueOf(req[0].toUpperCase()), Integer.parseInt(req[1]));
            }

            skillLocks.put(new ResourceLocation(entry[0]), requirements);
        });
    }

    public void encode(CompoundTag nbt)
    {
        nbt.putBoolean("disable_wool", disableWool);
        nbt.putBoolean("death_reset", deathReset);
        nbt.putBoolean("use_skill_fragments", useSkillFragments);
        nbt.putBoolean("use_xp_points", useXpPoints);
        nbt.putInt("skill_fragments_from_task", skillFragmentsFromTask);
        nbt.putInt("skill_fragments_from_goal", skillFragmentsFromGoal);
        nbt.putInt("skill_fragments_from_challenge", skillFragmentsFromChallenge);
        nbt.putInt("starting_cost", startingCost);
        nbt.putInt("cost_increase", costIncrease);
        nbt.putInt("max_level", maxLevel);

        ListTag skillLocksNBT = new ListTag();
        skillLocks.forEach(((resourceLocation, requirements) ->
        {
            StringBuilder stringBuilder = new StringBuilder(resourceLocation.toString());
            for (Requirement requirement : requirements)
                stringBuilder.append(" ").append(requirement.toString());

            skillLocksNBT.add(StringTag.valueOf(stringBuilder.toString()));
        }));

        nbt.put("skill_locks", skillLocksNBT);
    }

    public static void register()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG_SPEC);
    }
    
    public static void load()
    {
        config = new Config();
        config.disableWool = DISABLE_WOOL.get();
        config.deathReset = DEATH_RESET.get();
        config.useSkillFragments = USE_SKILL_FRAGMENTS.get();
        config.useXpPoints = USE_XP_POINTS.get();
        config.skillFragmentsFromTask = SKILL_FRAGMENTS_FROM_ADVANCEMENTS_TASK.get();
        config.skillFragmentsFromGoal = SKILL_FRAGMENTS_FROM_ADVANCEMENTS_GOAL.get();
        config.skillFragmentsFromChallenge = SKILL_FRAGMENTS_FROM_ADVANCEMENTS_CHALLENGE.get();
        config.startingCost = STARTING_COST.get();
        config.costIncrease = COST_INCREASE.get();
        config.maxLevel = MAXIMUM_LEVEL.get();

        for (String line : SKILL_LOCKS.get())
        {
            String[] entry = line.split(" ");
            Requirement[] requirements = new Requirement[entry.length - 1];
            for (int i = 1; i < entry.length; i++)
            {
                String[] req = entry[i].split(":");
                
                if (req[0].equalsIgnoreCase("defense"))
                    req[0] = "defence";
                
                requirements[i - 1] = new Requirement(Skill.valueOf(req[0].toUpperCase()), Integer.parseInt(req[1]));
            }
            
            config.skillLocks.put(new ResourceLocation(entry[0]), requirements);
        }
    }

    public static void set(Config config)
    {
        Config.config = config;
    }

    public static Config get()
    {
        return config;
    }

    public static boolean getDisableWool()
    {
        return config.disableWool;
    }
    
    public static boolean getDeathReset()
    {
        return config.deathReset;
    }

    public static boolean getUseSkillFragments()
    {
        return config.useSkillFragments;
    }

    public static boolean getUseXpPoints()
    {
        return config.useXpPoints;
    }

    public static int getSkillFragmentsFromTaskAdvancements()
    {
        return config.skillFragmentsFromTask;
    }

    public static int getSkillFragmentsFromChallengeAdvancements()
    {
        return config.skillFragmentsFromChallenge;
    }

    public static int getSkillFragmentsFromGoalAdvancements()
    {
        return config.skillFragmentsFromGoal;
    }
    
    public static int getStartCost()
    {
        return config.startingCost;
    }
    
    public static int getCostIncrease()
    {
        return config.costIncrease;
    }
    
    public static int getMaxLevel()
    {
        return config.maxLevel;
    }
    
    public static Requirement[] getRequirements(ResourceLocation key)
    {
        return config.skillLocks.get(key);
    }
}