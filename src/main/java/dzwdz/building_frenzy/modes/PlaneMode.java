package dzwdz.building_frenzy.modes;

import dzwdz.building_frenzy.MathStuff;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.util.math.MathHelper.floor;

public class PlaneMode extends BuildMode {
    @Override
    public void render(WorldRenderContext context, ClientPlayerEntity ply) {
        if (origin == null) return;

        Vec3d hit = MathStuff.axisIntersection(origin, ply.getCameraPosVec(context.tickDelta()), ply.getRotationVecClient());
        hit = MathStuff.BlockPosToVec(new BlockPos(hit)); // rounding the vector, todo make this less ugly
        Box box = new Box(origin, hit).expand(.5);

        box = box.offset(ply.getCameraPosVec(context.tickDelta()).negate());

        DebugRenderer.drawBox(box, .7f, 0, 1, .5f);
    }

    private String vecString(Vec3d from) {
        return floor(from.getX()) + " " + floor(from.getY()) + " " + floor(from.getZ()) + " ";
    }

    @Override
    public void finalize(Vec3d origin, Vec3d player, Vec3d direction, Item item) {
        Vec3d v = MathStuff.axisIntersection(origin, player, direction);
        String cmd = "/fill " + vecString(origin) + vecString(v) + item.toString();
        //System.out.println(cmd);
        MinecraftClient.getInstance().player.sendChatMessage(cmd);
    }
}
