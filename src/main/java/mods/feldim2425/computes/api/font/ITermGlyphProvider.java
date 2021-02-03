package mods.feldim2425.computes.api.font;

import net.minecraft.util.Tuple;

import java.nio.ByteBuffer;

public interface ITermGlyphProvider {


    ByteBuffer getImage(int ch);

    /**
     * This is exclusively used to normalize the characters size to the terminal size and for rendering the byte buffer.
     * Since characters sizes have to be known by the server and are monosize, the size
     * is defined by the UniFontUtil.
     *
     * @return Size of a single-with character in a (width, height)-Tuple
     */
    Tuple<Integer, Integer> getSingleChSize();
}
