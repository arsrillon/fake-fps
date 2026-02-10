package net.marblock.fakefps.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.debug.DebugHudLines;
import net.minecraft.client.gui.hud.debug.FpsDebugHudEntry;
import net.minecraft.client.option.GameOptions;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import static net.marblock.fakefps.FakeFPS.fakeFpsAmount;

@Mixin(FpsDebugHudEntry.class)
public class FpsMixin {

    @Unique
    private long lastUpdateTime = 0;
    @Unique
    private int cachedFakeFps = 0;

    /**
     * @author marblock
     * @reason Fake FPS implementation
     */
    @Overwrite
    public void render(DebugHudLines lines, @Nullable World world, @Nullable WorldChunk clientChunk, @Nullable WorldChunk chunk) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        int i = minecraftClient.getInactivityFpsLimiter().update();
        GameOptions gameOptions = minecraftClient.options;

        int targetFps = fakeFpsAmount;
        int displayFps;

        if (targetFps > 0) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastUpdateTime > 250) {
                int range = Math.max(1, targetFps / 20);
                int offset = ThreadLocalRandom.current().nextInt(-range, range + 1);

                cachedFakeFps = targetFps + offset;
                lastUpdateTime = currentTime;
            }
            displayFps = cachedFakeFps;
        } else {
            displayFps = minecraftClient.getCurrentFps();
        }

        String fpsString = String.format(Locale.ROOT, "%d fps T: %s%s",
                displayFps,
                i == 260 ? "inf" : i,
                gameOptions.getEnableVsync().getValue() ? " vsync" : ""
        );

        lines.addPriorityLine(fpsString);
    }
}