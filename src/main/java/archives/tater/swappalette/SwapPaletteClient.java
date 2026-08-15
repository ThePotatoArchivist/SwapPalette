package archives.tater.swappalette;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;

public class SwapPaletteClient implements ClientModInitializer {
    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(SwapPalette.id("swap_palette"));

    public static final KeyMapping OPEN_PALETTE = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.swap_palette.open_palette",
            InputConstants.KEY_SEMICOLON,
            CATEGORY
    ));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_PALETTE.consumeClick()) {
                if (client.player == null) continue;
                client.gui.setScreen(new PaletteScreen(Component.literal("Search Inventory"), client.player));
            }
        });
    }
}
