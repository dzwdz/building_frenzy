package dzwdz.building_frenzy;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Util {
    // there's probably a builtin method for this but :shrug:
    public static Vec3d BlockPosToVec(BlockPos pos) {
        if (pos == null) return null;
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ()).add(.5, .5, .5);
    }

    // adapted from https://stackoverflow.com/questions/5666222/3d-line-plane-intersection
    public static double lineIntersection(Vec3d planePoint, Vec3d planeNormal, Vec3d linePoint, Vec3d lineDirection) {
        return (planeNormal.dotProduct(planePoint) - planeNormal.dotProduct(linePoint)) / planeNormal.dotProduct(lineDirection.normalize());
    }

    public static Vec3d axisIntersection(Vec3d origin, Vec3d from, Vec3d direction) {
        return axisIntersection(origin, from, direction, 0);
    }

    public static Vec3d axisIntersection(Vec3d origin, Vec3d from, Vec3d direction, double depth) {
        double closest = Double.POSITIVE_INFINITY;
        Vec3d closest_plane = null;

        for (Vec3d plane : ImmutableSet.of(new Vec3d(1, 0, 0), new Vec3d(0, 1, 0), new Vec3d(0, 0, 1),
                new Vec3d(-1, 0, 0), new Vec3d(0, -1, 0), new Vec3d(0, 0, -1))) {
            double t = lineIntersection(origin, plane, from, direction);
            if (1 < t && t < closest) { // todo make the min distance configurable
                closest = t;
                closest_plane = plane;
            }
        }

        assert closest_plane != null;

        return from.add(direction.multiply(closest)).add(
                closest_plane.multiply(direction).normalize().multiply(depth)
        );
    }

    public static BlockHitResult eyeTrace(MinecraftClient client) {
        HitResult hit = client.player.raycast(64, client.getTickDelta(), true);
        if (hit == null || hit.getType() != HitResult.Type.BLOCK) return null;
        return (BlockHitResult) hit;
    }
}
