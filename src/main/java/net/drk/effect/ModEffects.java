package net.drk.effect;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


    public class ModEffects implements ModInitializer {
        public static final StatusEffect TATER_EFFECT;



        static {
            TATER_EFFECT = Registry.register(Registries.STATUS_EFFECT, Identifier.of("drkmod", "tater"), new TaterEffect());
        }

        public static void registerEffects() {
        }

        @Override
        public void onInitialize() {
            // ...
        }
    }

