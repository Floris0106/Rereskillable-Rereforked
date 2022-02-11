package floris0106.rereskillablerereforked.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;

public class SkillsCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSource>literal("skills")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(SetCommand.register())
                                .then(GetCommand.register())
                                .then(ResetCommand.register()))
        );
    }
}