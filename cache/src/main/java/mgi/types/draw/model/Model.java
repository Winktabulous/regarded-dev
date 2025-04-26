package mgi.types.draw.model;

import mgi.types.draw.Rasterizer3D;

public class Model extends Renderable {

	static Model Model_sharedSequenceModel;
	static byte[] Model_sharedSequenceModelFaceAlphas;
	static Model Model_sharedSpotAnimationModel;
	static byte[] Model_sharedSpotAnimationModelFaceAlphas;
	static boolean[] field2597;
	static boolean[] field2591;
	static int[] modelViewportXs;
	static int[] modelViewportYs;
	static int[] field2548;
	static int[] field2590;
	static int[] field2596;
	static int[] field2602;
	static int[] field2573;
	static int[][] field2561;
	static int[] field2601;
	static int[][] field2547;
	static int[] field2603;
	static int[] field2604;
	static int[] field2605;
	static int[] field2616;
	static int[] field2607;
	static int[] field2608;
	static int Model_transformTempX;
	static int Model_transformTempY;
	static int Model_transformTempZ;
	static boolean field2612;
	static int[] Model_sine;
	static int[] Model_cosine;
	static int[] Model_colorPalette;
	static int[] field2560;
	int verticesCount;
	int[] verticesX;
	int[] verticesY;
	int[] verticesZ;
	int indicesCount;
	int[] indices1;
	int[] indices2;
	int[] indices3;
	int[] faceColors1;
	int[] faceColors2;
	int[] faceColors3;
	byte[] faceRenderPriorities;
	byte[] faceAlphas;
	byte[] field2566;
	short[] faceTextures;
	byte field2568;
	int field2569;
	int[] field2570;
	int[] field2571;
	int[] field2572;
	int[][] vertexLabels;
	int[][] faceLabelsAlpha;
	int[][] field2575;
	int[][] field2594;

	public boolean isSingleTile;
	int boundsType;
	int bottomY;
	int xzRadius;
	int diameter;
	int radius;
	int xMid;
	int yMid;
	int zMid;
	int xMidOffset;
	int yMidOffset;
	int zMidOffset;
	public byte overrideHue;
	public byte overrideSaturation;
	public byte overrideLuminance;
	public byte overrideAmount;

	static {
		Model_sharedSequenceModel = new Model(); // L: 11
		Model_sharedSequenceModelFaceAlphas = new byte[1]; // L: 12
		Model_sharedSpotAnimationModel = new Model(); // L: 13
		Model_sharedSpotAnimationModelFaceAlphas = new byte[1]; // L: 14
		field2597 = new boolean[6500]; // L: 55
		field2591 = new boolean[6500]; // L: 56
		modelViewportXs = new int[6500]; // L: 57
		modelViewportYs = new int[6500]; // L: 58
		field2548 = new int[6500]; // L: 59
		field2590 = new int[6500]; // L: 60
		field2596 = new int[6500]; // L: 61
		field2602 = new int[6500]; // L: 62
		field2573 = new int[1600]; // L: 64
		field2561 = new int[1600][512]; // L: 65
		field2601 = new int[12]; // L: 66
		field2547 = new int[12][2000]; // L: 67
		field2603 = new int[2000]; // L: 68
		field2604 = new int[2000]; // L: 69
		field2605 = new int[12]; // L: 70
		field2616 = new int[10]; // L: 71
		field2607 = new int[10]; // L: 72
		field2608 = new int[10]; // L: 73
		field2612 = true; // L: 77
		Model_sine = Rasterizer3D.Rasterizer3D_sine; // L: 80
		Model_cosine = Rasterizer3D.Rasterizer3D_cosine; // L: 81
		Model_colorPalette = Rasterizer3D.Rasterizer3D_colorPalette; // L: 82
		field2560 = Rasterizer3D.field2385; // L: 83
	}

	Model() {
		this.verticesCount = 0;
		this.indicesCount = 0;
		this.field2568 = 0;
		this.field2569 = 0;
		this.isSingleTile = false;
		this.xMidOffset = -1;
		this.yMidOffset = -1;
		this.zMidOffset = -1;
	} // L: 91

