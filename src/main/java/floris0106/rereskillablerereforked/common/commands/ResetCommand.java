package floris0106.rereskillablerereforked.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.network.SyncSkills;
import floris0106.rereskillablerereforked.common.skills.Skill;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class ResetCommand implements Command<CommandSourceStack>
{
    private static final ResetCommand COMMAND = new ResetCommand();

    static ArgumentBuilder<CommandSourceStack, ?> register()
    {
        return Commands.literal("reset")
                .executes(COMMAND);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
    {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");

        for (Skill skill : Skill.values())
            SkillModel.get(player).setSkillLevel(skill, 1);

        SyncSkills.send(player);

        return 1;
    }
}