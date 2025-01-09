package net.drk;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TradeOffer {
    private final PlayerEntity player;
    private final PlayerEntity otherPlayer;
    private final Map<ItemStack, Integer> offers = new HashMap<>();
    private boolean accepted = false;

    public TradeOffer(PlayerEntity player, PlayerEntity otherPlayer) {
        this.player = player;
        this.otherPlayer = otherPlayer;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public PlayerEntity getOtherPlayer() {
        return otherPlayer;
    }

    public void addOffer(ItemStack item, int amount) {
        offers.put(item, amount);
    }

    public Map<ItemStack, Integer> getOffer(PlayerEntity player) {
        return player.equals(this.player) ? offers : new HashMap<>();
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean hasRequiredItems() {
        for (Map.Entry<ItemStack, Integer> entry : offers.entrySet()) {
            ItemStack offeredItem = entry.getKey();
            int amount = entry.getValue();
            if (player.getInventory().count(offeredItem.getItem()) < amount) {
                return false;
            }
        }
        return true;
    }
}
