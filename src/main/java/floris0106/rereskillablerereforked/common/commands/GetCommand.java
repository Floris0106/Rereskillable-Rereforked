package floris0106.rereskillablerereforked.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.skills.Skill;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;

public class GetCommand implements Command<CommandSourceStack>
{
    private static final GetCommand COMMAND = new GetCommand();

    static ArgumentBuilder<CommandSourceStack, ?> register()
    {
        return Commands.literal("get")
            .then(Commands.argument("skill", EnumArgument.enumArgument(Skill.class))
            .executes(COMMAND));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
    {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        Skill skill = context.getArgument("skill", Skill.class);
        int level = SkillModel.get(player).getSkillLevel(skill);

        context.getSource().sendSuccess(new TranslatableComponent(skill.displayName).append(" " + level), true);

        return 1;
    }
}