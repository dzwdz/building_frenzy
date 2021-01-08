package dzwdz.building_frenzy;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Renderer implements WorldRenderEvents.DebugRender {
    @Override
    public void beforeDebugRender(WorldRenderContext context) {
        Entity ent = context.camera().getFocusedEntity();
        if (!(ent instanceof ClientPlayerEntity)) return;
        ClientPlayerEntity ply = (ClientPlayerEntity) ent;

        if (BuildingFrenzy.origin == null) return;

        Vec3d origin = MathStuff.BlockPosToVec(BuildingFrenzy.origin);

        // copied from VillageDebugRenderer
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();

        DebugRenderer.drawBox(BuildingFrenzy.origin, 0f, 1f, .2f, 1f, .5f);

        Vec3d v = MathStuff.axisIntersection(origin, ply.getCameraPosVec(context.tickDelta()), ply.getRotationVecClient());
        DebugRenderer.drawBox(BuildingFrenzy.origin, new BlockPos(v).add(1, 1, 1),  .0f, .0f, 1f, .5f);

        // copied from VillageDebugRenderer
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }
}