	public Model(Model[] var1, int var2) {
		this.verticesCount = 0; // L: 18
		this.indicesCount = 0; // L: 22
		this.field2568 = 0; // L: 33
		this.field2569 = 0; // L: 34
		this.isSingleTile = false; // L: 42
		this.xMidOffset = -1; // L: 51
		this.yMidOffset = -1; // L: 52
		this.zMidOffset = -1; // L: 53
		boolean var3 = false; // L: 94
		boolean var4 = false; // L: 95
		boolean var5 = false; // L: 96
		boolean var6 = false; // L: 97
		this.verticesCount = 0; // L: 98
		this.indicesCount = 0; // L: 99
		this.field2569 = 0; // L: 100
		this.field2568 = -1; // L: 101

		int var7;
		Model var8;
		for (var7 = 0; var7 < var2; ++var7) { // L: 102
			var8 = var1[var7]; // L: 103
			if (var8 != null) { // L: 104
				this.verticesCount += var8.verticesCount; // L: 105
				this.indicesCount += var8.indicesCount; // L: 106
				this.field2569 += var8.field2569; // L: 107
				if (var8.faceRenderPriorities != null) { // L: 108
					var3 = true;
				} else {
					if (this.field2568 == -1) { // L: 110
						this.field2568 = var8.field2568;
					}

					if (this.field2568 != var8.field2568) { // L: 111
						var3 = true;
					}
				}

				var4 |= var8.faceAlphas != null; // L: 113
				var5 |= var8.faceTextures != null; // L: 114
				var6 |= var8.field2566 != null; // L: 115
			}
		}

		this.verticesX = new int[this.verticesCount]; // L: 118
		this.verticesY = new int[this.verticesCount]; // L: 119
		this.verticesZ = new int[this.verticesCount]; // L: 120
		this.indices1 = new int[this.indicesCount]; // L: 121
		this.indices2 = new int[this.indicesCount]; // L: 122
		this.indices3 = new int[this.indicesCount]; // L: 123
		this.faceColors1 = new int[this.indicesCount]; // L: 124
		this.faceColors2 = new int[this.indicesCount]; // L: 125
		this.faceColors3 = new int[this.indicesCount]; // L: 126
		if (var3) { // L: 127
			this.faceRenderPriorities = new byte[this.indicesCount];
		}

		if (var4) { // L: 128
			this.faceAlphas = new byte[this.indicesCount];
		}

		if (var5) { // L: 129
			this.faceTextures = new short[this.indicesCount];
		}

		if (var6) { // L: 130
			this.field2566 = new byte[this.indicesCount];
		}

		if (this.field2569 > 0) { // L: 131
			this.field2570 = new int[this.field2569]; // L: 132
			this.field2571 = new int[this.field2569]; // L: 133
			this.field2572 = new int[this.field2569]; // L: 134
		}

		this.verticesCount = 0; // L: 136
		this.indicesCount = 0; // L: 137
		this.field2569 = 0; // L: 138

		for (var7 = 0; var7 < var2; ++var7) { // L: 139
			var8 = var1[var7]; // L: 140
			if (var8 != null) { // L: 141
				int var9;
				for (var9 = 0; var9 < var8.indicesCount; ++var9) { // L: 142
					this.indices1[this.indicesCount] = this.verticesCount + var8.indices1[var9]; // L: 143
					this.indices2[this.indicesCount] = this.verticesCount + var8.indices2[var9]; // L: 144
					this.indices3[this.indicesCount] = this.verticesCount + var8.indices3[var9]; // L: 145
					this.faceColors1[this.indicesCount] = var8.faceColors1[var9]; // L: 146
					this.faceColors2[this.indicesCount] = var8.faceColors2[var9]; // L: 147
					this.faceColors3[this.indicesCount] = var8.faceColors3[var9]; // L: 148
					if (var3) { // L: 149
						if (var8.faceRenderPriorities != null) { // L: 150
							this.faceRenderPriorities[this.indicesCount] = var8.faceRenderPriorities[var9];
						} else {
							this.faceRenderPriorities[this.indicesCount] = var8.field2568; // L: 151
						}
					}

					if (var4 && var8.faceAlphas != null) { // L: 153 154
						this.faceAlphas[this.indicesCount] = var8.faceAlphas[var9];
					}

					if (var5) { // L: 156
						if (var8.faceTextures != null) { // L: 157
							this.faceTextures[this.indicesCount] = var8.faceTextures[var9];
						} else {
							this.faceTextures[this.indicesCount] = -1; // L: 158
						}
					}

					if (var6) { // L: 160
						if (var8.field2566 != null && var8.field2566[var9] != -1) { // L: 161
							this.field2566[this.indicesCount] = (byte)(this.field2569 + var8.field2566[var9]);
						} else {
							this.field2566[this.indicesCount] = -1; // L: 162
						}
					}

					++this.indicesCount; // L: 164
				}

				for (var9 = 0; var9 < var8.field2569; ++var9) { // L: 166
					this.field2570[this.field2569] = this.verticesCount + var8.field2570[var9]; // L: 167
					this.field2571[this.field2569] = this.verticesCount + var8.field2571[var9]; // L: 168
					this.field2572[this.field2569] = this.verticesCount + var8.field2572[var9]; // L: 169
					++this.field2569; // L: 170
				}

				for (var9 = 0; var9 < var8.verticesCount; ++var9) { // L: 172
					this.verticesX[this.verticesCount] = var8.verticesX[var9]; // L: 173
					this.verticesY[this.verticesCount] = var8.verticesY[var9]; // L: 174
					this.verticesZ[this.verticesCount] = var8.verticesZ[var9]; // L: 175
					++this.verticesCount; // L: 176
				}
			}
		}

	} // L: 180

	public Model contourGround(int[][] var1, int var2, int var3, int var4, boolean var5, int var6) {
		this.calculateBoundsCylinder(); // L: 183
		int var7 = var2 - this.xzRadius; // L: 184
		int var8 = var2 + this.xzRadius; // L: 185
		int var9 = var4 - this.xzRadius; // L: 186
		int var10 = var4 + this.xzRadius; // L: 187
		if (var7 >= 0 && var8 + 128 >> 7 < var1.length && var9 >= 0 && var10 + 128 >> 7 < var1[0].length) { // L: 188
			var7 >>= 7; // L: 189
			var8 = var8 + 127 >> 7; // L: 190
			var9 >>= 7; // L: 191
			var10 = var10 + 127 >> 7; // L: 192
			if (var3 == var1[var7][var9] && var3 == var1[var8][var9] && var3 == var1[var7][var10] && var3 == var1[var8][var10]) { // L: 193
				return this;
			} else {
				Model var11;
				if (var5) { // L: 195
					var11 = new Model(); // L: 196
					var11.verticesCount = this.verticesCount; // L: 197
					var11.indicesCount = this.indicesCount; // L: 198
					var11.field2569 = this.field2569; // L: 199
					var11.verticesX = this.verticesX; // L: 200
					var11.verticesZ = this.verticesZ; // L: 201
					var11.indices1 = this.indices1; // L: 202
					var11.indices2 = this.indices2; // L: 203
					var11.indices3 = this.indices3; // L: 204
					var11.faceColors1 = this.faceColors1; // L: 205
					var11.faceColors2 = this.faceColors2; // L: 206
					var11.faceColors3 = this.faceColors3; // L: 207
					var11.faceRenderPriorities = this.faceRenderPriorities; // L: 208
					var11.faceAlphas = this.faceAlphas; // L: 209
					var11.field2566 = this.field2566; // L: 210
					var11.faceTextures = this.faceTextures; // L: 211
					var11.field2568 = this.field2568; // L: 212
					var11.field2570 = this.field2570; // L: 213
					var11.field2571 = this.field2571; // L: 214
					var11.field2572 = this.field2572; // L: 215
					var11.vertexLabels = this.vertexLabels; // L: 216
					var11.faceLabelsAlpha = this.faceLabelsAlpha; // L: 217
					var11.isSingleTile = this.isSingleTile; // L: 218
					var11.verticesY = new int[var11.verticesCount]; // L: 219
				} else {
					var11 = this; // L: 222
				}

				int var12;
				int var13;
				int var14;
				int var15;
				int var16;
				int var17;
				int var18;
				int var19;
				int var20;
				int var21;
				if (var6 == 0) { // L: 224
					for (var12 = 0; var12 < var11.verticesCount; ++var12) { // L: 225
						var13 = var2 + this.verticesX[var12]; // L: 226
						var14 = var4 + this.verticesZ[var12]; // L: 227
						var15 = var13 & 127; // L: 228
						var16 = var14 & 127; // L: 229
						var17 = var13 >> 7; // L: 230
						var18 = var14 >> 7; // L: 231
						var19 = var1[var17][var18] * (128 - var15) + var1[var17 + 1][var18] * var15 >> 7; // L: 232
						var20 = var1[var17][var18 + 1] * (128 - var15) + var15 * var1[var17 + 1][var18 + 1] >> 7; // L: 233
						var21 = var19 * (128 - var16) + var20 * var16 >> 7; // L: 234
						var11.verticesY[var12] = var21 + this.verticesY[var12] - var3; // L: 235
					}
				} else {
					for (var12 = 0; var12 < var11.verticesCount; ++var12) { // L: 239
						var13 = (-this.verticesY[var12] << 16) / super.height; // L: 240
						if (var13 < var6) { // L: 241
							var14 = var2 + this.verticesX[var12]; // L: 242
							var15 = var4 + this.verticesZ[var12]; // L: 243
							var16 = var14 & 127; // L: 244
							var17 = var15 & 127; // L: 245
							var18 = var14 >> 7; // L: 246
							var19 = var15 >> 7; // L: 247
							var20 = var1[var18][var19] * (128 - var16) + var1[var18 + 1][var19] * var16 >> 7; // L: 248
							var21 = var1[var18][var19 + 1] * (128 - var16) + var16 * var1[var18 + 1][var19 + 1] >> 7; // L: 249
							int var22 = var20 * (128 - var17) + var21 * var17 >> 7; // L: 250
							var11.verticesY[var12] = (var6 - var13) * (var22 - var3) / var6 + this.verticesY[var12]; // L: 251
						}
					}
				}

				var11.resetBounds(); // L: 255
				return var11; // L: 256
			}
		} else {
			return this;
		}
	}

