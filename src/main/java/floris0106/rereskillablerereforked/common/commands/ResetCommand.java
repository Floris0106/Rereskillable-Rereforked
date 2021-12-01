package floris0106.rereskillablerereforked.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.network.SyncSkills;
import floris0106.rereskillablerereforked.common.skills.Skill;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ResetCommand implements Command<CommandSource>
{
    private static final ResetCommand COMMAND = new ResetCommand();

    static ArgumentBuilder<CommandSource, ?> register()
    {
        return net.minecraft.command.Commands.literal("reset")
                .executes(COMMAND);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException
    {
        ServerPlayerEntity player = EntityArgument.getPlayer(context, "player");

        for (Skill skill : Skill.values())
            SkillModel.get(player).setSkillLevel(skill, 1);

        SyncSkills.send(player);

        return 1;
    }
}