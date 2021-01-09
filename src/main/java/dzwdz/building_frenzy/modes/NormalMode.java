package dzwdz.building_frenzy.modes;

import net.minecraft.client.MinecraftClient;

public class NormalMode extends BuildMode {
    @Override
    public boolean overrideVanilla() {
        return false;
    }

    @Override
    public void clientTick(MinecraftClient client) {}
}
