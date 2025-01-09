package net.drk;

import net.drk.A.ShieldDataPayload;
import net.drk.A.ShieldNetworking;
import net.drk.A.ShieldTestCommand;
import net.drk.AI.AIChatbot;
import net.drk.AI.ChatbotCommand;
import net.drk.GUI.HUD.BARS;
import net.drk.Z.SplashScreen;
import net.drk.Z28.*;

import net.drk.Z28.d.*;
import net.drk.Z28.rb.RainbowTextHandler;
import net.drk.Z28.rb.TextParticleCommand;
import net.drk.block.ModBlocks;
import net.drk.block.entities.ModBlockEntities;
import net.drk.block.entities.ModBlockEntityTypes;
import net.drk.command.*;
import net.drk.command.Settings.OvershieldSettingsCommand;
import net.drk.command.lumin.ShowLuminCommand;
import net.drk.command.player.SpawnFakePlayerCommand;
import net.drk.command.shield.ShieldCommand;
import net.drk.command.shield.ShowShieldCommand;
import net.drk.command.shield.max.MaxShieldColorCommand;
import net.drk.discord.DiscordPresenceTicker;
import net.drk.discord.DiscordRichPresenceManager;
import net.drk.discord.PresenceCommand;
import net.drk.encryption.item.ITEMBase64Decoder;
import net.drk.encryption.item.ITEMBase64Encoder;
import net.drk.encryption.model.MODELBase64Decoder;
import net.drk.entity.ModEntities;
import net.drk.entity.custom.DodoEntity;
import net.drk.entity.custom.GrimDragonEntity;
import net.drk.entity.custom.PlayerEntity;
import net.drk.entity.vfx.custom.GroundFlame;
import net.drk.event.*;
import net.drk.hotbar.CustomInventoryHud;
import net.drk.item.ModItemGroups;
import net.drk.item.ModItems;
import net.drk.item.other.abilities.BloodfireSpearAbilities;
import net.drk.item.other.weapons.BloodfireSpear;
import net.drk.network.CustomC2SNetworking;
import net.drk.network.NetworkPayloads;
import net.drk.network.lumin.LuminNetworking;
import net.drk.shatterbound.ShatterboundCommand;
import net.drk.summoning.command.*;
import net.drk.util.IEntityDataSaver;
import net.drk.worldgen.BlockRegenerationHandler;
import net.drk.zodiac.ChooseZodiacCommand;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.drk.effect.ModEffects;
import net.drk.particle.custom.ModParticles;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;


import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static net.drk.command.FriendListManager.server;


public class DarkMod implements ModInitializer {
    public static final String MOD_ID = "drkmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    public DarkMod() {
        super();
    }

    @Override
    public void onInitialize() {



    BloodfireSpearAbilities.registerAbilities();







        BlockRegenerationHandler handler = new BlockRegenerationHandler(10); // Proximity radius set to 10 blocks

// Initialize regeneration handler on server start
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            System.out.println("[BlockRegenerationHandler] Server started, initializing regeneration handler."); // Clearer debug log
            ServerWorld overworld = server.getWorld(World.OVERWORLD);
            if (overworld != null) {
                handler.initialize(overworld); // Ensure overworld exists before initializing
            } else {
                System.err.println("[BlockRegenerationHandler] ERROR: Overworld not found during initialization!");
            }
        });

