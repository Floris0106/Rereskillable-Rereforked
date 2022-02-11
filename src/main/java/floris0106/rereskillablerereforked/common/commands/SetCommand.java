package floris0106.rereskillablerereforked.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import floris0106.rereskillablerereforked.common.Config;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.network.SyncSkills;
import floris0106.rereskillablerereforked.common.skills.Skill;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.server.command.EnumArgument;

public class SetCommand implements Command<CommandSource>
{
    private static final SetCommand COMMAND = new SetCommand();

    static ArgumentBuilder<CommandSource, ?> register()
    {
        return Commands.literal("set")
            .then(Commands.argument("skill", EnumArgument.enumArgument(Skill.class))
            .then(Commands.argument("level", IntegerArgumentType.integer(1, Config.getMaxLevel()))
            .executes(COMMAND)));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException
    {
        ServerPlayerEntity player = EntityArgument.getPlayer(context, "player");
        Skill skill = context.getArgument("skill", Skill.class);
        int level = IntegerArgumentType.getInteger(context, "level");

        SkillModel.get(player).setSkillLevel(skill, level);
        SyncSkills.send(player);

        return 1;
    }
}