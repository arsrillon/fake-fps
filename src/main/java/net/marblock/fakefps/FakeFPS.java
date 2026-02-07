package net.marblock.fakefps;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import static net.minecraft.server.command.CommandManager.*;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.text.Text;

public class FakeFPS implements ModInitializer {
    public static final String MOD_ID = "fakefps";

    public static int fakeFpsAmount = 1000;

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("fakefps")
                    .then(argument("amount", IntegerArgumentType.integer())
                            .executes(context -> {
                                int value = IntegerArgumentType.getInteger(context, "amount");
                                fakeFpsAmount = value;
                                context.getSource().sendFeedback(() -> Text.literal("Fake FPS set to: " + value), false);
                                return 1;
                            })
                    )
                    .then(literal("off")
                            .executes(context -> {
                                fakeFpsAmount = 0;
                                context.getSource().sendFeedback(() -> Text.literal("Fake FPS disabled."), false);
                                return 1;
                            })
                    )
            );
        });
    }
}