	public Model toSharedSequenceModel(boolean var1) {
		if (!var1 && Model_sharedSequenceModelFaceAlphas.length < this.indicesCount) { // L: 260
			Model_sharedSequenceModelFaceAlphas = new byte[this.indicesCount + 100];
		}

		return this.buildSharedModel(var1, Model_sharedSequenceModel, Model_sharedSequenceModelFaceAlphas); // L: 261
	}

	public Model toSharedSpotAnimationModel(boolean var1) {
        if (!var1 && Model_sharedSpotAnimationModelFaceAlphas.length < this.indicesCount) { // L: 265
            Model_sharedSpotAnimationModelFaceAlphas = new byte[this.indicesCount + 100];
        }

        return this.buildSharedModel(var1, Model_sharedSpotAnimationModel, Model_sharedSpotAnimationModelFaceAlphas); // L: 266
    }
	
	Model buildSharedModel(boolean var1, Model var2, byte[] var3) {
		var2.verticesCount = this.verticesCount; // L: 270
		var2.indicesCount = this.indicesCount; // L: 271
		var2.field2569 = this.field2569; // L: 272
		if (var2.verticesX == null || var2.verticesX.length < this.verticesCount) { // L: 273
			var2.verticesX = new int[this.verticesCount + 100]; // L: 274
			var2.verticesY = new int[this.verticesCount + 100]; // L: 275
			var2.verticesZ = new int[this.verticesCount + 100]; // L: 276
		}

		int var4;
		for (var4 = 0; var4 < this.verticesCount; ++var4) { // L: 278
			var2.verticesX[var4] = this.verticesX[var4]; // L: 279
			var2.verticesY[var4] = this.verticesY[var4]; // L: 280
			var2.verticesZ[var4] = this.verticesZ[var4]; // L: 281
		}

		if (var1) { // L: 283
			var2.faceAlphas = this.faceAlphas;
		} else {
			var2.faceAlphas = var3; // L: 285
			if (this.faceAlphas == null) { // L: 286
				for (var4 = 0; var4 < this.indicesCount; ++var4) { // L: 287
					var2.faceAlphas[var4] = 0;
				}
			} else {
				for (var4 = 0; var4 < this.indicesCount; ++var4) { // L: 290
					var2.faceAlphas[var4] = this.faceAlphas[var4];
				}
			}
		}

		var2.indices1 = this.indices1; // L: 293
		var2.indices2 = this.indices2; // L: 294
		var2.indices3 = this.indices3; // L: 295
		var2.faceColors1 = this.faceColors1; // L: 296
		var2.faceColors2 = this.faceColors2; // L: 297
		var2.faceColors3 = this.faceColors3; // L: 298
		var2.faceRenderPriorities = this.faceRenderPriorities; // L: 299
		var2.field2566 = this.field2566; // L: 300
		var2.faceTextures = this.faceTextures; // L: 301
		var2.field2568 = this.field2568; // L: 302
		var2.field2570 = this.field2570; // L: 303
		var2.field2571 = this.field2571; // L: 304
		var2.field2572 = this.field2572; // L: 305
		var2.vertexLabels = this.vertexLabels; // L: 306
		var2.faceLabelsAlpha = this.faceLabelsAlpha; // L: 307
		var2.field2575 = this.field2575; // L: 308
		var2.field2594 = this.field2594; // L: 309
		var2.isSingleTile = this.isSingleTile; // L: 310
		var2.resetBounds(); // L: 311
		var2.overrideAmount = 0; // L: 312
		return var2; // L: 313
	}

	
	
	void calculateBoundingBox(int var1) {
		if (this.xMidOffset == -1) { // L: 317
			int var2 = 0; // L: 318
			int var3 = 0; // L: 319
			int var4 = 0; // L: 320
			int var5 = 0; // L: 321
			int var6 = 0; // L: 322
			int var7 = 0; // L: 323
			int var8 = Model_cosine[var1]; // L: 324
			int var9 = Model_sine[var1]; // L: 325

			for (int var10 = 0; var10 < this.verticesCount; ++var10) { // L: 326
				int var11 = Rasterizer3D.method3927(this.verticesX[var10], this.verticesZ[var10], var8, var9); // L: 327
				int var12 = this.verticesY[var10]; // L: 328
				int var13 = Rasterizer3D.method3951(this.verticesX[var10], this.verticesZ[var10], var8, var9); // L: 329
				if (var11 < var2) { // L: 330
					var2 = var11;
				}

				if (var11 > var5) { // L: 331
					var5 = var11;
				}

				if (var12 < var3) { // L: 332
					var3 = var12;
				}

				if (var12 > var6) { // L: 333
					var6 = var12;
				}

				if (var13 < var4) { // L: 334
					var4 = var13;
				}

				if (var13 > var7) { // L: 335
					var7 = var13;
				}
			}

			this.xMid = (var5 + var2) / 2; // L: 337
			this.yMid = (var6 + var3) / 2; // L: 338
			this.zMid = (var7 + var4) / 2; // L: 339
			this.xMidOffset = (var5 - var2 + 1) / 2; // L: 340
			this.yMidOffset = (var6 - var3 + 1) / 2; // L: 341
			this.zMidOffset = (var7 - var4 + 1) / 2; // L: 342
			boolean var14 = true; // L: 343
			if (this.xMidOffset < 32) { // L: 344
				this.xMidOffset = 32;
			}

			if (this.zMidOffset < 32) { // L: 345
				this.zMidOffset = 32;
			}

			if (this.isSingleTile) { // L: 346
				boolean var15 = true; // L: 347
				this.xMidOffset += 8; // L: 348
				this.zMidOffset += 8; // L: 349
			}

		}
	} // L: 351

	
	
