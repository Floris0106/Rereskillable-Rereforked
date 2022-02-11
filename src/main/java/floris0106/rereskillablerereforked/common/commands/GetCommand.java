package floris0106.rereskillablerereforked.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import floris0106.rereskillablerereforked.common.capabilities.SkillModel;
import floris0106.rereskillablerereforked.common.skills.Skill;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.server.command.EnumArgument;

public class GetCommand implements Command<CommandSource>
{
    private static final GetCommand COMMAND = new GetCommand();

    static ArgumentBuilder<CommandSource, ?> register()
    {
        return Commands.literal("get")
            .then(Commands.argument("skill", EnumArgument.enumArgument(Skill.class))
            .executes(COMMAND));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException
    {
        ServerPlayerEntity player = EntityArgument.getPlayer(context, "player");
        Skill skill = context.getArgument("skill", Skill.class);
        int level = SkillModel.get(player).getSkillLevel(skill);

        context.getSource().sendSuccess(new TranslationTextComponent(skill.displayName).append(" " + level), true);

        return 1;
    }
}