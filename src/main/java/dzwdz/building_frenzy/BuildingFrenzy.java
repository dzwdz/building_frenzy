package dzwdz.building_frenzy;

import dzwdz.building_frenzy.modes.BuildMode;
import dzwdz.building_frenzy.modes.PlaneMode;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class BuildingFrenzy implements ClientModInitializer {
    public static Vec3d origin;

    public static BuildMode active_mode = new PlaneMode();

    // temporary
    private static boolean wasPressed = false;

    @Override
    public void onInitializeClient() {
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(new Renderer());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            boolean isPressed = MinecraftClient.getInstance().options.keyUse.isPressed();
            if (!wasPressed && isPressed) {
                origin = null;
                HitResult hit = client.player.raycast(32, client.getTickDelta(), true);
                if (hit == null) return;
                if (hit.getType() != HitResult.Type.BLOCK) return;
                BlockHitResult bhit = (BlockHitResult) hit;
                origin = MathStuff.BlockPosToVec(bhit.getBlockPos().offset(bhit.getSide()));
            }
            if (wasPressed && !isPressed) {
                if (origin != null) {
                    active_mode.finalize(origin,
                            client.player.getCameraPosVec(client.getTickDelta()),
                            client.player.getRotationVecClient(),
                            client.player.getMainHandStack().getItem());
                    origin = null;
                }
            }
            wasPressed = isPressed;
        });
    }
}