	public void calculateBoundsCylinder() {
		if (this.boundsType != 1) { // L: 354
			this.boundsType = 1; // L: 355
			super.height = 0; // L: 356
			this.bottomY = 0; // L: 357
			this.xzRadius = 0; // L: 358

			for (int var1 = 0; var1 < this.verticesCount; ++var1) { // L: 359
				int var2 = this.verticesX[var1]; // L: 360
				int var3 = this.verticesY[var1]; // L: 361
				int var4 = this.verticesZ[var1]; // L: 362
				if (-var3 > super.height) { // L: 363
					super.height = -var3;
				}

				if (var3 > this.bottomY) { // L: 364
					this.bottomY = var3;
				}

				int var5 = var2 * var2 + var4 * var4; // L: 365
				if (var5 > this.xzRadius) { // L: 366
					this.xzRadius = var5;
				}
			}

			this.xzRadius = (int)(Math.sqrt((double)this.xzRadius) + 0.99D); // L: 368
			this.radius = (int)(Math.sqrt((double)(this.xzRadius * this.xzRadius + super.height * super.height)) + 0.99D); // L: 369
			this.diameter = this.radius + (int)(Math.sqrt((double)(this.xzRadius * this.xzRadius + this.bottomY * this.bottomY)) + 0.99D); // L: 370
		}
	} // L: 371

	
	void method4255() {
		if (this.boundsType != 2) { // L: 374
			this.boundsType = 2; // L: 375
			this.xzRadius = 0; // L: 376

			for (int var1 = 0; var1 < this.verticesCount; ++var1) { // L: 377
				int var2 = this.verticesX[var1]; // L: 378
				int var3 = this.verticesY[var1]; // L: 379
				int var4 = this.verticesZ[var1]; // L: 380
				int var5 = var2 * var2 + var4 * var4 + var3 * var3; // L: 381
				if (var5 > this.xzRadius) { // L: 382
					this.xzRadius = var5;
				}
			}

			this.xzRadius = (int)(Math.sqrt((double)this.xzRadius) + 0.99D); // L: 384
			this.radius = this.xzRadius; // L: 385
			this.diameter = this.xzRadius + this.xzRadius; // L: 386
		}
	} // L: 387

	
	public int method4277() {
		this.calculateBoundsCylinder(); // L: 390
		return this.xzRadius; // L: 391
	}


	public void resetBounds() {
		this.boundsType = 0; // L: 395
		this.xMidOffset = -1; // L: 396
	} // L: 397

	public void rotateY90Ccw() {
		for (int var1 = 0; var1 < this.verticesCount; ++var1) { // L: 659
			int var2 = this.verticesX[var1]; // L: 660
			this.verticesX[var1] = this.verticesZ[var1]; // L: 661
			this.verticesZ[var1] = -var2; // L: 662
		}

		this.resetBounds(); // L: 664
	} // L: 665

	
	
	public void rotateY180() {
		for (int var1 = 0; var1 < this.verticesCount; ++var1) { // L: 668
			this.verticesX[var1] = -this.verticesX[var1]; // L: 669
			this.verticesZ[var1] = -this.verticesZ[var1]; // L: 670
		}

		this.resetBounds(); // L: 672
	} // L: 673

	
	
	public void rotateY270Ccw() {
		for (int var1 = 0; var1 < this.verticesCount; ++var1) { // L: 676
			int var2 = this.verticesZ[var1]; // L: 677
			this.verticesZ[var1] = this.verticesX[var1]; // L: 678
			this.verticesX[var1] = -var2; // L: 679
		}

		this.resetBounds(); // L: 681
	} // L: 682

	
	
	public void rotateZ(int var1) {
		int var2 = Model_sine[var1]; // L: 685
		int var3 = Model_cosine[var1]; // L: 686

		for (int var4 = 0; var4 < this.verticesCount; ++var4) { // L: 687
			int var5 = var3 * this.verticesY[var4] - var2 * this.verticesZ[var4] >> 16; // L: 688
			this.verticesZ[var4] = var2 * this.verticesY[var4] + var3 * this.verticesZ[var4] >> 16; // L: 689
			this.verticesY[var4] = var5; // L: 690
		}

		this.resetBounds(); // L: 692
	} // L: 693

	
	
	public void offsetBy(int var1, int var2, int var3) {
		for (int var4 = 0; var4 < this.verticesCount; ++var4) { // L: 696
			int[] var10000 = this.verticesX; // L: 697
			var10000[var4] += var1;
			var10000 = this.verticesY; // L: 698
			var10000[var4] += var2;
			var10000 = this.verticesZ; // L: 699
			var10000[var4] += var3;
		}

		this.resetBounds(); // L: 701
	} // L: 702

	
	
	public void scale(int var1, int var2, int var3) {
		for (int var4 = 0; var4 < this.verticesCount; ++var4) { // L: 705
			this.verticesX[var4] = this.verticesX[var4] * var1 / 128; // L: 706
			this.verticesY[var4] = var2 * this.verticesY[var4] / 128; // L: 707
			this.verticesZ[var4] = var3 * this.verticesZ[var4] / 128; // L: 708
		}

		this.resetBounds(); // L: 710
	} // L: 711
	