// Tick event, optimized to avoid unnecessary casting
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world instanceof ServerWorld serverWorld) {
                handler.tick(serverWorld);
            }
        });




            LOGGER.info("[SHATTERPOINT] Decoded Assets");

        ShieldNetworking.registerNetworking();
        LuminNetworking.registerNetworking();

        NetworkPayloads.initialize();
        CustomC2SNetworking.initialize();





        //ServerPlayNetworking.registerGlobalReceiver(SummonEntityPacket.IDENTIFIER, SummonEntityPacket::handlePacket);

        LOGGER.info("[SHATTERPOINT] Decoded Assets");





        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            NbtCompound oldData = ((IEntityDataSaver) oldPlayer).getPersistentData();
            ((IEntityDataSaver) newPlayer).getPersistentData().copyFrom(oldData);
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            // Save persistent data when the server stops
            server.getPlayerManager().getPlayerList().forEach(player -> {
                NbtCompound data = ((IEntityDataSaver) player).getPersistentData();
                player.writeCustomDataToNbt(data);
            });
        });





        ChatMessageListener.register();  // Register the chat listener




        // Listen for ticks after the world is loaded





        // Weapon Abilities

        LOGGER.info("Loading Shatterpoint...");

        ModEntities.registerModEntities();

        LOGGER.info("Registering renderer for DodoEntity...");
        FabricDefaultAttributeRegistry.register(ModEntities.DODO, DodoEntity.createDodoAttributes());
        LOGGER.info("Successfully registered DodoEntity renderer");
        LOGGER.info("Registering renderer for PLAYERENTITY...");
        FabricDefaultAttributeRegistry.register(ModEntities.PLAYER, PlayerEntity.createPlayerAttributes());
        LOGGER.info("Successfully registered PLAYERENTITY renderer");
        LOGGER.info("Registering renderer for GRIMDRAGON...");
        FabricDefaultAttributeRegistry.register(ModEntities.GRIM_DRAGON, GrimDragonEntity.createGrimDragonAttributes());
        LOGGER.info("Successfully registered GRIMDRAGON renderer");
        FabricDefaultAttributeRegistry.register(ModEntities.GROUND_FLAME, GroundFlame.createGroundFlameAttributes());
        LOGGER.info("Successfully registered GroundFlame renderer");













        CommandRegistrationCallback.EVENT.register(SetHomeCommand::register);
        CommandRegistrationCallback.EVENT.register(ReturnHomeCommand::register);
        CommandRegistrationCallback.EVENT.register(FunCommands::register);
        CommandRegistrationCallback.EVENT.register(ScoreCommand::register);
        CommandRegistrationCallback.EVENT.register(AddFriendCommand::register);
        CommandRegistrationCallback.EVENT.register(RemoveFriendCommand::register);
        CommandRegistrationCallback.EVENT.register(AcceptFriendCommand::register);
        CommandRegistrationCallback.EVENT.register(RequestFriendCommand::register);
        CommandRegistrationCallback.EVENT.register(ListFriendsCommand::register);
        CommandRegistrationCallback.EVENT.register(SupportCenterCommandFeedback::register);
        CommandRegistrationCallback.EVENT.register(AdminFeedbackCommand::register);
        CommandRegistrationCallback.EVENT.register(FilterFeedbackCommand::register);
        CommandRegistrationCallback.EVENT.register(ShowShieldCommand::register);
        CommandRegistrationCallback.EVENT.register(ShowLuminCommand::register);
        CommandRegistrationCallback.EVENT.register(ShowCurrencyCommand::register);









        PowerManager powerManager = new PowerManager();
        new PowerBasedDamageEventListener(powerManager);
        AttackEventListener.register();


        CommandRegistrationCallback.EVENT.register(TestCommand::register);


        ServerPlayerEvents.COPY_FROM.register(new PlayerCopyHandler());


        LOGGER.info("[SHATTERPOINT] Registering item Groups...");
        ModItemGroups.registerItemGroups();
        LOGGER.info("[SHATTERPOINT] Item Groups registered successfully.");

        LOGGER.info("[SHATTERPOINT] Registering items...");
        ModItems.initialize();
        LOGGER.info("[SHATTERPOINT] Items registered successfully.");

        LOGGER.info("[SHATTERPOINT] Registering Effects...");
        ModEffects.registerEffects();
        LOGGER.info("[SHATTERPOINT] Effects registered successfully.");

        LOGGER.info("[SHATTERPOINT] Registering Particles...");
        ModParticles.registerParticles();
        LOGGER.info("[SHATTERPOINT] Particles registered successfully.");

        LOGGER.info("[SHATTERPOINT] Registering blocks...");
        ModBlocks.registerModBlocks();
        ModBlockEntities.registerBlockEntities();
        ModBlockEntityTypes.initialize();

        LOGGER.info("[SHATTERPOINT] Blocks registered successfully.");

        LOGGER.info("[SHATTERPOINT] Registering MoreBlocks...");


        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            AdminSupportCenterReportCommand.register(dispatcher, registryAccess, environment);
            AdminFeedbackReplyCommand.register(dispatcher, registryAccess, environment);
            PlayerFeedbackCommand.register(dispatcher, registryAccess, environment);
            SpawnFakePlayerCommand.register(dispatcher);
            ShatterboundCommand.register(dispatcher);


            ShieldCommand.register(dispatcher);

            GetLootCommand.register(dispatcher);

            SupportCenterReportCommand.register(dispatcher);
            SellItemCommand.register(dispatcher);
            GiveLuminCommand.register(dispatcher);
            AddLuminCommand.register(dispatcher);
            CheckBalanceCommand.register(dispatcher);
            TradeCommand.register(dispatcher);
            HelpCommand.register(dispatcher);
            BuyItemCommand.register(dispatcher);
            AddScoreCommand.register(dispatcher);
            TagCommand.register(dispatcher);
            HungerListener.register();
            AIChatbot chatbot = new AIChatbot();
            ChatbotCommand.register(dispatcher);
            FilterplayerFeedbackCommand.register(dispatcher);
            PowerCommand.register(dispatcher, powerManager);
            BattleTracker.initialize();
            RetrieveCommand.register(dispatcher, registryAccess, environment);
            FilterRetrieveCommand.register(dispatcher, registryAccess, environment);
            SetCurrencyCommand.register(dispatcher);
            RetrievedItemsCommand.register(dispatcher, registryAccess, environment);
            ModifyAlphaCommand.register(dispatcher);
            AddEffectCommand.register(dispatcher);
            ChangeNicknameCommand.register(dispatcher);
            ChooseZodiacCommand.register(dispatcher);
            PresenceCommand.register(dispatcher, registryAccess, environment);
            ShieldTestCommand.register(dispatcher);


            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                TextParticleCommand.register(dispatcher);
            }




            ServerLifecycleEvents.SERVER_STARTED.register(server -> {
                FriendListManager.setServer(server);
                OvershieldSettingsCommand.register(dispatcher);
                MaxShieldColorCommand.register(dispatcher);

            });
        });


        LOGGER.info("DarkMod initialization complete.");





    };


    private void decodeAssets() {
        ITEMBase64Decoder itemDecoder = new ITEMBase64Decoder();
        itemDecoder.decodeDirectory("C:\\Users\\JTyle\\Downloads\\dark-mod-template-1.21\\src\\main\\resources\\encrypted\\textures\\item", "C:\\Users\\JTyle\\Downloads\\dark-mod-template-1.21\\src\\main\\resources\\assets\\drkmod\\textures\\item");

        // Decode models
        MODELBase64Decoder decoder = new MODELBase64Decoder();
        decoder.decodeDirectory("C:\\Users\\JTyle\\Downloads\\dark-mod-template-1.21\\src\\main\\resources\\encrypted\\models\\item", "C:\\Users\\JTyle\\Downloads\\dark-mod-template-1.21\\src\\main\\resources\\assets\\drkmod\\models\\item");

        // Decode textures

        LOGGER.info("Assets decoded successfully.");
    }
    private void encodeAssets() {
        // Encode models
        model.MODELBase64Encoder encoder = new model.MODELBase64Encoder();
        encoder.encodeDirectory("C:\\Users\\JTyle\\Downloads\\dark-mod-template-1.21\\src\\main\\resources\\assets\\drkmod\\models\\item", "C:\\Users\\JTyle\\Downloads\\dark-mod-template-1.21\\src\\main\\resources\\encrypted\\models\\item");


        // Encode textures
        ITEMBase64Encoder itemEncoder = new ITEMBase64Encoder();
        itemEncoder.encodeDirectory("C:\\Users\\JTyle\\Downloads\\dark-mod-template-1.21\\src\\main\\resources\\assets\\drkmod\\textures\\item", "C:\\Users\\JTyle\\Downloads\\dark-mod-template-1.21\\src\\main\\resources\\encrypted\\textures\\item");

        LOGGER.info("Assets encoded successfully.");
    }
    public static Identifier identifierOf(String name) {
        return Identifier.of(MOD_ID, name);

    }

}
