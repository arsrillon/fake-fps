package net.marblock.fakefps;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.text.Text;

public class FakeFPS implements ClientModInitializer {
    public static final String MOD_ID = "fakefps";
    public static int fakeFpsAmount = 1000;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("fakefps")
                    .then(ClientCommandManager.argument("amount", IntegerArgumentType.integer())
                            .executes(context -> {
                                int value = IntegerArgumentType.getInteger(context, "amount");
                                fakeFpsAmount = value;
                                context.getSource().sendFeedback(Text.literal("§aFake FPS set to: " + value));
                                return 1;
                            })
                    )
                    .then(ClientCommandManager.literal("off")
                            .executes(context -> {
                                fakeFpsAmount = 0;
                                context.getSource().sendFeedback(Text.literal("§cFake FPS disabled."));
                                return 1;
                            })
                    )
            );
        });
    }
}