	final void drawFace(int var1) {
		if (field2591[var1]) { // L: 1210
			this.method4261(var1); // L: 1211
		} else {
			int var2 = this.indices1[var1]; // L: 1214
			int var3 = this.indices2[var1]; // L: 1215
			int var4 = this.indices3[var1]; // L: 1216
			Rasterizer3D.field2395 = field2597[var1]; // L: 1217
			if (this.faceAlphas == null) { // L: 1218
				Rasterizer3D.Rasterizer3D_alpha = 0; // L: 1219
			} else {
				Rasterizer3D.Rasterizer3D_alpha = this.faceAlphas[var1] & 255; // L: 1222
			}

			this.method4278(var1, modelViewportYs[var2], modelViewportYs[var3], modelViewportYs[var4], modelViewportXs[var2], modelViewportXs[var3], modelViewportXs[var4], this.faceColors1[var1], this.faceColors2[var1], this.faceColors3[var1]); // L: 1224
		}
	} // L: 1212 1225

	
	final void method4278(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
		if (this.faceTextures != null && this.faceTextures[var1] != -1) { // L: 1228
			int var11;
			int var12;
			int var13;
			if (this.field2566 != null && this.field2566[var1] != -1) { // L: 1246
				int var14 = this.field2566[var1] & 255; // L: 1247
				var11 = this.field2570[var14]; // L: 1248
				var12 = this.field2571[var14]; // L: 1249
				var13 = this.field2572[var14]; // L: 1250
			} else {
				var11 = this.indices1[var1]; // L: 1253
				var12 = this.indices2[var1]; // L: 1254
				var13 = this.indices3[var1]; // L: 1255
			}

			if (this.faceColors3[var1] == -1) { // L: 1257
				Rasterizer3D.method3922(var2, var3, var4, var5, var6, var7, var8, var8, var8, field2590[var11], field2590[var12], field2590[var13], field2596[var11], field2596[var12], field2596[var13], field2602[var11], field2602[var12], field2602[var13], this.faceTextures[var1]); // L: 1258
			} else {
				Rasterizer3D.method3922(var2, var3, var4, var5, var6, var7, var8, var9, var10, field2590[var11], field2590[var12], field2590[var13], field2596[var11], field2596[var12], field2596[var13], field2602[var11], field2602[var12], field2602[var13], this.faceTextures[var1]); // L: 1261
			}
		} else if (this.faceColors3[var1] == -1 && this.overrideAmount > 0) { // L: 1229
			Rasterizer3D.method3993(var2, var3, var4, var5, var6, var7, Model_colorPalette[this.faceColors1[var1]], this.overrideHue, this.overrideSaturation, this.overrideLuminance, this.overrideAmount); // L: 1230
		} else if (this.faceColors3[var1] == -1) { // L: 1232
			Rasterizer3D.method3919(var2, var3, var4, var5, var6, var7, Model_colorPalette[this.faceColors1[var1]]); // L: 1233
		} else if (this.overrideAmount > 0) { // L: 1235
			Rasterizer3D.method3916(var2, var3, var4, var5, var6, var7, var8, var9, var10, this.overrideHue, this.overrideSaturation, this.overrideLuminance, this.overrideAmount); // L: 1236
		} else {
			Rasterizer3D.method3915(var2, var3, var4, var5, var6, var7, var8, var9, var10); // L: 1239
		}

	} // L: 1264

	
	final void method4261(int var1) {
		int var2 = Rasterizer3D.Rasterizer3D_clipMidX; // L: 1267
		int var3 = Rasterizer3D.Rasterizer3D_clipMidY; // L: 1268
		int var4 = 0; // L: 1269
		int var5 = this.indices1[var1]; // L: 1270
		int var6 = this.indices2[var1]; // L: 1271
		int var7 = this.indices3[var1]; // L: 1272
		int var8 = field2602[var5]; // L: 1273
		int var9 = field2602[var6]; // L: 1274
		int var10 = field2602[var7]; // L: 1275
		if (this.faceAlphas == null) { // L: 1276
			Rasterizer3D.Rasterizer3D_alpha = 0;
		} else {
			Rasterizer3D.Rasterizer3D_alpha = this.faceAlphas[var1] & 255; // L: 1277
		}

		int var11;
		int var12;
		int var13;
		int var14;
		if (var8 >= 50) { // L: 1278
			field2616[var4] = modelViewportXs[var5]; // L: 1279
			field2607[var4] = modelViewportYs[var5]; // L: 1280
			field2608[var4++] = this.faceColors1[var1]; // L: 1281
		} else {
			var11 = field2590[var5]; // L: 1284
			var12 = field2596[var5]; // L: 1285
			var13 = this.faceColors1[var1]; // L: 1286
			if (var10 >= 50) { // L: 1287
				var14 = field2560[var10 - var8] * (50 - var8); // L: 1288
				field2616[var4] = var2 + Rasterizer3D.Rasterizer3D_zoom * (var11 + ((field2590[var7] - var11) * var14 >> 16)) / 50; // L: 1289
				field2607[var4] = var3 + Rasterizer3D.Rasterizer3D_zoom * (var12 + ((field2596[var7] - var12) * var14 >> 16)) / 50; // L: 1290
				field2608[var4++] = var13 + ((this.faceColors3[var1] - var13) * var14 >> 16); // L: 1291
			}

			if (var9 >= 50) { // L: 1293
				var14 = field2560[var9 - var8] * (50 - var8); // L: 1294
				field2616[var4] = var2 + Rasterizer3D.Rasterizer3D_zoom * (var11 + ((field2590[var6] - var11) * var14 >> 16)) / 50; // L: 1295
				field2607[var4] = var3 + Rasterizer3D.Rasterizer3D_zoom * (var12 + ((field2596[var6] - var12) * var14 >> 16)) / 50; // L: 1296
				field2608[var4++] = var13 + ((this.faceColors2[var1] - var13) * var14 >> 16); // L: 1297
			}
		}

		if (var9 >= 50) { // L: 1300
			field2616[var4] = modelViewportXs[var6]; // L: 1301
			field2607[var4] = modelViewportYs[var6]; // L: 1302
			field2608[var4++] = this.faceColors2[var1]; // L: 1303
		} else {
			var11 = field2590[var6]; // L: 1306
			var12 = field2596[var6]; // L: 1307
			var13 = this.faceColors2[var1]; // L: 1308
			if (var8 >= 50) { // L: 1309
				var14 = field2560[var8 - var9] * (50 - var9); // L: 1310
				field2616[var4] = var2 + Rasterizer3D.Rasterizer3D_zoom * (var11 + ((field2590[var5] - var11) * var14 >> 16)) / 50; // L: 1311
				field2607[var4] = var3 + Rasterizer3D.Rasterizer3D_zoom * (var12 + ((field2596[var5] - var12) * var14 >> 16)) / 50; // L: 1312
				field2608[var4++] = var13 + ((this.faceColors1[var1] - var13) * var14 >> 16); // L: 1313
			}

			if (var10 >= 50) { // L: 1315
				var14 = field2560[var10 - var9] * (50 - var9); // L: 1316
				field2616[var4] = var2 + Rasterizer3D.Rasterizer3D_zoom * (var11 + ((field2590[var7] - var11) * var14 >> 16)) / 50; // L: 1317
				field2607[var4] = var3 + Rasterizer3D.Rasterizer3D_zoom * (var12 + ((field2596[var7] - var12) * var14 >> 16)) / 50; // L: 1318
				field2608[var4++] = var13 + ((this.faceColors3[var1] - var13) * var14 >> 16); // L: 1319
			}
		}

		if (var10 >= 50) { // L: 1322
			field2616[var4] = modelViewportXs[var7]; // L: 1323
			field2607[var4] = modelViewportYs[var7]; // L: 1324
			field2608[var4++] = this.faceColors3[var1]; // L: 1325
		} else {
			var11 = field2590[var7]; // L: 1328
			var12 = field2596[var7]; // L: 1329
			var13 = this.faceColors3[var1]; // L: 1330
			if (var9 >= 50) { // L: 1331
				var14 = field2560[var9 - var10] * (50 - var10); // L: 1332
				field2616[var4] = var2 + Rasterizer3D.Rasterizer3D_zoom * (var11 + ((field2590[var6] - var11) * var14 >> 16)) / 50; // L: 1333
				field2607[var4] = var3 + Rasterizer3D.Rasterizer3D_zoom * (var12 + ((field2596[var6] - var12) * var14 >> 16)) / 50; // L: 1334
				field2608[var4++] = var13 + ((this.faceColors2[var1] - var13) * var14 >> 16); // L: 1335
			}

			if (var8 >= 50) { // L: 1337
				var14 = field2560[var8 - var10] * (50 - var10); // L: 1338
				field2616[var4] = var2 + Rasterizer3D.Rasterizer3D_zoom * (var11 + ((field2590[var5] - var11) * var14 >> 16)) / 50; // L: 1339
				field2607[var4] = var3 + Rasterizer3D.Rasterizer3D_zoom * (var12 + ((field2596[var5] - var12) * var14 >> 16)) / 50; // L: 1340
				field2608[var4++] = var13 + ((this.faceColors1[var1] - var13) * var14 >> 16); // L: 1341
			}
		}

		var11 = field2616[0]; // L: 1344
		var12 = field2616[1]; // L: 1345
		var13 = field2616[2]; // L: 1346
		var14 = field2607[0]; // L: 1347
		int var15 = field2607[1]; // L: 1348
		int var16 = field2607[2]; // L: 1349
		Rasterizer3D.field2395 = false; // L: 1350
		if (var4 == 3) { // L: 1351
			if (var11 < 0 || var12 < 0 || var13 < 0 || var11 > Rasterizer3D.Rasterizer3D_clipWidth || var12 > Rasterizer3D.Rasterizer3D_clipWidth || var13 > Rasterizer3D.Rasterizer3D_clipWidth) { // L: 1352
				Rasterizer3D.field2395 = true;
			}

			this.method4278(var1, var14, var15, var16, var11, var12, var13, field2608[0], field2608[1], field2608[2]); // L: 1353
		}

		if (var4 == 4) { // L: 1355
			if (var11 < 0 || var12 < 0 || var13 < 0 || var11 > Rasterizer3D.Rasterizer3D_clipWidth || var12 > Rasterizer3D.Rasterizer3D_clipWidth || var13 > Rasterizer3D.Rasterizer3D_clipWidth || field2616[3] < 0 || field2616[3] > Rasterizer3D.Rasterizer3D_clipWidth) { // L: 1356
				Rasterizer3D.field2395 = true;
			}

			int var17;
			if (this.faceTextures != null && this.faceTextures[var1] != -1) { // L: 1357
				int var18;
				int var19;
				if (this.field2566 != null && this.field2566[var1] != -1) { // L: 1381
					int var20 = this.field2566[var1] & 255; // L: 1382
					var17 = this.field2570[var20]; // L: 1383
					var18 = this.field2571[var20]; // L: 1384
					var19 = this.field2572[var20]; // L: 1385
				} else {
					var17 = var5; // L: 1388
					var18 = var6; // L: 1389
					var19 = var7; // L: 1390
				}

				short var21 = this.faceTextures[var1]; // L: 1392
				if (this.faceColors3[var1] == -1) { // L: 1393
					Rasterizer3D.method3922(var14, var15, var16, var11, var12, var13, this.faceColors1[var1], this.faceColors1[var1], this.faceColors1[var1], field2590[var17], field2590[var18], field2590[var19], field2596[var17], field2596[var18], field2596[var19], field2602[var17], field2602[var18], field2602[var19], var21); // L: 1394
					Rasterizer3D.method3922(var14, var16, field2607[3], var11, var13, field2616[3], this.faceColors1[var1], this.faceColors1[var1], this.faceColors1[var1], field2590[var17], field2590[var18], field2590[var19], field2596[var17], field2596[var18], field2596[var19], field2602[var17], field2602[var18], field2602[var19], var21); // L: 1395
				} else {
					Rasterizer3D.method3922(var14, var15, var16, var11, var12, var13, field2608[0], field2608[1], field2608[2], field2590[var17], field2590[var18], field2590[var19], field2596[var17], field2596[var18], field2596[var19], field2602[var17], field2602[var18], field2602[var19], var21); // L: 1398
					Rasterizer3D.method3922(var14, var16, field2607[3], var11, var13, field2616[3], field2608[0], field2608[2], field2608[3], field2590[var17], field2590[var18], field2590[var19], field2596[var17], field2596[var18], field2596[var19], field2602[var17], field2602[var18], field2602[var19], var21); // L: 1399
				}
			} else if (this.faceColors3[var1] == -1 && this.overrideAmount > 0) { // L: 1358
				var17 = Model_colorPalette[this.faceColors1[var1]]; // L: 1359
				Rasterizer3D.method3993(var14, var15, var16, var11, var12, var13, var17, this.overrideHue, this.overrideSaturation, this.overrideLuminance, this.overrideAmount); // L: 1360
				Rasterizer3D.method3993(var14, var16, field2607[3], var11, var13, field2616[3], var17, this.overrideHue, this.overrideSaturation, this.overrideLuminance, this.overrideAmount); // L: 1361
			} else if (this.faceColors3[var1] == -1) { // L: 1363
				var17 = Model_colorPalette[this.faceColors1[var1]]; // L: 1364
				Rasterizer3D.method3919(var14, var15, var16, var11, var12, var13, var17); // L: 1365
				Rasterizer3D.method3919(var14, var16, field2607[3], var11, var13, field2616[3], var17); // L: 1366
			} else if (this.overrideAmount > 0) { // L: 1368
				Rasterizer3D.method3916(var14, var15, var16, var11, var12, var13, field2608[0], field2608[1], field2608[2], this.overrideHue, this.overrideLuminance, this.overrideSaturation, this.overrideAmount); // L: 1369
				Rasterizer3D.method3916(var14, var16, field2607[3], var11, var13, field2616[3], field2608[0], field2608[2], field2608[3], this.overrideHue, this.overrideLuminance, this.overrideSaturation, this.overrideAmount); // L: 1370
			} else {
				Rasterizer3D.method3915(var14, var15, var16, var11, var12, var13, field2608[0], field2608[1], field2608[2]); // L: 1373
				Rasterizer3D.method3915(var14, var16, field2607[3], var11, var13, field2616[3], field2608[0], field2608[2], field2608[3]); // L: 1374
			}
		}
	} // L: 1403

