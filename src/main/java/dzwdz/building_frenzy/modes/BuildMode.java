package dzwdz.building_frenzy.modes;

import dzwdz.building_frenzy.MathStuff;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.GameOptions;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public abstract class BuildMode {
    public Vec3d origin = null;
    private boolean wasPressed; // still temporary

    public boolean overrideVanilla() {
        return true;
    }

    public void render(WorldRenderContext context, ClientPlayerEntity ply) {}

    public void finalize(Vec3d origin, Vec3d player, Vec3d direction, Item item) {}

    public void clientTick(MinecraftClient client) {
        if (client.player == null) return;
        boolean isPressed = client.options.keyUse.isPressed();
        if (!wasPressed && isPressed) {
            origin = null;
            HitResult hit = client.player.raycast(64, client.getTickDelta(), true);
            if (hit == null) return;
            if (hit.getType() != HitResult.Type.BLOCK) return;
            BlockHitResult bhit = (BlockHitResult) hit;
            origin = MathStuff.BlockPosToVec(bhit.getBlockPos().offset(bhit.getSide()));
        }
        if (wasPressed && !isPressed) {
            if (origin != null) {
                finalize(origin,
                        client.player.getCameraPosVec(client.getTickDelta()),
                        client.player.getRotationVecClient(),
                        client.player.getMainHandStack().getItem());
                origin = null;
            }
        }
        wasPressed = isPressed;
    }

    public void resetState() {
        origin = null;
        wasPressed = false;
        GameOptions options = MinecraftClient.getInstance().options;

        // avoid ghost clicks
        while (options.keyUse.wasPressed());
        while (options.keyAttack.wasPressed());
    }
}
