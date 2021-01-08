package dzwdz.building_frenzy;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
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

        Vec3d hit = MathStuff.axisIntersection(origin, ply.getCameraPosVec(context.tickDelta()), ply.getRotationVecClient());
        hit = MathStuff.BlockPosToVec(new BlockPos(hit)); // rounding the vector, todo make this less ugly
        Box box = new Box(origin, hit).expand(.5);
        box = box.offset(ply.getCameraPosVec(context.tickDelta()).negate());
        DebugRenderer.drawBox(box, 0, 0, 1, .5f);


        // copied from VillageDebugRenderer
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }
}
