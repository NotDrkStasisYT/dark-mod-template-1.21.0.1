package net.drk;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.impl.event.interaction.FakePlayerNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;
import java.util.UUID;

public class FakePlayer extends ServerPlayerEntity {

    public static final UUID DEFAULT_UUID = UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77");
    private static final GameProfile DEFAULT_PROFILE = new GameProfile(DEFAULT_UUID, "[Minecraft FakePlayer]");

    // Constructor
    public FakePlayer(ServerWorld world, GameProfile profile) {
        super(world.getServer(), world, profile, SyncedClientOptions.createDefault());
        this.networkHandler = new FakePlayerNetworkHandler(this);
    }

    // Ensure the FakePlayer can be damaged like a normal player
    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return false;  // Allow all types of damage
    }
    @Override
    public boolean isSpectator() {
        return false;  // The FakePlayer is not a spectator
    }

    @Override
    public boolean isCreative() {
        return false;  // The FakePlayer is not in creative mode
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;  // Check for invulnerability
        }

        // Call the parent method to handle actual damage application
        return super.damage(source, amount);
    }


    @Override
    public void tick() {
        super.tick();  // Let the fake player tick like a real player
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);

        // Remove the player from the world upon death
        this.getWorld().getServer().execute(() -> {
            this.remove(Entity.RemovalReason.KILLED);
        });
    }
}
