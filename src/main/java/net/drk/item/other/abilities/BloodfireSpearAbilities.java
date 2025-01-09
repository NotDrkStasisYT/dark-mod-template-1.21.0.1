package net.drk.item.other.abilities;

import net.drk.item.ModItems;
import net.drk.network.packet.SummonEntityNetworking;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class BloodfireSpearAbilities {

    private static KeyBinding summonArrowKey;

    public static void registerAbilities() {
        registerKeyBindings();
        ClientTickEvents.END_CLIENT_TICK.register(BloodfireSpearAbilities::onKeyPress);
    }

    private static void registerKeyBindings() {
        summonArrowKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.drkmod.summonArrow",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.drkmod"
        ));
    }

    private static void onKeyPress(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        PlayerEntity player = client.player;

        // Check if the R key is pressed
        if (summonArrowKey.wasPressed()) {
            // Ensure the player is holding the Bloodfire Spear
            if (player.getMainHandStack().isOf(ModItems.BLOODFIRE_SPEAR)) {
                summonArrow(player);
            }
        }
    }

    private static void summonArrow(PlayerEntity player) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        // Get the player's eye position to ensure the arrow spawns in front of them.
        Vec3d playerEyePosition = player.getEyePos();

        // Calculate the direction the player is facing (rotation)
        Vec3d direction = player.getRotationVec(1.0F); // 1.0F for the player's rotation

        // Calculate the spawn position (slightly in front of the player)
        double spawnX = playerEyePosition.x + direction.x;
        double spawnY = playerEyePosition.y + direction.y;
        double spawnZ = playerEyePosition.z + direction.z;

        // Send a packet to summon the arrow at the correct location on the server
        SummonEntityNetworking.sendSummonEntityPacketToServer(Optional.of(Identifier.of("drkmod:ground_flame")));

        // Send a message to the player indicating that the arrow was summoned
        player.sendMessage(Text.literal("Arrow summoned!"), true);
    }


}
