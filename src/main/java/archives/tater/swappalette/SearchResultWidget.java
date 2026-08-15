package archives.tater.swappalette;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;

import java.util.stream.IntStream;

public class SearchResultWidget extends AbstractButton {
    private final ItemStack stack;
    private final Font font;
    private final Player player;
    private final int slotIndex;

    public SearchResultWidget(int x, int y, int width, int height, Font font, Player player, int slotIndex) {
        var stack = player.getInventory().getItem(slotIndex);
        super(x, y, width, height, stack.getStyledHoverName());
        this.player = player;
        this.slotIndex = slotIndex;
        this.stack = stack;
        this.font = font;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        var slots = player.inventoryMenu.slots;
        Minecraft.getInstance().gameMode.handleContainerInput(player.inventoryMenu.containerId, IntStream.range(0, slots.size()).filter(slot -> {
            var slot1 = slots.get(slot);
            return slot1.container == player.getInventory() && slot1.getContainerSlot() == slotIndex;
        }).findAny().orElseThrow(), player.getInventory().getSelectedSlot(), ContainerInput.SWAP, player);
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.item(stack, getX(), getY());
        graphics.itemDecorations(font, stack, getX(), getY());
        graphics.text(font, message, getX() + 18, getY(), 0xffffffff, true);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {

    }
}
