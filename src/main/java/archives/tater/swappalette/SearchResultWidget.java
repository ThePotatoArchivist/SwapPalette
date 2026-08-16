package archives.tater.swappalette;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.world.item.ItemStack;

public class SearchResultWidget extends AbstractButton {
    private final boolean selected;
    private final ItemStack stack;
    private final Font font;
    private final Runnable onPress;

    public SearchResultWidget(Font font, int x, int y, int width, int height, boolean selected, ItemStack stack, Runnable onPress) {
        super(x, y, width, height, stack.getStyledHoverName());
        this.selected = selected;
        this.stack = stack;
        this.font = font;
        this.onPress = onPress;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        onPress.run();
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.item(stack, getX(), getY());
        graphics.itemDecorations(font, stack, getX(), getY());
        graphics.text(font, message, getX() + 18 + (selected ? 8 : 0), getY(), 0xffffffff, true);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }
}
