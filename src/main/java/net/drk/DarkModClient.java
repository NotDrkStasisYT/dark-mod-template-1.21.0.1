package net.drk;

import net.drk.A.ShieldNetworking;
import net.drk.GUI.HUD.BARS;
import net.drk.Z.SplashScreen;
import net.drk.Z.Z;
import net.drk.Z28.d.DamageNumbers;
import net.drk.Z28.d.TextParticle;
import net.drk.block.ModBlocks;
import net.drk.block.advanced.renderer.*;
import net.drk.block.entities.ModBlockEntities;
import net.drk.block.voxel.logs.blockEntityRenderer.*;
import net.drk.block.voxel.logs.blockEntityRenderer.OakLogBlockEntityRenderer;
import net.drk.block.voxel.tavern.blockEntityRenderer.TavernKegBlockEntityRenderer;
import net.drk.block.voxel.tavern.blockEntityRenderer.TavernKegStationBlockEntityRenderer;
import net.drk.discord.DiscordPresenceTicker;
import net.drk.discord.DiscordRichPresenceManager;
import net.drk.entity.ModEntities;
import net.drk.entity.client.*;
import net.drk.entity.vfx.client.GroundFlameModel;
import net.drk.entity.vfx.client.GroundFlameRenderer;
import net.drk.hotbar.CustomInventoryHud;
import net.drk.network.lumin.LuminNetworking;
import net.drk.particle.custom.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DarkModClient implements ClientModInitializer {



    private static KeyBinding openZScreenKey;
    private DiscordPresenceTicker presenceTicker;
    private boolean hasLoaded = false;


    // Declare keybindings and variables for toggling displays
    private static KeyBinding toggleDisplayKeyBinding;
    private static int currentDisplay = 0; // 0 = shield, 1 = Lumin, 2 = currency
    private static long lastSwitchTime = 0; // Track the last switch time
    private static final long COOLDOWN_TIME = 500; // 2 seconds cooldown in milliseconds

    // Declare the other keybindings
    private static KeyBinding teleportKeyBinding1;
    private static KeyBinding teleportKeyBinding2;
    public static final Logger LOGGER = LoggerFactory.getLogger(DarkModClient.class);



    @Override
    public void onInitializeClient() {

    renderBlocks();





        ShieldNetworking.registerClientNetworking(); // Client-side
        LuminNetworking.registerClientNetworking();





        HudRenderCallback.EVENT.register((context, tickDelta) -> BARS.render(context));
        HudRenderCallback.EVENT.register((context, tickDelta) -> CustomInventoryHud.render(context));

        String playerId = MinecraftClient.getInstance().getSession().getUsername(); // Gets the player's username


        new DamageNumbers();


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null && !hasLoaded) {
                // Show the splash screen
                client.setScreen(new SplashScreen());
                hasLoaded = true; // Ensure we only show it once
            }
        });

        DiscordRichPresenceManager presenceManager = new DiscordRichPresenceManager(playerId);
        presenceTicker = new DiscordPresenceTicker(playerId);
        presenceTicker.start();

        // Add shutdown hook to ensure Discord RPC is properly cleaned up
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            presenceManager.shutdown(); // Clean up the presence manager
            presenceTicker.stop();      // Stop the ticker
        }));
        new Thread(() -> {
            try {
                // Sleep for 5000 milliseconds (5 seconds)
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // Handle interruption
                Thread.currentThread().interrupt();
                LOGGER.warn("Thread was interrupted: " + e.getMessage());
            }
            // Log "hi" after the delay
            LOGGER.info("HELLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLOOOOOOOOOOOOOOOOOO");
            System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");


            LOGGER.info("[SHATTERPOINT] Encoded Assets");
        }).start();      //Customization


