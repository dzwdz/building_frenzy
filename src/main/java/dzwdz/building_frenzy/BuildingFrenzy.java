package dzwdz.building_frenzy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class BuildingFrenzy implements ClientModInitializer {
    private static KeyBinding originBind;

    public static BlockPos origin;

    // temporary
    private static boolean wasPressed = false;

    @Override
    public void onInitializeClient() {
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(new Renderer());
        originBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.building_frenzy.origin",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "category.building_frenzy"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            boolean isPressed = originBind.isPressed();
            if (!wasPressed && isPressed) {
                origin = null;
                HitResult hit = client.player.raycast(32, client.getTickDelta(), true);
                if (hit == null) return;
                if (hit.getType() != HitResult.Type.BLOCK) return;
                BlockHitResult bhit = (BlockHitResult) hit;
                origin = bhit.getBlockPos().offset(bhit.getSide());
            }
            if (wasPressed && !isPressed) {
                if (origin == null) return;
                Vec3d v = MathStuff.axisIntersection(MathStuff.BlockPosToVec(origin),
                            client.player.getCameraPosVec(client.getTickDelta()),
                            client.player.getRotationVecClient());
                BlockPos pos = new BlockPos(v);
                Item active = client.player.getMainHandStack().getItem();
                String cmd = "/fill "
                            + origin.getX() + " " + origin.getY() + " " + origin.getZ() + " "
                            + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " "
                            + active.toString();
                System.out.println(cmd);
                client.player.sendChatMessage(cmd);
                origin = null;
            }
            wasPressed = isPressed;
        });
    }
}
