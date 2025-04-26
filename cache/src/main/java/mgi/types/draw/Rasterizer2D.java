package mgi.types.draw;

public class Rasterizer2D {

    public static int[] Rasterizer2D_pixels;
    public static int Rasterizer2D_width;
    public static int Rasterizer2D_height;
    public static int Rasterizer2D_yClipStart;
    public static int Rasterizer2D_yClipEnd;
    public static int Rasterizer2D_xClipStart;
    public static int Rasterizer2D_xClipEnd;

    static {
        Rasterizer2D_yClipStart = 0; // L: 11
        Rasterizer2D_yClipEnd = 0; // L: 12
        Rasterizer2D_xClipStart = 0; // L: 13
        Rasterizer2D_xClipEnd = 0; // L: 14
    }
    public static void Rasterizer2D_replace(int[] var0, int var1, int var2) {
        Rasterizer2D_pixels = var0; // L: 19
        Rasterizer2D_width = var1; // L: 20
        Rasterizer2D_height = var2; // L: 21
        Rasterizer2D_setClip(0, 0, var1, var2); // L: 22
    } // L: 23
    public static void Rasterizer2D_setClip(int var0, int var1, int var2, int var3) {
        if (var0 < 0) { // L: 33
            var0 = 0;
        }

        if (var1 < 0) { // L: 34
            var1 = 0;
        }

        if (var2 > Rasterizer2D_width) { // L: 35
            var2 = Rasterizer2D_width;
        }

        if (var3 > Rasterizer2D_height) {
            var3 = Rasterizer2D_height; // L: 36
        }

        Rasterizer2D_xClipStart = var0; // L: 37
        Rasterizer2D_yClipStart = var1; // L: 38
        Rasterizer2D_xClipEnd = var2; // L: 39
        Rasterizer2D_yClipEnd = var3; // L: 40
    } // L: 41
    public static void Rasterizer2D_getClipArray(int[] var0) {
        var0[0] = Rasterizer2D_xClipStart; // L: 51
        var0[1] = Rasterizer2D_yClipStart; // L: 52
        var0[2] = Rasterizer2D_xClipEnd; // L: 53
        var0[3] = Rasterizer2D_yClipEnd; // L: 54
    } // L: 55
    public static void Rasterizer2D_setClipArray(int[] var0) {
        Rasterizer2D_xClipStart = var0[0]; // L: 58
        Rasterizer2D_yClipStart = var0[1]; // L: 59
        Rasterizer2D_xClipEnd = var0[2]; // L: 60
        Rasterizer2D_yClipEnd = var0[3]; // L: 61
    } // L: 62
    public static void Rasterizer2D_clear() {
        int var0 = 0; // L: 65

        int var1;
        for (var1 = Rasterizer2D_width * Rasterizer2D_height - 7; var0 < var1; Rasterizer2D_pixels[var0++] = 0) { // L: 66 67 75
            Rasterizer2D_pixels[var0++] = 0; // L: 68
            Rasterizer2D_pixels[var0++] = 0; // L: 69
            Rasterizer2D_pixels[var0++] = 0; // L: 70
            Rasterizer2D_pixels[var0++] = 0; // L: 71
            Rasterizer2D_pixels[var0++] = 0; // L: 72
            Rasterizer2D_pixels[var0++] = 0; // L: 73
            Rasterizer2D_pixels[var0++] = 0; // L: 74
        }

        for (var1 += 7; var0 < var1; Rasterizer2D_pixels[var0++] = 0) { // L: 77 78
        }

    } // L: 79

    public static void Rasterizer2D_fillRectangle(int var0, int var1, int var2, int var3, int var4) {
        if (var0 < Rasterizer2D_xClipStart) { // L: 235
            var2 -= Rasterizer2D_xClipStart - var0; // L: 236
            var0 = Rasterizer2D_xClipStart; // L: 237
        }

        if (var1 < Rasterizer2D_yClipStart) { // L: 239
            var3 -= Rasterizer2D_yClipStart - var1; // L: 240
            var1 = Rasterizer2D_yClipStart; // L: 241
        }

        if (var0 + var2 > Rasterizer2D_xClipEnd) { // L: 243
            var2 = Rasterizer2D_xClipEnd - var0;
        }

        if (var3 + var1 > Rasterizer2D_yClipEnd) {
            var3 = Rasterizer2D_yClipEnd - var1; // L: 244
        }

        int var5 = Rasterizer2D_width - var2; // L: 245
        int var6 = var0 + Rasterizer2D_width * var1; // L: 246

        for (int var7 = -var3; var7 < 0; ++var7) { // L: 247
            for (int var8 = -var2; var8 < 0; ++var8) { // L: 248
                Rasterizer2D_pixels[var6++] = var4; // L: 249
            }

            var6 += var5; // L: 251
        }

    } // L: 253
    public static void Rasterizer2D_drawVerticalLine(int var0, int var1, int var2, int var3) {
        if (var0 >= Rasterizer2D_xClipStart && var0 < Rasterizer2D_xClipEnd) { // L: 434
            if (var1 < Rasterizer2D_yClipStart) { // L: 435
                var2 -= Rasterizer2D_yClipStart - var1; // L: 436
                var1 = Rasterizer2D_yClipStart; // L: 437
            }

            if (var2 + var1 > Rasterizer2D_yClipEnd) { // L: 439
                var2 = Rasterizer2D_yClipEnd - var1;
            }

            int var4 = var0 + Rasterizer2D_width * var1; // L: 440

            for (int var5 = 0; var5 < var2; ++var5) { // L: 441
                Rasterizer2D_pixels[var4 + var5 * Rasterizer2D_width] = var3;
            }

        }
    } // L: 442
    public static void Rasterizer2D_drawHorizontalLine(int var0, int var1, int var2, int var3) {
        if (var1 >= Rasterizer2D_yClipStart && var1 < Rasterizer2D_yClipEnd) { // L: 399
            if (var0 < Rasterizer2D_xClipStart) { // L: 400
                var2 -= Rasterizer2D_xClipStart - var0; // L: 401
                var0 = Rasterizer2D_xClipStart; // L: 402
            }

            if (var0 + var2 > Rasterizer2D_xClipEnd) { // L: 404
                var2 = Rasterizer2D_xClipEnd - var0;
            }

            int var4 = var0 + Rasterizer2D_width * var1; // L: 405

            for (int var5 = 0; var5 < var2; ++var5) { // L: 406
                Rasterizer2D_pixels[var4 + var5] = var3;
            }

        }
    } // L: 407

}
