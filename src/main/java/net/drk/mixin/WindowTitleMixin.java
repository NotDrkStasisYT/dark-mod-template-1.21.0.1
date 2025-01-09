package net.drk.mixin;

import net.drk.ShatterpointInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class WindowTitleMixin {
    @Shadow
    private Window window;

    // Inject at the beginning of the updateWindowTitle method
    @Inject(method = "updateWindowTitle", at = @At("HEAD"), cancellable = true)
    public void overrideWindowTitle(CallbackInfo info) {
        // Set the window title to "Shatterpoint <version>"
        String title = ShatterpointInfo.NAME + " " + ShatterpointInfo.VERSION;
        window.setTitle(title); // Set the new title
        info.cancel(); // Prevent the original method from running
    }
}
