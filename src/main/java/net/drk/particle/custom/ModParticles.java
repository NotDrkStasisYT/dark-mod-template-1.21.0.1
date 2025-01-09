package net.drk.particle.custom;

import net.drk.DarkMod;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {
    public static final SimpleParticleType SPARKLE_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType TEXT_PARTICLE = FabricParticleTypes.simple();

    public static final String MOD_ID = "drkmod";

    public static void registerParticles() {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "sparkle_particle"), SPARKLE_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "text_particle"), TEXT_PARTICLE);


    }
}
