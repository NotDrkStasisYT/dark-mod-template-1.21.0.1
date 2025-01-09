package net.drk.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.drk.command.ReportEntry;
import net.drk.command.SupportCenter;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

public class SupportCenterReportCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Shatterpoint")

                .then(CommandManager.literal("supportcenter")
                        .then(CommandManager.literal("report")
                                .then(CommandManager.argument("player", StringArgumentType.word())
                                        .then(CommandManager.argument("issue", StringArgumentType.greedyString())
                                                .executes(SupportCenterReportCommand::execute)
                                        )
                                )
                        )
                )
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        String playerName = StringArgumentType.getString(context, "player");
        String issue = StringArgumentType.getString(context, "issue");

        // Add the report to the list
        SupportCenter.addReport(new ReportEntry(playerName, issue));

        // Confirmation message
        source.sendFeedback(()->Text.literal("Report received: " + issue + " for player: " + playerName), true);

        return Command.SINGLE_SUCCESS;
    }
}
