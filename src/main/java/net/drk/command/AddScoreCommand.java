package net.drk.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.math.BigInteger;

public class AddScoreCommand {

    // Scoreboard names for different parts of the number
    private static final String SCOREBOARD_PREFIX = "large_number_";
    private static final String BILLIONS = SCOREBOARD_PREFIX + "billions";
    private static final String MILLIONS = SCOREBOARD_PREFIX + "millions";
    private static final String THOUSANDS = SCOREBOARD_PREFIX + "thousands";
    private static final String UNITS = SCOREBOARD_PREFIX + "units";

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("setlarge")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("player", StringArgumentType.string())
                        .requires(source -> source.hasPermissionLevel(2))

                        .then(CommandManager.argument("number", StringArgumentType.string())
                                .executes(context -> {
                                    String playerName = StringArgumentType.getString(context, "player");
                                    String numberStr = StringArgumentType.getString(context, "number");
                                    ServerCommandSource source = context.getSource();

                                    // Convert the number to BigInteger
                                    BigInteger number = new BigInteger(numberStr);

                                    // Split the number into parts

                                    BigInteger billions = number.divide(BigInteger.valueOf(1_000_000_000));
                                    BigInteger millions = number.mod(BigInteger.valueOf(1_000_000_000))
                                            .divide(BigInteger.valueOf(1_000_000));
                                    BigInteger thousands = number.mod(BigInteger.valueOf(1_000_000))
                                            .divide(BigInteger.valueOf(1_000));
                                    BigInteger units = number.mod(BigInteger.valueOf(1_000));

                                    // Construct scoreboard commands
                                    String commandBillions = String.format("scoreboard players set %s %s %d", playerName, BILLIONS, billions.intValue());
                                    String commandMillions = String.format("scoreboard players set %s %s %d", playerName, MILLIONS, millions.intValue());
                                    String commandThousands = String.format("scoreboard players set %s %s %d", playerName, THOUSANDS, thousands.intValue());
                                    String commandUnits = String.format("scoreboard players set %s %s %d", playerName, UNITS, units.intValue());

                                    // Execute commands silently
                                    ServerCommandSource silentSource = source.withSilent();
                                    source.getServer().getCommandManager().getDispatcher().execute(commandBillions, silentSource);
                                    source.getServer().getCommandManager().getDispatcher().execute(commandMillions, silentSource);
                                    source.getServer().getCommandManager().getDispatcher().execute(commandThousands, silentSource);
                                    source.getServer().getCommandManager().getDispatcher().execute(commandUnits, silentSource);

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }
}
