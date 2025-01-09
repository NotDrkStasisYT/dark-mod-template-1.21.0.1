package net.drk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class AdminSupportCenterReportCommand {

    private static final int REPORTS_PER_PAGE = 5; // Number of reports per page

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("Shatterpoint")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("admin")
                        .requires(source -> source.hasPermissionLevel(2))

                        .then(CommandManager.literal("supportcenter")
                                .then(CommandManager.literal("report")
                                        .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                                                .executes(context -> {
                                                    int page = IntegerArgumentType.getInteger(context, "page");
                                                    ServerCommandSource source = context.getSource();

                                                    List<ReportEntry> reports = SupportCenter.getReports(); // Replace with actual method to get reports
                                                    int totalPages = (int) Math.ceil((double) reports.size() / REPORTS_PER_PAGE);

                                                    if (page < 1 || page > totalPages) {
                                                        source.sendFeedback(()->Text.literal("§cInvalid page number.").formatted(Formatting.RED), false);
                                                        return 1;
                                                    }

                                                    source.sendFeedback(()->Text.literal("§6--- Reported Issues (Page " + page + "/" + totalPages + ") ---").formatted(Formatting.GOLD), false);

                                                    int startIndex = (page - 1) * REPORTS_PER_PAGE;
                                                    int endIndex = Math.min(startIndex + REPORTS_PER_PAGE, reports.size());

                                                    for (int i = startIndex; i < endIndex; i++) {
                                                        ReportEntry report = reports.get(i);
                                                        String formattedReport = report.formattedMessage();
                                                        source.sendFeedback(()->Text.literal("§7- ").formatted(Formatting.GRAY)
                                                                .append(Text.literal(formattedReport).formatted(Formatting.WHITE)), false);
                                                    }

                                                    return 1;
                                                }))))));
    }
}