    public final void method4272(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        field2573[0] = -1; // L: 714
        if (this.boundsType != 2 && this.boundsType != 1) { // L: 715
            this.method4255();
        }

        int var8 = Rasterizer3D.Rasterizer3D_clipMidX; // L: 716
        int var9 = Rasterizer3D.Rasterizer3D_clipMidY; // L: 717
        int var10 = Model_sine[var1]; // L: 718
        int var11 = Model_cosine[var1]; // L: 719
        int var12 = Model_sine[var2]; // L: 720
        int var13 = Model_cosine[var2]; // L: 721
        int var14 = Model_sine[var3]; // L: 722
        int var15 = Model_cosine[var3]; // L: 723
        int var16 = Model_sine[var4]; // L: 724
        int var17 = Model_cosine[var4]; // L: 725
        int var18 = var16 * var6 + var17 * var7 >> 16; // L: 726

        for (int var19 = 0; var19 < this.verticesCount; ++var19) { // L: 727
            int var20 = this.verticesX[var19]; // L: 728
            int var21 = this.verticesY[var19]; // L: 729
            int var22 = this.verticesZ[var19]; // L: 730
            int var23;
            if (var3 != 0) { // L: 731
                var23 = var21 * var14 + var20 * var15 >> 16; // L: 732
                var21 = var21 * var15 - var20 * var14 >> 16; // L: 733
                var20 = var23; // L: 734
            }

            if (var1 != 0) { // L: 736
                var23 = var21 * var11 - var22 * var10 >> 16; // L: 737
                var22 = var21 * var10 + var22 * var11 >> 16; // L: 738
                var21 = var23; // L: 739
            }

            if (var2 != 0) { // L: 741
                var23 = var22 * var12 + var20 * var13 >> 16; // L: 742
                var22 = var22 * var13 - var20 * var12 >> 16; // L: 743
                var20 = var23; // L: 744
            }

            var20 += var5; // L: 746
            var21 += var6; // L: 747
            var22 += var7; // L: 748
            var23 = var21 * var17 - var22 * var16 >> 16; // L: 749
            var22 = var21 * var16 + var22 * var17 >> 16; // L: 750
            field2548[var19] = var22 - var18; // L: 752
            modelViewportXs[var19] = var20 * Rasterizer3D.Rasterizer3D_zoom / var22 + var8; // L: 753
            modelViewportYs[var19] = var23 * Rasterizer3D.Rasterizer3D_zoom / var22 + var9; // L: 754
            if (this.field2569 > 0) { // L: 755
                field2590[var19] = var20; // L: 756
                field2596[var19] = var23; // L: 757
                field2602[var19] = var22; // L: 758
            }
        }

        try {
            this.draw0(false, false, false, 0L); // L: 762
        } catch (Exception var25) { // L: 764
            var25.printStackTrace();
        }

    } // L: 765
    final void draw0(boolean var1, boolean var2, boolean var3, long var4) {
        if (this.diameter < 1600) { // L: 1026
            int var6;
            for (var6 = 0; var6 < this.diameter; ++var6) { // L: 1027
                field2573[var6] = 0;
            }

            var6 = var3 ? 20 : 5; // L: 1028

            int var7;
            int var8;
            int var9;
            int var10;
            int var11;
            int var12;
            int var15;
            int var16;
            int var18;
            int var28;
            for (var7 = 0; var7 < this.indicesCount; ++var7) { // L: 1029
                if (this.faceColors3[var7] != -2) { // L: 1030
                    var8 = this.indices1[var7]; // L: 1031
                    var9 = this.indices2[var7]; // L: 1032
                    var10 = this.indices3[var7]; // L: 1033
                    var11 = modelViewportXs[var8]; // L: 1034
                    var12 = modelViewportXs[var9]; // L: 1035
                    var28 = modelViewportXs[var10]; // L: 1036
                    int var29;
                    int var30;
                    if (!var1 || var11 != -5000 && var12 != -5000 && var28 != -5000) { // L: 1037
                        if (var2) { // L: 1063
                            var15 = modelViewportYs[var8]; // L: 1065
                            var16 = modelViewportYs[var9]; // L: 1066
                            var30 = modelViewportYs[var10]; // L: 1067
                            var18 = var6 + ViewportMouse.ViewportMouse_y; // L: 1069
                            boolean var34;
                            if (var18 < var15 && var18 < var16 && var18 < var30) { // L: 1070
                                var34 = false; // L: 1071
                            } else {
                                var18 = ViewportMouse.ViewportMouse_y - var6; // L: 1074
                                if (var18 > var15 && var18 > var16 && var18 > var30) { // L: 1075
                                    var34 = false; // L: 1076
                                } else {
                                    var18 = var6 + ViewportMouse.ViewportMouse_x; // L: 1079
                                    if (var18 < var11 && var18 < var12 && var18 < var28) { // L: 1080
                                        var34 = false; // L: 1081
                                    } else {
                                        var18 = ViewportMouse.ViewportMouse_x - var6; // L: 1084
                                        if (var18 > var11 && var18 > var12 && var18 > var28) { // L: 1085
                                            var34 = false; // L: 1086
                                        } else {
                                            var34 = true; // L: 1089
                                        }
                                    }
                                }
                            }

                            if (var34) { // L: 1091
                                ViewportMouse.ViewportMouse_entityTags[++ViewportMouse.ViewportMouse_entityCount - 1] = var4; // L: 1094
                                var2 = false; // L: 1096
                            }
                        }

                        if ((var11 - var12) * (modelViewportYs[var10] - modelViewportYs[var9]) - (var28 - var12) * (modelViewportYs[var8] - modelViewportYs[var9]) > 0) { // L: 1099
                            field2591[var7] = false; // L: 1100
                            if (var11 >= 0 && var12 >= 0 && var28 >= 0 && var11 <= Rasterizer3D.Rasterizer3D_clipWidth && var12 <= Rasterizer3D.Rasterizer3D_clipWidth && var28 <= Rasterizer3D.Rasterizer3D_clipWidth) { // L: 1101
                                field2597[var7] = false; // L: 1102
                            } else {
                                field2597[var7] = true;
                            }

                            var29 = (field2548[var8] + field2548[var9] + field2548[var10]) / 3 + this.radius; // L: 1103
                            field2561[var29][field2573[var29]++] = var7; // L: 1104
                        }
                    } else {
                        var29 = field2590[var8]; // L: 1038
                        var15 = field2590[var9]; // L: 1039
                        var16 = field2590[var10]; // L: 1040
                        var30 = field2596[var8]; // L: 1041
                        var18 = field2596[var9]; // L: 1042
                        int var19 = field2596[var10]; // L: 1043
                        int var20 = field2602[var8]; // L: 1044
                        int var21 = field2602[var9]; // L: 1045
                        int var22 = field2602[var10]; // L: 1046
                        var29 -= var15; // L: 1047
                        var16 -= var15; // L: 1048
                        var30 -= var18; // L: 1049
                        var19 -= var18; // L: 1050
                        var20 -= var21; // L: 1051
                        var22 -= var21; // L: 1052
                        int var23 = var30 * var22 - var20 * var19; // L: 1053
                        int var24 = var20 * var16 - var29 * var22; // L: 1054
                        int var25 = var29 * var19 - var30 * var16; // L: 1055
                        if (var15 * var23 + var18 * var24 + var21 * var25 > 0) { // L: 1056
                            field2591[var7] = true; // L: 1057
                            int var26 = (field2548[var8] + field2548[var9] + field2548[var10]) / 3 + this.radius; // L: 1058
                            field2561[var26][field2573[var26]++] = var7; // L: 1059
                        }
                    }
                }
            }

            int[] var27;
            if (this.faceRenderPriorities == null) { // L: 1108
                for (var7 = this.diameter - 1; var7 >= 0; --var7) { // L: 1109
                    var8 = field2573[var7]; // L: 1110
                    if (var8 > 0) { // L: 1111
                        var27 = field2561[var7]; // L: 1112

                        for (var10 = 0; var10 < var8; ++var10) { // L: 1113
                            this.drawFace(var27[var10]);
                        }
                    }
                }

            } else {
                for (var7 = 0; var7 < 12; ++var7) { // L: 1118
                    field2601[var7] = 0; // L: 1119
                    field2605[var7] = 0; // L: 1120
                }

                for (var7 = this.diameter - 1; var7 >= 0; --var7) { // L: 1122
                    var8 = field2573[var7]; // L: 1123
                    if (var8 > 0) { // L: 1124
                        var27 = field2561[var7]; // L: 1125

                        for (var10 = 0; var10 < var8; ++var10) { // L: 1126
                            var11 = var27[var10]; // L: 1127
                            byte var33 = this.faceRenderPriorities[var11]; // L: 1128
                            var28 = field2601[var33]++; // L: 1129
                            field2547[var33][var28] = var11; // L: 1130
                            if (var33 < 10) { // L: 1131
                                int[] var10000 = field2605;
                                var10000[var33] += var7;
                            } else if (var33 == 10) { // L: 1132
                                field2603[var28] = var7;
                            } else {
                                field2604[var28] = var7; // L: 1133
                            }
                        }
                    }
                }

                var7 = 0; // L: 1137
                if (field2601[1] > 0 || field2601[2] > 0) { // L: 1138
                    var7 = (field2605[1] + field2605[2]) / (field2601[1] + field2601[2]);
                }

                var8 = 0; // L: 1139
                if (field2601[3] > 0 || field2601[4] > 0) { // L: 1140
                    var8 = (field2605[3] + field2605[4]) / (field2601[3] + field2601[4]);
                }

                var9 = 0; // L: 1141
                if (field2601[6] > 0 || field2601[8] > 0) { // L: 1142
                    var9 = (field2605[8] + field2605[6]) / (field2601[8] + field2601[6]);
                }

                var11 = 0; // L: 1144
                var12 = field2601[10]; // L: 1145
                int[] var13 = field2547[10]; // L: 1146
                int[] var14 = field2603; // L: 1147
                if (var11 == var12) { // L: 1148
                    var11 = 0; // L: 1149
                    var12 = field2601[11]; // L: 1150
                    var13 = field2547[11]; // L: 1151
                    var14 = field2604; // L: 1152
                }

                if (var11 < var12) { // L: 1154
                    var10 = var14[var11];
                } else {
                    var10 = -1000; // L: 1155
                }

                for (var15 = 0; var15 < 10; ++var15) { // L: 1156
                    while (var15 == 0 && var10 > var7) { // L: 1157
                        this.drawFace(var13[var11++]); // L: 1158
                        if (var11 == var12 && var13 != field2547[11]) { // L: 1159
                            var11 = 0; // L: 1160
                            var12 = field2601[11]; // L: 1161
                            var13 = field2547[11]; // L: 1162
                            var14 = field2604; // L: 1163
                        }

                        if (var11 < var12) { // L: 1165
                            var10 = var14[var11];
                        } else {
                            var10 = -1000; // L: 1166
                        }
                    }

                    while (var15 == 3 && var10 > var8) { // L: 1168
                        this.drawFace(var13[var11++]); // L: 1169
                        if (var11 == var12 && var13 != field2547[11]) { // L: 1170
                            var11 = 0; // L: 1171
                            var12 = field2601[11]; // L: 1172
                            var13 = field2547[11]; // L: 1173
                            var14 = field2604; // L: 1174
                        }

                        if (var11 < var12) { // L: 1176
                            var10 = var14[var11];
                        } else {
                            var10 = -1000; // L: 1177
                        }
                    }

                    while (var15 == 5 && var10 > var9) { // L: 1179
                        this.drawFace(var13[var11++]); // L: 1180
                        if (var11 == var12 && var13 != field2547[11]) { // L: 1181
                            var11 = 0; // L: 1182
                            var12 = field2601[11]; // L: 1183
                            var13 = field2547[11]; // L: 1184
                            var14 = field2604; // L: 1185
                        }

                        if (var11 < var12) { // L: 1187
                            var10 = var14[var11];
                        } else {
                            var10 = -1000; // L: 1188
                        }
                    }

                    var16 = field2601[var15]; // L: 1190
                    int[] var17 = field2547[var15]; // L: 1191

                    for (var18 = 0; var18 < var16; ++var18) { // L: 1192
                        this.drawFace(var17[var18]); // L: 1193
                    }
                }

                while (var10 != -1000) { // L: 1196
                    this.drawFace(var13[var11++]); // L: 1197
                    if (var11 == var12 && var13 != field2547[11]) { // L: 1198
                        var11 = 0; // L: 1199
                        var13 = field2547[11]; // L: 1200
                        var12 = field2601[11]; // L: 1201
                        var14 = field2604; // L: 1202
                    }

                    if (var11 < var12) { // L: 1204
                        var10 = var14[var11];
                    } else {
                        var10 = -1000; // L: 1205
                    }
                }

            }
        }
    } // L: 1116 1207

}
