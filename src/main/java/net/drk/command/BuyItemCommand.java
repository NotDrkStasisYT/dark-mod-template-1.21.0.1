package net.drk.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.math.BigInteger;
import java.util.UUID;

public class BuyItemCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("Shatterpoint")
                        .then(CommandManager.literal("auction")
                                .then(CommandManager.literal("bid")
                                        .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                                .executes(BuyItemCommand::bidOnItem)
                                        )
                                )
                                .then(CommandManager.literal("accept")
                                        .executes(BuyItemCommand::acceptPrivateAuction)
                                )
                        )
        );
    }

    private static int bidOnItem(CommandContext<ServerCommandSource> context) {
        int amount = IntegerArgumentType.getInteger(context, "amount");
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity bidder = source.getPlayer();
        UUID auctionId = bidder.getUuid(); // Adjust as necessary for your auction ID logic

        Auction auction = SellItemCommand.getAuction(auctionId);
        if (auction == null) {
            bidder.sendMessage(Text.literal("No active auction found!").formatted(Formatting.RED), false);
            return Command.SINGLE_SUCCESS;
        }

        if (auction.isAuctionEnded()) {
            bidder.sendMessage(Text.literal("This auction has ended!").formatted(Formatting.RED), false);
            return Command.SINGLE_SUCCESS;
        }

        BigInteger bidAmount = BigInteger.valueOf(amount);

        if (bidAmount.compareTo(auction.getHighestBid()) > 0) {
            if (!SellItemCommand.hasEnoughGems(bidder, bidAmount)) {
                bidder.sendMessage(Text.literal("You do not have enough Gems!").formatted(Formatting.RED), false);
                return Command.SINGLE_SUCCESS;
            }

            auction.placeBid(bidder, bidAmount);
            SellItemCommand.deductGems(bidder, bidAmount);

            ServerPlayerEntity seller = auction.getSeller();
            seller.sendMessage(Text.literal(bidder.getName().getString() + " has placed a new bid of " + bidAmount + " Gems on your auctioned item.").formatted(Formatting.AQUA), false);
            bidder.sendMessage(Text.literal("You have placed a bid of " + bidAmount + " Gems.").formatted(Formatting.GREEN), false);
        } else {
            bidder.sendMessage(Text.literal("Your bid must be higher than the current bid!").formatted(Formatting.RED), false);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int acceptPrivateAuction(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity buyer = source.getPlayer();
        UUID auctionId = buyer.getUuid(); // Adjust as necessary for your auction ID logic

        Auction auction = SellItemCommand.getAuction(auctionId);
        if (auction == null) {
            buyer.sendMessage(Text.literal("No active private auction found!").formatted(Formatting.RED), false);
            return Command.SINGLE_SUCCESS;
        }

        if (auction.isAuctionEnded()) {
            buyer.sendMessage(Text.literal("This auction has ended!").formatted(Formatting.RED), false);
            return Command.SINGLE_SUCCESS;
        }

        BigInteger price = auction.getHighestBid(); // In private auctions, this is the fixed price

        if (!SellItemCommand.hasEnoughGems(buyer, price)) {
            buyer.sendMessage(Text.literal("You do not have enough Gems to buy this item!").formatted(Formatting.RED), false);
            return Command.SINGLE_SUCCESS;
        }

        // Proceed with the purchase
        ServerPlayerEntity seller = auction.getSeller();
        ItemStack itemStack = auction.getItemStack();

        buyer.getInventory().insertStack(itemStack.copy());
        seller.getInventory().removeStack(seller.getInventory().indexOf(itemStack), itemStack.getCount());

        SellItemCommand.deductGems(buyer, price);
        SellItemCommand.addGems(seller, price);

        seller.sendMessage(Text.literal(buyer.getName().getString() + " has bought your item for " + price + " Gems.").formatted(Formatting.GREEN), false);
        buyer.sendMessage(Text.literal("You have bought the item for " + price + " Gems.").formatted(Formatting.GREEN), false);

        SellItemCommand.endAuction(auctionId);

        return Command.SINGLE_SUCCESS;
    }
}
