/* package net.drk.event;

import net.drk.PlayerData;
import net.drk.PlayerdataManager;
import net.drk.LuminCurrency;
import net.drk.command.FriendListManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MobDeathListener {

    public static void register() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, source) -> {
            // Check if the attacker is a player
            if (source.getAttacker() instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) source.getAttacker();

                // Ensure the killed entity is a LivingEntity (mob)
                if (entity instanceof LivingEntity) {
                    LivingEntity killedEntity = (LivingEntity) entity;

                    // Check if the killed entity is not a player
                    if (!(killedEntity instanceof ServerPlayerEntity)) {
                        // Print debug info
                        System.out.println("Player: " + player.getName().getString() + " killed a: " + killedEntity.getType().getName().getString());

                        // Retrieve player's data to update lumin
                        PlayerData playerData = PlayerdataManager.getPlayerData(player.getName().getString());
                        LuminCurrency luminCurrency = playerData.getLuminCurrency();

                        // Get lumin based on the specific entity type
                        EntityType<?> entityType = killedEntity.getType();
                        BigInteger luminFromEntity = EntityLuminMapper.getLuminForEntity(entityType);

                        // Calculate multiplier based on number of friends online
                        BigDecimal multiplier = FriendListManager.getLuminMultiplier(player);

                        // Apply the multiplier to the lumin amount
                        BigDecimal luminWithMultiplier = new BigDecimal(luminFromEntity).multiply(multiplier);
                        BigInteger finalLumin = luminWithMultiplier.setScale(0, BigDecimal.ROUND_DOWN).toBigInteger();

                        // Add lumin to the player's currency
                        luminCurrency.addLumin(finalLumin);

                        // Save updated player data
                        PlayerdataManager.setPlayerData(player.getName().getString(), playerData);

                        // Notify the player
                        player.sendMessage(Text.literal("You gained " + finalLumin + " lumin for killing a " + entityType.getName().getString() + " (Multiplier: " + multiplier + "x)!").formatted(Formatting.GOLD), false);
                    } else {
                        System.out.println("Killed entity was a player: " + killedEntity.getName().getString());
                    }
                }
            }
        });
    }
} */