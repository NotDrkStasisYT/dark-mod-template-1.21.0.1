package net.drk;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.drk.item.trinket.backpack.SchoolBackpack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class ModKeybinds {
    public static final KeyBinding OPEN_BACKPACK_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.modid.open_backpack", // Translation key for the keybind name
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B, // Default key is 'B'
            "category.modid.keybinds" // Translation key for the category
    ));

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_BACKPACK_KEY.wasPressed()) {
                if (client.player != null) {
                    PlayerEntity player = client.player;
                    Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent(player);

                    if (trinketComponent.isPresent()) {
                        // Check the specific trinket slot for the backpack
                        ItemStack backpackItem = trinketComponent.get()
                                .getEquipped(stack -> stack.getItem() instanceof SchoolBackpack)
                                .stream()
                                .findFirst()
                                .map(pair -> pair.getRight())
                                .orElse(ItemStack.EMPTY);

                        if (!backpackItem.isEmpty()) {
                            ((SchoolBackpack) backpackItem.getItem()).use(client.world, client.player, Hand.MAIN_HAND);
                        }
                    }
                }
            }
        });
    }
}
