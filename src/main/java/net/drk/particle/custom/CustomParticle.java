package net.drk.particle.custom;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;

public class CustomParticle extends SpriteBillboardParticle {
    protected CustomParticle(World world, double x, double y, double z) {
        super((ClientWorld) world, x, y, z);
        this.velocityX = 0.01;  // Initial velocity
        this.velocityY = 0.02;
        this.velocityZ = 0.01;
    }

    @Override
    public void tick() {
        super.tick();

        // Custom movement logic, like oscillation
        this.velocityX = Math.sin(this.age * 0.1) * 0.05;
        this.velocityZ = Math.cos(this.age * 0.1) * 0.05;

        // Manually update position
        this.move(this.velocityX, this.velocityY, this.velocityZ);

        // Check if the particle should die
        if (this.age++ >= this.maxAge) {
            this.markDead();
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }
}
