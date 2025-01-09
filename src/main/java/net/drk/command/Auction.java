package net.drk.command;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import java.math.BigInteger;

public class Auction {
    private final ServerPlayerEntity seller;
    private final ItemStack itemStack;
    private BigInteger highestBid;
    private ServerPlayerEntity highestBidder;
    private boolean auctionEnded;

    public Auction(ServerPlayerEntity seller, ItemStack itemStack, BigInteger startingBid) {
        this.seller = seller;
        this.itemStack = itemStack;
        this.highestBid = startingBid;
        this.highestBidder = null;
        this.auctionEnded = false;
    }

    public ServerPlayerEntity getSeller() {
        return seller;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public BigInteger getHighestBid() {
        return highestBid;
    }

    public ServerPlayerEntity getHighestBidder() {
        return highestBidder;
    }

    public boolean isAuctionEnded() {
        return auctionEnded;
    }

    public void endAuction() {
        this.auctionEnded = true;
    }

    public void placeBid(ServerPlayerEntity bidder, BigInteger bidAmount) {
        if (bidAmount.compareTo(highestBid) > 0) {
            this.highestBid = bidAmount;
            this.highestBidder = bidder;
        }
    }
}
