package net.drk;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import java.util.HashMap;
import java.util.Map;

public class TradeManager {
    private static final Map<PlayerEntity, TradeOffer> tradeOffers = new HashMap<>();
    private static final Map<PlayerEntity, StringBuilder> tradeHistories = new HashMap<>();

    public static void initiateTrade(PlayerEntity player1, PlayerEntity player2) {
        tradeOffers.put(player1, new TradeOffer(player1, player2));
        tradeOffers.put(player2, new TradeOffer(player2, player1));
        player1.sendMessage(Text.literal("Trade initiated with " + player2.getName().getString()), false);
        player2.sendMessage(Text.literal(player1.getName().getString() + " wants to trade with you. Use /Shatterpoint trade accept or /Shatterpoint trade cancel."), false);
    }

    public static void acceptTrade(PlayerEntity player) {
        TradeOffer offer = tradeOffers.get(player);
        if (offer != null) {
            if (!offer.hasRequiredItems()) {
                player.sendMessage(Text.literal("You do not have the required items to complete the trade."), false);
                return;
            }

            PlayerEntity otherPlayer = offer.getOtherPlayer();
            offer.setAccepted(true);

            player.sendMessage(Text.literal("Trade accepted."), false);
            otherPlayer.sendMessage(Text.literal(player.getName().getString() + " accepted the trade."), false);

            // Check if the other player has also accepted the trade
            TradeOffer otherOffer = tradeOffers.get(otherPlayer);
            if (otherOffer != null && otherOffer.isAccepted()) {
                // If both players have accepted, complete the trade
                completeTrade(offer);
            }
        } else {
            player.sendMessage(Text.literal("No trade offer to accept."), false);
        }
    }

    public static void cancelTrade(PlayerEntity player) {
        TradeOffer offer = tradeOffers.remove(player);
        if (offer != null) {
            PlayerEntity otherPlayer = offer.getOtherPlayer();
            tradeOffers.remove(otherPlayer);
            player.sendMessage(Text.literal("Trade canceled."), false);
            otherPlayer.sendMessage(Text.literal(player.getName().getString() + " canceled the trade."), false);
        } else {
            player.sendMessage(Text.literal("No trade offer to cancel."), false);
        }
    }

    public static void offerItem(PlayerEntity player, ItemStack item, int amount) {
        TradeOffer offer = tradeOffers.get(player);
        if (offer != null) {
            ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);

            if (heldItem.isEmpty() || heldItem.getCount() < amount ) {
                player.sendMessage(Text.literal("You do not have the required item in your main hand to offer."), false);
                return;
            }

            ItemStack itemStack = heldItem.copy(); // Create a copy to avoid modifying the player's held item directly
            itemStack.setCount(amount);

            // Add the item to the trade offer
            offer.addOffer(itemStack, amount);

            // Send messages to players
            player.sendMessage(Text.literal("Offered " + amount + " of " + itemStack.getName().getString()).append(itemStack.toHoverableText()));
            offer.getOtherPlayer().sendMessage(Text.literal(player.getName().getString() + " offered " + amount + " of " + itemStack.getName().getString()).append(itemStack.toHoverableText()));
        } else {
            player.sendMessage(Text.literal("No active trade to offer items."), false);
        }
    }



    private static void completeTrade(TradeOffer offer) {
        PlayerEntity player1 = offer.getPlayer();
        PlayerEntity player2 = offer.getOtherPlayer();

        if (!offer.hasRequiredItems()) {
            player1.sendMessage(Text.literal("Trade failed. One of the players no longer has the required items."), false);
            player2.sendMessage(Text.literal("Trade failed. One of the players no longer has the required items."), false);
            cancelTrade(player1);
            return;
        }

        // Transfer items between players
        transferItems(player1, offer.getOffer(player1), player2);
        transferItems(player2, offer.getOffer(player2), player1);

        // Log trade history
        logTradeHistory(player1, player2, offer);

        // Notify players
        player1.sendMessage(Text.literal("Trade completed with " + player2.getName().getString()), false);
        player2.sendMessage(Text.literal("Trade completed with " + player1.getName().getString()), false);

        // Clean up
        tradeOffers.remove(player1);
        tradeOffers.remove(player2);
    }

    private static void transferItems(PlayerEntity fromPlayer, Map<ItemStack, Integer> offers, PlayerEntity toPlayer) {
        for (Map.Entry<ItemStack, Integer> entry : offers.entrySet()) {
            ItemStack itemStack = entry.getKey();
            int amount = entry.getValue();
            ItemStack transferStack = new ItemStack(itemStack.getItem(), amount);

            // Remove items from the offering player
            fromPlayer.getInventory().removeStack(fromPlayer.getInventory().selectedSlot, transferStack.getCount());

            // Add items to the receiving player
            toPlayer.getInventory().offerOrDrop(transferStack);
        }
    }

    private static void logTradeHistory(PlayerEntity player1, PlayerEntity player2, TradeOffer offer) {
        StringBuilder history1 = tradeHistories.computeIfAbsent(player1, k -> new StringBuilder());
        StringBuilder history2 = tradeHistories.computeIfAbsent(player2, k -> new StringBuilder());

        history1.append("Traded with ").append(player2.getName().getString()).append(": ");
        history2.append("Traded with ").append(player1.getName().getString()).append(": ");

        offer.getOffer(player1).forEach((item, amount) -> {
            history1.append(amount).append(" of ").append(item.getName().getString()).append(", ");
        });
        offer.getOffer(player2).forEach((item, amount) -> {
            history2.append(amount).append(" of ").append(item.getName().getString()).append(", ");
        });

        history1.append("\n");
        history2.append("\n");
    }

    public static String getTradeHistory(PlayerEntity player) {
        return tradeHistories.getOrDefault(player, new StringBuilder("No trade history available.")).toString();
    }
}
