package dzwdz.building_frenzy.modes;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;

public abstract class BuildMode {
    public boolean overrideVanilla() {
        return true;
    }

    public void render(WorldRenderContext context, ClientPlayerEntity ply) {}

    public void finalize(Vec3d origin, Vec3d player, Vec3d direction, Item item) {}
}
