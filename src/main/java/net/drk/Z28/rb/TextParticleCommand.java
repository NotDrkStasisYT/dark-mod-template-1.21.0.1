package net.drk.Z28.rb;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.drk.Z28.d.Color;  // Make sure to import your Color class
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayDeque;
import java.util.Deque;

public class TextParticleCommand {

    private static final Deque<net.drk.Z28.rb.TextParticle> particles = new ArrayDeque<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("spawnTextParticle")
                        .requires(source -> source.hasPermissionLevel(2)) // Adjust permission level as needed
                        .then(CommandManager.argument("text", StringArgumentType.string())
                                .executes(TextParticleCommand::spawnTextParticle)
                        )
        );
    }

    private static int spawnTextParticle(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String text = StringArgumentType.getString(context, "text");

        // Get the player who executed the command
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendError(Text.literal("Command can only be executed by a player."));
            return Command.SINGLE_SUCCESS;
        }

        // Get the player's position
        Vec3d particlePos = player.getPos().add(0.0, player.getHeight() + 0.25, 0.0); // Spawn slightly above the player
        Vec3d particleVelocity = player.getVelocity(); // Optionally use the player's velocity

        // Create a new TextParticle
        MinecraftClient client = MinecraftClient.getInstance();
        var world = client.world;
        net.drk.Z28.rb.TextParticle particle = new TextParticle(world, particlePos, particleVelocity);

        // Set the text and color for the particle
        particle.setText(text);
        particle.setColor(1.0f,1.0f,1.0f,1.0f); // RGB for white

        // Add the particle to the client
        client.particleManager.addParticle(particle);

        // Optional: Store the particle in a deque if needed for further manipulation
        particles.add(particle); // No need to cast; directly access the static field

        // Send feedback to the player using a Supplier
        source.sendFeedback(() -> Text.literal("Text particle spawned: " + text), false);

        return Command.SINGLE_SUCCESS;
    }
}
