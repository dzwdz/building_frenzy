package dzwdz.building_frenzy;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;

public class Renderer implements WorldRenderEvents.DebugRender {
    @Override
    public void beforeDebugRender(WorldRenderContext context) {
        Entity ent = context.camera().getFocusedEntity();
        if (!(ent instanceof ClientPlayerEntity)) return;
        ClientPlayerEntity ply = (ClientPlayerEntity) ent;

        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();

        BuildingFrenzy.active_mode.render(context, ply);

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }
}
