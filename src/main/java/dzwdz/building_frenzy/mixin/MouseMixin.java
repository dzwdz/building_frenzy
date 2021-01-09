package dzwdz.building_frenzy.mixin;

import dzwdz.building_frenzy.BuildingFrenzy;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(
            at = @At("HEAD"),
            method = "onMouseScroll",
            cancellable = true
    )
    private void onMouseScrollHook(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (window == MinecraftClient.getInstance().getWindow().getHandle()) {
            double d = (client.options.discreteMouseScroll ? Math.signum(vertical) : vertical) * client.options.mouseWheelSensitivity;
            if (BuildingFrenzy.active_mode.onScroll(d)) ci.cancel();
        }
    }
}
