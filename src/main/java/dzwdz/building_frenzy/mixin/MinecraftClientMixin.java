package dzwdz.building_frenzy.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Final public GameOptions options;

    @Redirect(
            method = "Lnet/minecraft/client/MinecraftClient;handleInputEvents()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/options/KeyBinding;isPressed()Z"
            ))
    private boolean isPressedOverride(KeyBinding binding) {
        if (binding == options.keyAttack
                || binding == options.keyUse)
            return false;
        return binding.isPressed();
    }

    @Redirect(
            method = "Lnet/minecraft/client/MinecraftClient;handleInputEvents()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/options/KeyBinding;wasPressed()Z"
            ))
    private boolean wasPressedOverride(KeyBinding binding) {
        if (binding == options.keyAttack
                || binding == options.keyUse)
            return false;
        return binding.wasPressed();
    }
}
