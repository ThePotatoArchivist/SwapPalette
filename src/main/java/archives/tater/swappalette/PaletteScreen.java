package archives.tater.swappalette;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import it.unimi.dsi.fastutil.ints.IntList;

@SuppressWarnings("NotNullFieldNotInitialized")
public class PaletteScreen extends Screen {
    public static final int WIDTH = 300;
    private final Player player;
    private EditBox search;
    private IntList resultSlots;

    public PaletteScreen(Component title, Player player) {
        super(title);
        this.player = player;
    }

    @Override
    protected void init() {
        search = addRenderableWidget(new EditBox(font, (width - WIDTH) / 2, height / 2 - 20, WIDTH, 20, Component.empty()));

        var y = height / 2;
        var size = player.getInventory().getContainerSize();
        for (var slot = 0; slot < size; slot++) {
            if (player.getInventory().getItem(slot).isEmpty()) continue;
            addRenderableWidget(new SearchResultWidget((width - WIDTH) / 2, y, WIDTH, 24, font, player, slot));
            y += 24;
        }
    }

    @Override
    protected void setInitialFocus() {
        setInitialFocus(search);
    }

    @Override
    public boolean isInGameUi() {
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        return super.keyPressed(event);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        minecraft.gui.hud.extractDeferredSubtitles();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

    }
}
