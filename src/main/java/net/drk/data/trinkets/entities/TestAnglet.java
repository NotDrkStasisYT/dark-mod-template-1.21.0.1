package net.drk.data.trinkets.entities;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

// Example custom trinket item
public class TestAnglet extends Item implements Trinket {

    public TestAnglet(Item.Settings settings) {
        super(settings);

    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        // Custom logic when the trinket is equipped
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        // Custom logic when the trinket is unequipped
    }

    // Additional Trinket-related methods...
}
