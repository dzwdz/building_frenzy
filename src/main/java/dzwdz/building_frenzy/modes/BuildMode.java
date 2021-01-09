package dzwdz.building_frenzy.modes;

import dzwdz.building_frenzy.Util;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;

public abstract class BuildMode {
    public Vec3d origin = null;
    protected KeyBinding currentlyPressed = null;
    protected double depth = 0;

    public boolean overridesVanilla() {
        return true;
    }

    public void render(WorldRenderContext context, ClientPlayerEntity ply) {}

    public void finalize(Vec3d origin, Vec3d player, Vec3d direction, Item item) {}

    public void clientTick(MinecraftClient client) {
        if (client.player == null) return;

        if (currentlyPressed == null) {

            if (client.options.keyAttack.isPressed()) { // block breaking
                origin = null;
                BlockHitResult blockHit = Util.eyeTrace(client);
                if (blockHit != null)
                    origin = Util.BlockPosToVec(blockHit.getBlockPos());
                currentlyPressed = client.options.keyAttack;
                depth = 0;
            } else if (client.options.keyUse.isPressed()) { // block placing
                origin = null;
                BlockHitResult blockHit = Util.eyeTrace(client);
                if (blockHit != null)
                    origin = Util.BlockPosToVec(blockHit.getBlockPos().offset(blockHit.getSide()));
                currentlyPressed = client.options.keyUse;
                depth = 0;
            }

        } else if (!currentlyPressed.isPressed()) { // if the currently held button was released
            if (origin != null) {
                Item item = Items.AIR;
                if (currentlyPressed == client.options.keyUse)
                    item = client.player.getMainHandStack().getItem();

                finalize(origin,
                        client.player.getCameraPosVec(client.getTickDelta()),
                        client.player.getRotationVecClient(),
                        item);
                origin = null;
            }
            currentlyPressed = null;
        }
    }

    public void resetState() {
        origin = null;
        currentlyPressed = null;
        depth = 0;

        // avoid ghost clicks
        GameOptions options = MinecraftClient.getInstance().options;
        while (options.keyUse.wasPressed());
        while (options.keyAttack.wasPressed());
    }

    public boolean onScroll(double amt) {
        if (origin != null) {
            depth += amt;
            return true;
        }
        return false;
    }
}
