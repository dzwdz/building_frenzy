package dzwdz.building_frenzy.modes;

import net.minecraft.client.MinecraftClient;

public class NormalMode extends BuildMode {
    @Override
    public boolean overridesVanilla() {
        return false;
    }

    @Override
    public void clientTick(MinecraftClient client) {}

    @Override
    public boolean onScroll(double amt) {
        return false;
    }
}
