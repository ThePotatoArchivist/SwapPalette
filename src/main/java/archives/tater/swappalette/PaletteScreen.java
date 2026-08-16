package archives.tater.swappalette;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;

import it.unimi.dsi.fastutil.ints.IntImmutableList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static archives.tater.swappalette.ModUtil.comparingFloat;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.IntStream.range;

public class PaletteScreen extends Screen {
    public static final int WIDTH = 300;
    private IntList resultSlots = IntList.of();
    private final Player player;
    private final List<@Nullable String> stackNames;
    private String currentSearch = "";
    private int currentSelection = 0;
    @SuppressWarnings("NotNullFieldNotInitialized")
    private EditBox search;

    public PaletteScreen(Component title, Player player) {
        super(title);
        this.player = player;

        var inventory = player.getInventory();
        stackNames = range(0, inventory.getContainerSize()).mapToObj(slot -> {
            var stack = inventory.getItem(slot);
            if (stack.isEmpty()) return null;
            return stack.getHoverName().getString();
        }).toList();
    }

    @Override
    protected void init() {
        search = addRenderableWidget(new EditBox(font, (width - WIDTH) / 2, height / 2 - 20, WIDTH, 20, Component.empty()));
        search.setCanLoseFocus(false);
        search.setValue(currentSearch);
        search.setResponder(this::onSearchChange);

        var y = height / 2;
        for (var i = 0; i < resultSlots.size(); i++) {
            var slot = resultSlots.getInt(i);
            var stack = player.getInventory().getItem(slot);
            addRenderableWidget(new SearchResultWidget(font, (width - WIDTH) / 2, y, WIDTH, 24, i == currentSelection, stack, () -> onSelect(slot)));
            y += 24;
        }
    }

    private void onSearchChange(String s) {
        if (currentSearch.equals(s)) return;
        currentSearch = s;

        record SearchEntry(String name, int slot, float score) {}

        var normSearch = currentSearch.toLowerCase(Locale.ROOT);
        resultSlots = IntImmutableList.toList(range(0, stackNames.size())
                .mapToObj(slot -> {
                    var name = stackNames.get(slot);
                    if (name == null) return null;
                    return new SearchEntry(name, slot, getScore(name, normSearch));
                })
                .filter(Objects::nonNull)
                .sorted(comparingFloat(SearchEntry::score))
                .mapToInt(SearchEntry::slot)
        );
        currentSelection = 0;

        this.rebuildWidgets();
    }

    @SuppressWarnings("deprecation")
    private static float getScore(String name, String normSearch) {
        return StringUtils.getLevenshteinDistance(name, normSearch);
    }

    private void onSelect(int slot) {
        var slots = player.inventoryMenu.slots;
        var menuSlotIndex = range(0, slots.size()).filter(i -> {
            var menuSlot = slots.get(i);
            return menuSlot.container == player.getInventory() && menuSlot.getContainerSlot() == slot;
        }).findAny();
        if (menuSlotIndex.isEmpty()) return;

        Minecraft.getInstance().gameMode.handleContainerInput(player.inventoryMenu.containerId, menuSlotIndex.getAsInt(), player.getInventory().getSelectedSlot(), ContainerInput.SWAP, player);
        onClose();
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        switch (event.key()) {
            case InputConstants.KEY_UP -> selectPrevious();
            case InputConstants.KEY_DOWN -> selectNext();
            case InputConstants.KEY_TAB -> {
                if ((event.modifiers() & GLFW.GLFW_MOD_SHIFT) == 0)
                    selectNext();
                else
                    selectPrevious();
            }
            case InputConstants.KEY_RETURN -> {
                onSelect(resultSlots.getInt(currentSelection));
                return true;
            }
            default -> { return super.keyPressed(event); }
        }
        rebuildWidgets();
        return true;
    }

    private void selectNext() {
        currentSelection = min(resultSlots.size(), currentSelection + 1);
    }

    private void selectPrevious() {
        currentSelection = max(0, currentSelection - 1);
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
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        minecraft.gui.hud.extractDeferredSubtitles();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

    }
}
