package com.ipalacios;

import com.ipalacios.linesafari.Dinglemouse;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SolutionTest {

    // "Good" examples from the Kata description.

    private char[][] makeGrid(String[] in) {
        char[][] out = new char[in.length][];

        for (int i = 0; i < in.length; i++) {
            out[i] = in[i].toCharArray();
        }

        return out;
    }


    @Test
    public void exGood1() {
        final char grid[][] = this.makeGrid(new String[]{
                "           ",
                "X---------X",
                "           ",
                "           "
        });
        assertEquals(true, Dinglemouse.line(grid));
    }

    @Test
    public void exGood2() {
        final char grid[][] = this.makeGrid(new String[]{
                "     ",
                "  X  ",
                "  |  ",
                "  |  ",
                "  X  "
        });
        assertEquals(true, Dinglemouse.line(grid));
    }

    @Test
    public void exGood3() {
        final char grid[][] = this.makeGrid(new String[]{
                "                    ",
                "     +--------+     ",
                "  X--+        +--+  ",
                "                 |  ",
                "                 X  ",
                "                    "
        });
        assertEquals(true, Dinglemouse.line(grid));
    }

    @Test
    public void exGood4() {
        final char grid[][] = this.makeGrid(new String[]{
                "                     ",
                "    +-------------+  ",
                "    |             |  ",
                " X--+      X------+  ",
                "                     "
        });
        assertEquals(true, Dinglemouse.line(grid));
    }

    @Test
    public void exGood5() {
        final char grid[][] = this.makeGrid(new String[]{
                "                      ",
                "   +-------+          ",
                "   |      +++---+     ",
                "X--+      +-+   X      "
        });
        assertEquals(true, Dinglemouse.line(grid));
    }

    // "Bad" examples from the Kata description.

    @Test
    public void exBad1() {
        final char grid[][] = this.makeGrid(new String[]{
                "X-----|----X"
        });
        assertEquals(false, Dinglemouse.line(grid));
    }

    @Test
    public void exBad2() {
        final char grid[][] = this.makeGrid(new String[]{
                " X  ",
                " |  ",
                " +  ",
                " X  "
        });
        assertEquals(false, Dinglemouse.line(grid));
    }

    @Test
    public void exBad3() {
        final char grid[][] = this.makeGrid(new String[]{
                "   |--------+    ",
                "X---        ---+ ",
                "               | ",
                "               X "
        });
        assertEquals(false, Dinglemouse.line(grid));
    }

    @Test
    public void exBad4() {
        final char grid[][] = this.makeGrid(new String[]{
                "              ",
                "   +------    ",
                "   |          ",
                "X--+      X   ",
                "              "
        });
        assertEquals(false, Dinglemouse.line(grid));
    }

    @Test
    public void exBad5() {
        final char grid[][] = this.makeGrid(new String[]{
                "      +------+",
                "      |      |",
                "X-----+------+",
                "      |       ",
                "      X       ",
        });
        assertEquals(false, Dinglemouse.line(grid));
    }

}