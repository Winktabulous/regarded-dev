package mgi.types.draw.sprite;


import mgi.types.draw.Rasterizer2D;

public final class SpritePixels extends Rasterizer2D {

	public int[] pixels;
	public int subWidth;
	public int subHeight;
	public int xOffset;
	public int yOffset;
	public int width;
	public int height;

	public SpritePixels(int[] var1, int var2, int var3) {
		this.pixels = var1; // L: 26
		this.subWidth = this.width = var2; // L: 27
		this.subHeight = this.height = var3; // L: 28
		this.yOffset = 0; // L: 29
		this.xOffset = 0; // L: 30
	} // L: 31

	public SpritePixels(int var1, int var2) {
		this(new int[var2 * var1], var1, var2); // L: 22
	} // L: 23

	public SpritePixels() {
	} // L: 19
    public void drawTransBgAt(int var1, int var2) {
        var1 += this.xOffset; // L: 214
        var2 += this.yOffset; // L: 215
        int var3 = var1 + var2 * Rasterizer2D.Rasterizer2D_width; // L: 216
        int var4 = 0; // L: 217
        int var5 = this.subHeight; // L: 218
        int var6 = this.subWidth; // L: 219
        int var7 = Rasterizer2D.Rasterizer2D_width - var6; // L: 220
        int var8 = 0; // L: 221
        int var9;
        if (var2 < Rasterizer2D.Rasterizer2D_yClipStart) { // L: 222
            var9 = Rasterizer2D.Rasterizer2D_yClipStart - var2; // L: 223
            var5 -= var9; // L: 224
            var2 = Rasterizer2D.Rasterizer2D_yClipStart; // L: 225
            var4 += var9 * var6; // L: 226
            var3 += var9 * Rasterizer2D.Rasterizer2D_width; // L: 227
        }

        if (var5 + var2 > Rasterizer2D.Rasterizer2D_yClipEnd) { // L: 229
            var5 -= var5 + var2 - Rasterizer2D.Rasterizer2D_yClipEnd;
        }

        if (var1 < Rasterizer2D.Rasterizer2D_xClipStart) { // L: 230
            var9 = Rasterizer2D.Rasterizer2D_xClipStart - var1; // L: 231
            var6 -= var9; // L: 232
            var1 = Rasterizer2D.Rasterizer2D_xClipStart; // L: 233
            var4 += var9; // L: 234
            var3 += var9; // L: 235
            var8 += var9; // L: 236
            var7 += var9; // L: 237
        }

        if (var6 + var1 > Rasterizer2D.Rasterizer2D_xClipEnd) { // L: 239
            var9 = var6 + var1 - Rasterizer2D.Rasterizer2D_xClipEnd; // L: 240
            var6 -= var9; // L: 241
            var8 += var9; // L: 242
            var7 += var9; // L: 243
        }

        if (var6 > 0 && var5 > 0) { // L: 245
            Sprite_drawTransBg(Rasterizer2D.Rasterizer2D_pixels, this.pixels, 0, var4, var3, var6, var5, var7, var8); // L: 246
        }
    } // L: 247
    static void Sprite_drawTransBg(int[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        int var9 = -(var5 >> 2); // L: 250
        var5 = -(var5 & 3); // L: 251

        for (int var10 = -var6; var10 < 0; ++var10) { // L: 252
            int var11;
            for (var11 = var9; var11 < 0; ++var11) { // L: 253
                var2 = var1[var3++]; // L: 254
                if (var2 != 0) { // L: 255
                    var0[var4++] = var2;
                } else {
                    ++var4; // L: 256
                }

                var2 = var1[var3++]; // L: 257
                if (var2 != 0) { // L: 258
                    var0[var4++] = var2;
                } else {
                    ++var4; // L: 259
                }

                var2 = var1[var3++]; // L: 260
                if (var2 != 0) { // L: 261
                    var0[var4++] = var2;
                } else {
                    ++var4; // L: 262
                }

                var2 = var1[var3++]; // L: 263
                if (var2 != 0) { // L: 264
                    var0[var4++] = var2;
                } else {
                    ++var4; // L: 265
                }
            }

            for (var11 = var5; var11 < 0; ++var11) { // L: 267
                var2 = var1[var3++]; // L: 268
                if (var2 != 0) { // L: 269
                    var0[var4++] = var2;
                } else {
                    ++var4; // L: 270
                }
            }

            var4 += var7; // L: 272
            var3 += var8; // L: 273
        }

    } // L: 275
    public void outline(int var1) {
        int[] var2 = new int[this.subWidth * this.subHeight]; // L: 133
        int var3 = 0; // L: 134

        for (int var4 = 0; var4 < this.subHeight; ++var4) { // L: 135
            for (int var5 = 0; var5 < this.subWidth; ++var5) { // L: 136
                int var6 = this.pixels[var3]; // L: 137
                if (var6 == 0) { // L: 138
                    if (var5 > 0 && this.pixels[var3 - 1] != 0) { // L: 139
                        var6 = var1;
                    } else if (var4 > 0 && this.pixels[var3 - this.subWidth] != 0) { // L: 140
                        var6 = var1;
                    } else if (var5 < this.subWidth - 1 && this.pixels[var3 + 1] != 0) {
                        var6 = var1; // L: 141
                    } else if (var4 < this.subHeight - 1 && this.pixels[var3 + this.subWidth] != 0) { // L: 142
                        var6 = var1;
                    }
                }

                var2[var3++] = var6; // L: 144
            }
        }

        this.pixels = var2; // L: 147
    } // L: 148
    public void shadow(int var1) {
        for (int var2 = this.subHeight - 1; var2 > 0; --var2) { // L: 151
            int var3 = var2 * this.subWidth; // L: 152

            for (int var4 = this.subWidth - 1; var4 > 0; --var4) { // L: 153
                if (this.pixels[var4 + var3] == 0 && this.pixels[var4 + var3 - 1 - this.subWidth] != 0) { // L: 154
                    this.pixels[var4 + var3] = var1;
                }
            }
        }

    } // L: 157
    public void drawTransAt(int var1, int var2, int var3) {
        var1 += this.xOffset; // L: 400
        var2 += this.yOffset; // L: 401
        int var4 = var1 + var2 * Rasterizer2D.Rasterizer2D_width; // L: 402
        int var5 = 0; // L: 403
        int var6 = this.subHeight; // L: 404
        int var7 = this.subWidth; // L: 405
        int var8 = Rasterizer2D.Rasterizer2D_width - var7; // L: 406
        int var9 = 0; // L: 407
        int var10;
        if (var2 < Rasterizer2D.Rasterizer2D_yClipStart) { // L: 408
            var10 = Rasterizer2D.Rasterizer2D_yClipStart - var2; // L: 409
            var6 -= var10; // L: 410
            var2 = Rasterizer2D.Rasterizer2D_yClipStart; // L: 411
            var5 += var10 * var7; // L: 412
            var4 += var10 * Rasterizer2D.Rasterizer2D_width; // L: 413
        }

        if (var6 + var2 > Rasterizer2D.Rasterizer2D_yClipEnd) { // L: 415
            var6 -= var6 + var2 - Rasterizer2D.Rasterizer2D_yClipEnd;
        }

        if (var1 < Rasterizer2D.Rasterizer2D_xClipStart) { // L: 416
            var10 = Rasterizer2D.Rasterizer2D_xClipStart - var1; // L: 417
            var7 -= var10; // L: 418
            var1 = Rasterizer2D.Rasterizer2D_xClipStart; // L: 419
            var5 += var10; // L: 420
            var4 += var10; // L: 421
            var9 += var10; // L: 422
            var8 += var10; // L: 423
        }

        if (var7 + var1 > Rasterizer2D.Rasterizer2D_xClipEnd) { // L: 425
            var10 = var7 + var1 - Rasterizer2D.Rasterizer2D_xClipEnd; // L: 426
            var7 -= var10; // L: 427
            var9 += var10; // L: 428
            var8 += var10; // L: 429
        }

        if (var7 > 0 && var6 > 0) { // L: 431
            Sprite_drawTransparent(Rasterizer2D.Rasterizer2D_pixels, this.pixels, 0, var5, var4, var7, var6, var8, var9, var3); // L: 432
        }
    } // L: 433
    static void Sprite_drawTransparent(int[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
        int var10 = 256 - var9; // L: 436

        for (int var11 = -var6; var11 < 0; ++var11) { // L: 437
            for (int var12 = -var5; var12 < 0; ++var12) { // L: 438
                var2 = var1[var3++]; // L: 439
                if (var2 != 0) { // L: 440
                    int var13 = var0[var4]; // L: 441
                    var0[var4++] = ((var13 & 16711935) * var10 + var9 * (var2 & 16711935) & -16711936) + ((var2 & 65280) * var9 + var10 * (var13 & 65280) & 16711680) >> 8; // L: 442
                } else {
                    ++var4; // L: 444
                }
            }

            var4 += var7; // L: 446
            var3 += var8; // L: 447
        }

    } // L: 449
}