// Register the particle factory on the client side
        ParticleFactoryRegistry.getInstance().register(ModParticles.TEXT_PARTICLE, (spriteProvider) ->
                (parameters, world, x, y, z, velocityX, velocityY, velocityZ) ->
                        new TextParticle(world, new Vec3d(x, y, z), new Vec3d(velocityX, velocityY, velocityZ))
        );


        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.DODO, DodoModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.DODO, DodoRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.PLAYER, PlayerModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.PLAYER, PlayerRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.GRIM_DRAGON, GrimDragonModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.GRIM_DRAGON, GrimDragonRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.GROUND_FLAME, GroundFlameModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.GROUND_FLAME, GroundFlameRenderer::new);

        KeyBinding toggleBarKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.shatterpoint.toggle_bar",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.shatterpoint"
        ));

        // Keybindings for teleport and actionbar
        teleportKeyBinding1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.drkmod.teleport1",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Q,
                "category.drkmod"
        ));

        teleportKeyBinding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.drkmod.teleport2",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_E,
                "category.drkmod"
        ));


        // Register the toggle display keybinding
        toggleDisplayKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.drkmod.toggleDisplay",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.drkmod"
        ));


        openZScreenKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.examplemod.open_z_screen",  // The translation key
                InputUtil.Type.KEYSYM,            // The type of input (keyboard)
                GLFW.GLFW_KEY_O,                  // The keycode for 'O'
                "category.examplemod.keybindings" // The category for key bindings
        ));

        // Add a client tick event to check for key presses
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openZScreenKey.wasPressed()) {
                // Open the Z screen when the 'O' key is pressed
                openZScreen(client, new Z());
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleBarKey.wasPressed()) {
                BARS.toggleBar(); // Switch between shield and health bar
            }
        });

  /*    ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().options.hudHidden = true; // Hide all HUD elements
            }
        });*/
        // Client tick event for teleport and actionbar
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (MinecraftClient.getInstance().player != null && teleportKeyBinding1.isPressed() && teleportKeyBinding2.isPressed()) {
                var player = MinecraftClient.getInstance().player;
                Vec3d direction = player.getRotationVec(1.0F);
                player.setPosition(player.getX() + direction.x * 5, player.getY(), player.getZ() + direction.z * 5);
                player.sendMessage(Text.literal("Teleported 5 blocks forward!").formatted(Formatting.AQUA), false);
            }
        });

        // Client tick event for toggling display
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (MinecraftClient.getInstance().player != null && toggleDisplayKeyBinding.isPressed()) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastSwitchTime >= COOLDOWN_TIME) {
                    toggleDisplay();
                    lastSwitchTime = currentTime; // Update the last switch time
                }
            }
            // Always show the currently selected display
            updateDisplay();
        });
        BlockEntityRendererFactories.register(ModBlockEntities.BARREL, BarrelBlockEntityRenderer::new);

        LOGGER.info("DarkModClient initialization complete.");
    }

    // Method to toggle between shield, Lumin, and currency
    private void toggleDisplay() {
        currentDisplay = (currentDisplay + 1) % 3; // Cycle through 0, 1, 2 (shield, Lumin, currency)
    }

    // Method to update the display based on the current selection
    private void updateDisplay() {
        switch (currentDisplay) {
            case 0:
                showShield();
                break;
            case 1:
                showLumin();
                break;
            case 2:
                showCurrency();
                break;
        }
    }

    // Method to show the shield
    private void showShield() {
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            MinecraftClient.getInstance().getNetworkHandler().sendCommand("Shatterpoint display shield");
        } else {
            LOGGER.warn("Network handler is not available. Could not display shield.");
        }
    }

    private void showLumin() {
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            MinecraftClient.getInstance().getNetworkHandler().sendCommand("Shatterpoint display lumin");
        } else {
            LOGGER.warn("Network handler is not available. Could not display Lumin.");
        }
    }

    private void showCurrency() {
        if (MinecraftClient.getInstance().getNetworkHandler() != null) {
            MinecraftClient.getInstance().getNetworkHandler().sendCommand("Shatterpoint display currency");
        } else {
            LOGGER.warn("Network handler is not available. Could not display currency.");
        }
    }

    private void openZScreen(MinecraftClient client, Screen screen) {
        // Opens the screen, make sure it's done on the client thread
        client.execute(() -> client.setScreen(screen));
    }

    private void renderBlocks() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SPRUCE_LOG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OAK_LOG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.JUNGLE_LOG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MANGROVE_LOG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DARK_OAK_LOG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHERRY_LOG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ACACIA_LOG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BIRCH_LOG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.AUTUMN_LOG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MYSTICAL_LOG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHADOW_LOG, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SPIRIT_LOG, RenderLayer.getCutout());






        BlockEntityRendererFactories.register(ModBlockEntities.BARREL, BarrelBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.TAVERN_KEG_STATION, TavernKegStationBlockEntityRenderer::new);

        BlockEntityRendererFactories.register(ModBlockEntities.OAK_LOG, OakLogBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SPRUCE_LOG, SpruceLogBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.JUNGLE_LOG, JungleLogBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.MANGROVE_LOG, MangroveLogBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.DARK_OAK_LOG, DarkOakLogBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.CHERRY_LOG, CherryLogBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.ACACIA_LOG, AcaciaLogBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.BIRCH_LOG, BirchLogBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.TAVERN_KEG, TavernKegBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.AUTUMN_LOG, AutumnLogBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.MYSTICAL_LOG, MysticalLogBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SHADOW_LOG, ShadowLogBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.SPIRIT_LOG, SpiritLogBlockEntityRenderer::new);


    }
}
