package net.drk.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.drk.number.NumberFormatter;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SellItemCommand {

    // Auction storage
    private static final Map<UUID, Auction> activeAuctions = new HashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .then(CommandManager.literal("auction")
                                .then(CommandManager.argument("price", IntegerArgumentType.integer())
                                        .executes(SellItemCommand::startAuction)
                                )
                                .then(CommandManager.literal("private")
                                        .then(CommandManager.argument("player", StringArgumentType.word())
                                                .then(CommandManager.argument("price", IntegerArgumentType.integer())
                                                        .executes(SellItemCommand::sellItemToPlayer)
                                                )
                                        )
                                )
                        )
        );
    }

    private static int startAuction(CommandContext<ServerCommandSource> context) {
        int price = IntegerArgumentType.getInteger(context, "price");
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity seller = source.getPlayer();
        ItemStack itemStack = seller.getMainHandStack();

        if (itemStack.isEmpty()) {
            context.getSource().sendFeedback(() -> Text.literal("You must be holding an item to auction it!"), false);
            return Command.SINGLE_SUCCESS;
        }

        UUID auctionId = seller.getUuid();
        Auction auction = new Auction(seller, itemStack, BigInteger.valueOf(price));

        // Store the auction
        activeAuctions.put(auctionId, auction);

        String formattedPrice = NumberFormatter.formatPrice(BigInteger.valueOf(price));

        Text message = Text.literal("Auctioning ")
                .append(itemStack.toHoverableText())
                .append(Text.literal(" starting at ").append(Text.literal(formattedPrice).formatted(Formatting.GREEN)).append(Text.literal(" Gems.")));
        seller.sendMessage(message, false);

        // Schedule auction end
        scheduler.schedule(() -> endAuction(auctionId), 60, TimeUnit.SECONDS);

        return Command.SINGLE_SUCCESS;
    }

    public static void endAuction(UUID auctionId) { // Changed access to public
        Auction auction = activeAuctions.remove(auctionId);

        if (auction != null) {
            auction.endAuction();  // Mark the auction as ended

            ServerPlayerEntity highestBidder = auction.getHighestBidder();
            ServerPlayerEntity seller = auction.getSeller();
            ItemStack itemStack = auction.getItemStack();
            BigInteger highestBid = auction.getHighestBid();

            if (highestBidder != null) {
                // Transfer the item to the highest bidder and currency to the seller
                highestBidder.getInventory().insertStack(itemStack.copy());
                seller.getInventory().removeStack(seller.getInventory().indexOf(itemStack), itemStack.getCount());

                addGems(seller, highestBid);

                seller.sendMessage(Text.literal("Your auction has ended. ").append(itemStack.toHoverableText()).append(" sold for ").append(Text.literal(NumberFormatter.formatPrice(highestBid)).formatted(Formatting.GREEN)).append(" Gems."), false);
                highestBidder.sendMessage(Text.literal("You won the auction for ").append(itemStack.toHoverableText()).append(" with a bid of ").append(Text.literal(NumberFormatter.formatPrice(highestBid)).formatted(Formatting.GREEN)).append(" Gems."), false);
            } else {
                seller.sendMessage(Text.literal("Your auction has ended with no bids. ").append(itemStack.toHoverableText()).append(" has been returned to you."), false);
            }
        }
    }

    private static int sellItemToPlayer(CommandContext<ServerCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "player");
        int price = IntegerArgumentType.getInteger(context, "price");
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity seller = source.getPlayer();
        ServerPlayerEntity buyer = context.getSource().getServer().getPlayerManager().getPlayer(playerName);
        ItemStack itemStack = seller.getMainHandStack();

        if (itemStack.isEmpty()) {
            context.getSource().sendFeedback(() -> Text.literal("You must be holding an item to sell it!"), false);
            return Command.SINGLE_SUCCESS;
        }

        if (buyer == null) {
            seller.sendMessage(Text.literal("Player not found!").formatted(Formatting.RED), false);
            return Command.SINGLE_SUCCESS;
        }

        UUID auctionId = buyer.getUuid();
        Auction auction = new Auction(seller, itemStack, BigInteger.valueOf(price));
        activeAuctions.put(auctionId, auction);

        String formattedPrice = NumberFormatter.formatPrice(BigInteger.valueOf(price));
        seller.sendMessage(Text.literal("Offering ").append(itemStack.toHoverableText()).append(" to ").append(Text.literal(playerName)).append(" for ").append(Text.literal(formattedPrice).formatted(Formatting.GREEN)).append(" Gems."), false);
        buyer.sendMessage(Text.literal("You have been offered ").append(itemStack.toHoverableText()).append(" for ").append(Text.literal(formattedPrice).formatted(Formatting.GREEN)).append(" Gems. Type /Shatterpoint auction accept to buy."), false);

        return Command.SINGLE_SUCCESS;
    }

    public static Auction getAuction(UUID auctionId) {
        return activeAuctions.get(auctionId);
    }

    public static boolean hasEnoughGems(ServerPlayerEntity player, BigInteger amount) {
        // Implement this method to check if the player has enough Gems
        return true;
    }

    public static void deductGems(ServerPlayerEntity player, BigInteger amount) {
        // Implement this method to deduct Gems from the player
    }

    public static void addGems(ServerPlayerEntity player, BigInteger amount) {
        // Implement this method to add Gems to the player
    }
}
