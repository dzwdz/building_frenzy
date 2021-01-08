package dzwdz.building_frenzy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

public class BuildingFrenzy implements ClientModInitializer {
    private static KeyBinding originBind;

    public static BlockPos origin;

    @Override
    public void onInitializeClient() {
        System.out.println("test");

        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(new Renderer());
        originBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.building_frenzy.origin",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "category.building_frenzy"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (originBind.wasPressed()) {
                origin = null;
                if (client.player == null) return;
                HitResult hit = client.player.raycast(32, client.getTickDelta(), true);
                if (hit == null) return;
                if (hit.getType() != HitResult.Type.BLOCK) return;
                BlockHitResult bhit = (BlockHitResult) hit;
                origin = bhit.getBlockPos().offset(bhit.getSide());
            }
        });
    }
}
