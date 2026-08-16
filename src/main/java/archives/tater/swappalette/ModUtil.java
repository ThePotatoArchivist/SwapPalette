package archives.tater.swappalette;

import net.minecraft.util.ToFloatFunction;

import java.util.Comparator;

public class ModUtil {
    public static <T> Comparator<T> comparingFloat(ToFloatFunction<T> keyExtractor) {
        return (c1, c2) -> Float.compare(keyExtractor.applyAsFloat(c1), keyExtractor.applyAsFloat(c2));
    }
}
