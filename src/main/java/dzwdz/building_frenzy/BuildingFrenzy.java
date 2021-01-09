package dzwdz.building_frenzy;

import com.google.common.collect.ImmutableList;
import dzwdz.building_frenzy.modes.BuildMode;
import dzwdz.building_frenzy.modes.NormalMode;
import dzwdz.building_frenzy.modes.PlaneMode;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class BuildingFrenzy implements ClientModInitializer {
    private static List<BuildMode> modes = ImmutableList.of(new NormalMode(), new PlaneMode());
    private int mode_idx = 0;

    public static KeyBinding mode_switcher;

    public static BuildMode active_mode = new NormalMode();

    @Override
    public void onInitializeClient() {
        mode_switcher = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.building_frenzy.mode",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "category.building_frenzy"
        ));

        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(new Renderer());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (mode_switcher.wasPressed()) {
                mode_idx = (mode_idx + 1) % modes.size();
                if (mode_idx < 0) mode_idx += modes.size();

                active_mode.resetState();
                active_mode = modes.get(mode_idx);

                client.player.sendMessage(new LiteralText(active_mode.getClass().getSimpleName()), true);
            }
            active_mode.clientTick(client);
        });
    }
}
