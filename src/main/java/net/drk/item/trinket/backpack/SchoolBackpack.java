package net.drk.item.trinket.backpack;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class SchoolBackpack extends TrinketItem {
    private static final int INVENTORY_SIZE = 27; // 3x9 inventory slots

    public SchoolBackpack(Item.Settings settings) {
        super(settings);
    }

    @Override
    public Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, Identifier id) {
        Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifiers = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
        EntityAttributeModifier speedModifier = new EntityAttributeModifier(id.withSuffixedPath("school_backpack/movement_speed"),
                0.1, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        modifiers.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, speedModifier);
        return modifiers;
    }

    // Open the backpack inventory when used
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, p) -> new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId, inv, new SimpleInventory(INVENTORY_SIZE), INVENTORY_SIZE / 9),
                    Text.translatable("container.school_backpack")
            ));
        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}