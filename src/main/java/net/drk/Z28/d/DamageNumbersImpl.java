package net.drk.Z28.d;

import net.drk.DarkMod;
import net.drk.number.NumberFormatter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;

public final class DamageNumbersImpl implements DamageNumbersHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(DarkMod.MOD_ID);

    private final Deque<TextParticle> particles = new ArrayDeque<>();

    @Override
    public void onEntityHealthChange(@NotNull LivingEntity entity, float oldHealth, float newHealth) {
        float damage = oldHealth - newHealth;
        LOGGER.info("Damage calculated: " + damage); // Debug logging
        if (damage <= 0.0F) return;

        var client = MinecraftClient.getInstance();
        if (entity == client.player) return; // Skip player damage display

        var world = client.world;
        if (world == null || world != entity.getWorld()) return;
        if (entity.squaredDistanceTo(client.player) > 2304.0) return;

        Vec3d particlePos = entity.getPos().add(0.0, entity.getHeight() + 0.25, 0.0);
        Vec3d particleVelocity = entity.getVelocity();

        // Create a new particle for the damage number
        var particle = new TextParticle(world, particlePos, particleVelocity);

        // Log the raw damage value before formatting
        LOGGER.info("Raw damage before formatting: " + damage);

        String formattedDamage = NumberFormatter.formatDamage(damage);
        LOGGER.info("Formatted Damage: " + formattedDamage); // Debug logging

        particle.setText(formattedDamage);
        particle.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F)); // RGB for white

        particles.add(particle);
        client.particleManager.addParticle(particle);
    }
    public void onShieldChange(@NotNull LivingEntity entity, BigInteger oldShield, BigInteger newShield) {
        // Calculate shield damage as BigInteger
        BigInteger shieldDamage = oldShield.subtract(newShield);
        LOGGER.info("Shield damage calculated: " + shieldDamage); // Debug logging

        // Check if shield damage is positive
        if (shieldDamage.compareTo(BigInteger.ZERO) <= 0) return; // Use compareTo for BigInteger

        var client = MinecraftClient.getInstance();
        if (entity == client.player) return; // Skip player shield display

        var world = client.world;

        // Check if entity is within a certain distance from the player
        if (entity.squaredDistanceTo(client.player) > 2304.0) return;

        // Create and render the damage number for shield damage
        Vec3d particlePos = entity.getPos().add(0.0, entity.getHeight() + 0.25, 0.0);
        Vec3d particleVelocity = entity.getVelocity();

        var particle = new TextParticle(world, particlePos, particleVelocity);

        // Format the shield damage
        String formattedShieldDamage = NumberFormatter.formatDamageB(shieldDamage);

        // Log the raw shield damage value before formatting
        LOGGER.info("Raw shield damage before formatting: " + shieldDamage);
        LOGGER.info("Formatted Shield Damage: " + formattedShieldDamage); // Debug logging
        System.out.println("Formatted Shield Damage: " + formattedShieldDamage); // Debug logging

        // Set the text and color for the particle
        particle.setText(formattedShieldDamage);
        particle.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F)); // RGB for white

        // Add the particle to the client
        particles.add(particle);
        client.particleManager.addParticle(particle);
    }

}