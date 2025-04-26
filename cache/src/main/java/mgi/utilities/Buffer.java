package mgi.utilities;

public class Buffer {

    static int[] crc32Table;
    static long[] crc64Table;

    public byte[] array;
    public int offset;

    static {
        crc32Table = new int[256]; // L: 16

        int var2;
        for (int var1 = 0; var1 < 256; ++var1) { // L: 21
            int var4 = var1; // L: 22

            for (var2 = 0; var2 < 8; ++var2) { // L: 23
                if ((var4 & 1) == 1) { // L: 24
                    var4 = var4 >>> 1 ^ -306674912;
                } else {
                    var4 >>>= 1; // L: 25
                }
            }

            crc32Table[var1] = var4; // L: 27
        }

        crc64Table = new long[256]; // L: 31

        for (var2 = 0; var2 < 256; ++var2) { // L: 36
            long var0 = (long) var2; // L: 37

            for (int var3 = 0; var3 < 8; ++var3) { // L: 38
                if (1L == (var0 & 1L)) { // L: 39
                    var0 = var0 >>> 1 ^ -3932672073523589310L;
                } else {
                    var0 >>>= 1; // L: 40
                }
            }

            crc64Table[var2] = var0; // L: 42
        }

    } // L: 44

    public Buffer(byte[] var1) {
        this.array = var1; // L: 65
        this.offset = 0; // L: 66
    } // L: 67

    public void writeByte(int var1) {
        this.array[++this.offset - 1] = (byte) var1; // L: 75
    } // L: 76

    public void writeShort(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 >> 8); // L: 79
        this.array[++this.offset - 1] = (byte) var1; // L: 80
    } // L: 81

    public void writeMedium(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 >> 16); // L: 84
        this.array[++this.offset - 1] = (byte) (var1 >> 8); // L: 85
        this.array[++this.offset - 1] = (byte) var1; // L: 86
    } // L: 87

    public void writeInt(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 >> 24); // L: 90
        this.array[++this.offset - 1] = (byte) (var1 >> 16); // L: 91
        this.array[++this.offset - 1] = (byte) (var1 >> 8); // L: 92
        this.array[++this.offset - 1] = (byte) var1; // L: 93
    } // L: 94


    public void writeLongMedium(long var1) {
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 40)); // L: 97
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 32)); // L: 98
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 24)); // L: 99
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 16)); // L: 100
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 8)); // L: 101
        this.array[++this.offset - 1] = (byte) ((int) var1); // L: 102
    } // L: 103


    public void writeLong(long var1) {
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 56)); // L: 106
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 48)); // L: 107
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 40)); // L: 108
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 32)); // L: 109
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 24)); // L: 110
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 16)); // L: 111
        this.array[++this.offset - 1] = (byte) ((int) (var1 >> 8)); // L: 112
        this.array[++this.offset - 1] = (byte) ((int) var1); // L: 113
    } // L: 114

    public void writeBoolean(boolean var1) {
        this.writeByte(var1 ? 1 : 0); // L: 117
    } // L: 118

    public void writeStringCp1252NullTerminated(String var1) {
        int var2 = var1.indexOf(0); // L: 125
        if (var2 >= 0) { // L: 126
            throw new IllegalArgumentException("");
        } else {
            this.offset += encodeStringCp1252(var1, 0, var1.length(), this.array, this.offset); // L: 127
            this.array[++this.offset - 1] = 0; // L: 128
        }
    } // L: 129
    public static int encodeStringCp1252(CharSequence var0, int var1, int var2, byte[] var3, int var4) {
        int var5 = var2 - var1; // L: 45

        for (int var6 = 0; var6 < var5; ++var6) { // L: 46
            char var7 = var0.charAt(var6 + var1); // L: 47
            if (var7 > 0 && var7 < 128 || var7 >= 160 && var7 <= 255) { // L: 48
                var3[var6 + var4] = (byte)var7;
            } else if (var7 == 8364) { // L: 49
                var3[var6 + var4] = -128;
            } else if (var7 == 8218) { // L: 50
                var3[var6 + var4] = -126;
            } else if (var7 == 402) { // L: 51
                var3[var6 + var4] = -125;
            } else if (var7 == 8222) { // L: 52
                var3[var6 + var4] = -124;
            } else if (var7 == 8230) { // L: 53
                var3[var6 + var4] = -123;
            } else if (var7 == 8224) { // L: 54
                var3[var6 + var4] = -122;
            } else if (var7 == 8225) { // L: 55
                var3[var6 + var4] = -121;
            } else if (var7 == 710) {
                var3[var6 + var4] = -120; // L: 56
            } else if (var7 == 8240) { // L: 57
                var3[var6 + var4] = -119;
            } else if (var7 == 352) { // L: 58
                var3[var6 + var4] = -118;
            } else if (var7 == 8249) { // L: 59
                var3[var6 + var4] = -117;
            } else if (var7 == 338) { // L: 60
                var3[var6 + var4] = -116;
            } else if (var7 == 381) { // L: 61
                var3[var6 + var4] = -114;
            } else if (var7 == 8216) { // L: 62
                var3[var6 + var4] = -111;
            } else if (var7 == 8217) { // L: 63
                var3[var6 + var4] = -110;
            } else if (var7 == 8220) { // L: 64
                var3[var6 + var4] = -109;
            } else if (var7 == 8221) { // L: 65
                var3[var6 + var4] = -108;
            } else if (var7 == 8226) { // L: 66
                var3[var6 + var4] = -107;
            } else if (var7 == 8211) { // L: 67
                var3[var6 + var4] = -106;
            } else if (var7 == 8212) {
                var3[var6 + var4] = -105; // L: 68
            } else if (var7 == 732) { // L: 69
                var3[var6 + var4] = -104;
            } else if (var7 == 8482) { // L: 70
                var3[var6 + var4] = -103;
            } else if (var7 == 353) { // L: 71
                var3[var6 + var4] = -102;
            } else if (var7 == 8250) { // L: 72
                var3[var6 + var4] = -101;
            } else if (var7 == 339) { // L: 73
                var3[var6 + var4] = -100;
            } else if (var7 == 382) { // L: 74
                var3[var6 + var4] = -98;
            } else if (var7 == 376) { // L: 75
                var3[var6 + var4] = -97;
            } else {
                var3[var6 + var4] = 63; // L: 76
            }
        }

        return var5; // L: 78
    }

    public void writeStringCp1252NullCircumfixed(String var1) {
        int var2 = var1.indexOf(0); // L: 132
        if (var2 >= 0) { // L: 133
            throw new IllegalArgumentException("");
        } else {
            this.array[++this.offset - 1] = 0; // L: 134
            this.offset += encodeStringCp1252(var1, 0, var1.length(), this.array, this.offset); // L: 135
            this.array[++this.offset - 1] = 0; // L: 136
        }
    } // L: 137

    public void writeCESU8(CharSequence var1) {
        int var3 = var1.length(); // L: 142
        int var4 = 0; // L: 143

        int var5;
        for (var5 = 0; var5 < var3; ++var5) { // L: 144
            char var12 = var1.charAt(var5); // L: 145
            if (var12 <= 127) { // L: 146
                ++var4;
            } else if (var12 <= 2047) { // L: 147
                var4 += 2;
            } else {
                var4 += 3; // L: 148
            }
        }

        this.array[++this.offset - 1] = 0; // L: 153
        this.writeVarInt(var4); // L: 154
        var4 = this.offset * -2117273951; // L: 155
        byte[] var6 = this.array; // L: 157
        int var7 = this.offset; // L: 158
        int var8 = var1.length(); // L: 160
        int var9 = var7; // L: 161

        for (int var10 = 0; var10 < var8; ++var10) { // L: 162
            char var11 = var1.charAt(var10); // L: 163
            if (var11 <= 127) { // L: 164
                var6[var9++] = (byte) var11; // L: 165
            } else if (var11 <= 2047) { // L: 167
                var6[var9++] = (byte) (192 | var11 >> 6); // L: 168
                var6[var9++] = (byte) (128 | var11 & '?'); // L: 169
            } else {
                var6[var9++] = (byte) (224 | var11 >> '\f'); // L: 172
                var6[var9++] = (byte) (128 | var11 >> 6 & 63); // L: 173
                var6[var9++] = (byte) (128 | var11 & '?'); // L: 174
            }
        }

        var5 = var9 - var7; // L: 177
        this.offset = (var5 * -2117273951 + var4) * -271291039; // L: 179
    } // L: 180

    public void writeBytes(byte[] var1, int var2, int var3) {
        for (int var4 = var2; var4 < var3 + var2; ++var4) { // L: 183
            this.array[++this.offset - 1] = var1[var4];
        }

    } // L: 184

    public void writeBytes(Buffer var1) {
        this.writeBytes(var1.array, 0, var1.offset); // L: 187
    } // L: 188

    public void writeLengthInt(int var1) {
        if (var1 < 0) { // L: 191
            throw new IllegalArgumentException(); // L: 192
        } else {
            this.array[this.offset - var1 - 4] = (byte) (var1 >> 24); // L: 194
            this.array[this.offset - var1 - 3] = (byte) (var1 >> 16); // L: 195
            this.array[this.offset - var1 - 2] = (byte) (var1 >> 8); // L: 196
            this.array[this.offset - var1 - 1] = (byte) var1; // L: 197
        }
    } // L: 198

    public void writeLengthShort(int var1) {
        if (var1 >= 0 && var1 <= 65535) { // L: 201
            this.array[this.offset - var1 - 2] = (byte) (var1 >> 8); // L: 204
            this.array[this.offset - var1 - 1] = (byte) var1; // L: 205
        } else {
            throw new IllegalArgumentException(); // L: 202
        }
    } // L: 206

    public void method7740(int var1) {
        if (var1 >= 0 && var1 <= 255) { // L: 209
            this.array[this.offset - var1 - 1] = (byte) var1; // L: 212
        } else {
            throw new IllegalArgumentException(); // L: 210
        }
    } // L: 213

    public void writeSmartByteShort(int var1) {
        if (var1 >= 0 && var1 < 128) { // L: 216
            this.writeByte(var1); // L: 217
        } else if (var1 >= 0 && var1 < 32768) { // L: 220
            this.writeShort(var1 + 32768); // L: 221
        } else {
            throw new IllegalArgumentException(); // L: 224
        }
    } // L: 218 222

    public void writeVarInt(int var1) {
        if ((var1 & -128) != 0) { // L: 228
            if ((var1 & -16384) != 0) { // L: 229
                if ((var1 & -2097152) != 0) { // L: 230
                    if ((var1 & -268435456) != 0) { // L: 231
                        this.writeByte(var1 >>> 28 | 128);
                    }

                    this.writeByte(var1 >>> 21 | 128); // L: 232
                }

                this.writeByte(var1 >>> 14 | 128); // L: 234
            }

            this.writeByte(var1 >>> 7 | 128); // L: 236
        }

        this.writeByte(var1 & 127); // L: 238
    } // L: 239

    public int readUnsignedByte() {
        return this.array[++this.offset - 1] & 255; // L: 242
    }

    public byte readByte() {
        return this.array[++this.offset - 1]; // L: 246
    }

    public int readUnsignedShort() {
        this.offset += 2; // L: 250
        return (this.array[this.offset - 1] & 255) + ((this.array[this.offset - 2] & 255) << 8); // L: 251
    }

    public int readShort() {
        this.offset += 2; // L: 255
        int var1 = (this.array[this.offset - 1] & 255) + ((this.array[this.offset - 2] & 255) << 8); // L: 256
        if (var1 > 32767) { // L: 257
            var1 -= 65536;
        }

        return var1; // L: 258
    }

    public int readMedium() {
        this.offset += 3; // L: 262
        return ((this.array[this.offset - 3] & 255) << 16) + (this.array[this.offset - 1] & 255) + ((this.array[this.offset - 2] & 255) << 8); // L: 263
    }

    public int readInt() {
        this.offset += 4; // L: 267
        return ((this.array[this.offset - 3] & 255) << 16) + (this.array[this.offset - 1] & 255) + ((this.array[this.offset - 2] & 255) << 8) + ((this.array[this.offset - 4] & 255) << 24); // L: 268
    }

    public long readLong() {
        long var1 = (long) this.readInt() & 4294967295L; // L: 272
        long var3 = (long) this.readInt() & 4294967295L; // L: 273
        return (var1 << 32) + var3; // L: 274
    }

    public float readFloat() {
        return Float.intBitsToFloat(this.readInt()); // L: 278
    }

    public boolean readBoolean() {
        return (this.readUnsignedByte() & 1) == 1; // L: 282
    }

    public String readStringCp1252NullTerminatedOrNull() {
        if (this.array[this.offset] == 0) { // L: 286
            ++this.offset; // L: 287
            return null; // L: 288
        } else {
            return this.readStringCp1252NullTerminated(); // L: 290
        }
    }

    public String readStringCp1252NullTerminated() {
        int var1 = this.offset; // L: 294

        while (this.array[++this.offset - 1] != 0) { // L: 295
        }

        int var2 = this.offset - var1 - 1; // L: 296
        return var2 == 0 ? "" : decodeStringCp1252(this.array, var1, var2); // L: 297 298
    }

    public static final char[] cp1252AsciiExtension= new char[]{'€', '\u0000', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '\u0000', 'Ž', '\u0000', '\u0000', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '\u0000', 'ž', 'Ÿ'}; // L: 4

    public static String decodeStringCp1252(byte[] var0, int var1, int var2) {
        char[] var3 = new char[var2]; // L: 82
        int var4 = 0; // L: 83

        for (int var5 = 0; var5 < var2; ++var5) { // L: 84
            int var6 = var0[var5 + var1] & 255; // L: 85
            if (var6 != 0) { // L: 86
                if (var6 >= 128 && var6 < 160) { // L: 87
                    char var7 = cp1252AsciiExtension[var6 - 128]; // L: 88
                    if (var7 == 0) { // L: 89
                        var7 = '?';
                    }

                    var6 = var7; // L: 90
                }

                var3[var4++] = (char)var6; // L: 92
            }
        }

        return new String(var3, 0, var4); // L: 94
    }
    public String readStringCp1252NullCircumfixed() {
        byte var1 = this.array[++this.offset - 1]; // L: 302
        if (var1 != 0) { // L: 303
            throw new IllegalStateException("");
        } else {
            int var2 = this.offset; // L: 304

            while (this.array[++this.offset - 1] != 0) { // L: 305
            }

            int var3 = this.offset - var2 - 1; // L: 306
            return var3 == 0 ? "" : decodeStringCp1252(this.array, var2, var3); // L: 307 308
        }
    }

    public String readCESU8() {
        byte var1 = this.array[++this.offset - 1]; // L: 312
        if (var1 != 0) { // L: 313
            throw new IllegalStateException("");
        } else {
            int var2 = this.readVarInt(); // L: 314
            if (var2 + this.offset > this.array.length) { // L: 315
                throw new IllegalStateException("");
            } else {
                byte[] var4 = this.array; // L: 317
                int var5 = this.offset; // L: 318
                char[] var6 = new char[var2]; // L: 320
                int var7 = 0; // L: 321
                int var8 = var5; // L: 322

                int var11;
                for (int var9 = var5 + var2; var8 < var9; var6[var7++] = (char) var11) { // L: 323 324 355
                    int var10 = var4[var8++] & 255; // L: 325
                    if (var10 < 128) { // L: 327
                        if (var10 == 0) { // L: 328
                            var11 = 65533;
                        } else {
                            var11 = var10; // L: 329
                        }
                    } else if (var10 < 192) { // L: 331
                        var11 = 65533;
                    } else if (var10 < 224) { // L: 332
                        if (var8 < var9 && (var4[var8] & 192) == 128) { // L: 333
                            var11 = (var10 & 31) << 6 | var4[var8++] & 63; // L: 334
                            if (var11 < 128) { // L: 335
                                var11 = 65533;
                            }
                        } else {
                            var11 = 65533; // L: 337
                        }
                    } else if (var10 < 240) { // L: 339
                        if (var8 + 1 < var9 && (var4[var8] & 192) == 128 && (var4[var8 + 1] & 192) == 128) { // L: 340
                            var11 = (var10 & 15) << 12 | (var4[var8++] & 63) << 6 | var4[var8++] & 63; // L: 341
                            if (var11 < 2048) { // L: 342
                                var11 = 65533;
                            }
                        } else {
                            var11 = 65533; // L: 344
                        }
                    } else if (var10 < 248) { // L: 346
                        if (var8 + 2 < var9 && (var4[var8] & 192) == 128 && (var4[var8 + 1] & 192) == 128 && (var4[var8 + 2] & 192) == 128) { // L: 347
                            var11 = (var10 & 7) << 18 | (var4[var8++] & 63) << 12 | (var4[var8++] & 63) << 6 | var4[var8++] & 63; // L: 348
                            if (var11 >= 65536 && var11 <= 1114111) { // L: 349
                                var11 = 65533; // L: 350
                            } else {
                                var11 = 65533;
                            }
                        } else {
                            var11 = 65533; // L: 352
                        }
                    } else {
                        var11 = 65533; // L: 354
                    }
                }

                String var3 = new String(var6, 0, var7); // L: 357
                this.offset += var2; // L: 360
                return var3; // L: 361
            }
        }
    }

    public void readBytes(byte[] var1, int var2, int var3) {
        for (int var4 = var2; var4 < var3 + var2; ++var4) {
            var1[var4] = this.array[++this.offset - 1]; // L: 365
        }

    } // L: 366

    public int readShortSmart() {
        int var1 = this.array[this.offset] & 255; // L: 369
        return var1 < 128 ? this.readUnsignedByte() - 64 : this.readUnsignedShort() - 49152; // L: 370 371
    }

    public int readUShortSmart() {
        int var1 = this.array[this.offset] & 255; // L: 375
        return var1 < 128 ? this.readUnsignedByte() : this.readUnsignedShort() - 32768; // L: 376 377
    }

    public int readExtendedSmart() {
        int var1 = 0; // L: 381

        int var2;
        for (var2 = this.readUShortSmart(); var2 == 32767; var2 = this.readUShortSmart()) { // L: 382 383 385
            var1 += 32767; // L: 384
        }

        var1 += var2; // L: 387
        return var1; // L: 388
    }

    public int readUIntSmart() {
        return this.array[this.offset] < 0 ? this.readInt() & Integer.MAX_VALUE : this.readUnsignedShort(); // L: 392 393
    }

    public int readIntSmart() {
        if (this.array[this.offset] < 0) { // L: 397
            return this.readInt() & Integer.MAX_VALUE;
        } else {
            int var1 = this.readUnsignedShort(); // L: 398
            return var1 == 32767 ? -1 : var1; // L: 399
        }
    }

    public int readVarInt() {
        byte var1 = this.array[++this.offset - 1]; // L: 404

        int var2;
        for (var2 = 0; var1 < 0; var1 = this.array[++this.offset - 1]) { // L: 405 406 408
            var2 = (var2 | var1 & 127) << 7; // L: 407
        }

        return var2 | var1; // L: 410
    }

    public void xteaEncryptAll(int[] var1) {
        int var2 = this.offset / 8; // L: 414
        this.offset = 0; // L: 415

        for (int var3 = 0; var3 < var2; ++var3) { // L: 416
            int var4 = this.readInt(); // L: 417
            int var5 = this.readInt(); // L: 418
            int var6 = 0; // L: 419
            int var7 = -1640531527; // L: 420

            for (int var8 = 32; var8-- > 0; var5 += var4 + (var4 << 4 ^ var4 >>> 5) ^ var1[var6 >>> 11 & 3] + var6) { // L: 421 422 425
                var4 += var5 + (var5 << 4 ^ var5 >>> 5) ^ var6 + var1[var6 & 3]; // L: 423
                var6 += var7; // L: 424
            }

            this.offset -= 8; // L: 427
            this.writeInt(var4); // L: 428
            this.writeInt(var5); // L: 429
        }

    } // L: 431


    public void xteaDecryptAll(int[] var1) {
        int var2 = this.offset / 8; // L: 434
        this.offset = 0; // L: 435

        for (int var3 = 0; var3 < var2; ++var3) { // L: 436
            int var4 = this.readInt(); // L: 437
            int var5 = this.readInt(); // L: 438
            int var6 = -957401312; // L: 439
            int var7 = -1640531527; // L: 440

            for (int var8 = 32; var8-- > 0; var4 -= var5 + (var5 << 4 ^ var5 >>> 5) ^ var6 + var1[var6 & 3]) { // L: 441 442 445
                var5 -= var4 + (var4 << 4 ^ var4 >>> 5) ^ var1[var6 >>> 11 & 3] + var6; // L: 443
                var6 -= var7; // L: 444
            }

            this.offset -= 8; // L: 447
            this.writeInt(var4); // L: 448
            this.writeInt(var5); // L: 449
        }

    } // L: 451

    public void xteaEncrypt(int[] var1, int var2, int var3) {
        int var4 = this.offset; // L: 454
        this.offset = var2; // L: 455
        int var5 = (var3 - var2) / 8; // L: 456

        for (int var6 = 0; var6 < var5; ++var6) { // L: 457
            int var7 = this.readInt(); // L: 458
            int var8 = this.readInt(); // L: 459
            int var9 = 0; // L: 460
            int var10 = -1640531527; // L: 461

            for (int var11 = 32; var11-- > 0; var8 += var7 + (var7 << 4 ^ var7 >>> 5) ^ var1[var9 >>> 11 & 3] + var9) { // L: 462 463 466
                var7 += var8 + (var8 << 4 ^ var8 >>> 5) ^ var9 + var1[var9 & 3]; // L: 464
                var9 += var10; // L: 465
            }

            this.offset -= 8; // L: 468
            this.writeInt(var7); // L: 469
            this.writeInt(var8); // L: 470
        }

        this.offset = var4; // L: 472
    } // L: 473

    public void xteaDecrypt(int[] var1, int var2, int var3) {
        int var4 = this.offset; // L: 476
        this.offset = var2; // L: 477
        int var5 = (var3 - var2) / 8; // L: 478

        for (int var6 = 0; var6 < var5; ++var6) { // L: 479
            int var7 = this.readInt(); // L: 480
            int var8 = this.readInt(); // L: 481
            int var9 = -957401312; // L: 482
            int var10 = -1640531527; // L: 483

            for (int var11 = 32; var11-- > 0; var7 -= var8 + (var8 << 4 ^ var8 >>> 5) ^ var9 + var1[var9 & 3]) { // L: 484 485 488
                var8 -= var7 + (var7 << 4 ^ var7 >>> 5) ^ var1[var9 >>> 11 & 3] + var9; // L: 486
                var9 -= var10; // L: 487
            }

            this.offset -= 8; // L: 490
            this.writeInt(var7); // L: 491
            this.writeInt(var8); // L: 492
        }

        this.offset = var4; // L: 494
    } // L: 495


    public void writeByteAdd(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 + 128); // L: 525
    } // L: 526

    public void writeByteNeg(int var1) {
        this.array[++this.offset - 1] = (byte) (0 - var1); // L: 529
    } // L: 530

    public void writeByteSub(int var1) {
        this.array[++this.offset - 1] = (byte) (128 - var1); // L: 533
    } // L: 534

    public int readUnsignedByteAdd() {
        return this.array[++this.offset - 1] - 128 & 255; // L: 537
    }

    public int readUnsignedByteNeg() {
        return 0 - this.array[++this.offset - 1] & 255; // L: 541
    }

    public int readUnsignedByteSub() {
        return 128 - this.array[++this.offset - 1] & 255; // L: 545
    }

    public byte readByteAdd() {
        return (byte) (this.array[++this.offset - 1] - 128); // L: 549
    }

    public byte readByteNeg() {
        return (byte) (0 - this.array[++this.offset - 1]); // L: 553
    }

    public byte readByteSub() {
        return (byte) (128 - this.array[++this.offset - 1]); // L: 557
    }

    public void writeShortLE(int var1) {
        this.array[++this.offset - 1] = (byte) var1; // L: 561
        this.array[++this.offset - 1] = (byte) (var1 >> 8); // L: 562
    } // L: 563

    public void writeShortAdd(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 >> 8); // L: 566
        this.array[++this.offset - 1] = (byte) (var1 + 128); // L: 567
    } // L: 568

    public void writeShortAddLE(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 + 128); // L: 571
        this.array[++this.offset - 1] = (byte) (var1 >> 8); // L: 572
    } // L: 573

    public int readUnsignedShortLE() {
        this.offset += 2; // L: 576
        return ((this.array[this.offset - 1] & 255) << 8) + (this.array[this.offset - 2] & 255); // L: 577
    }

    public int readUnsignedShortAdd() {
        this.offset += 2; // L: 581
        return (this.array[this.offset - 1] - 128 & 255) + ((this.array[this.offset - 2] & 255) << 8); // L: 582
    }

    public int readUnsignedShortAddLE() {
        this.offset += 2; // L: 586
        return ((this.array[this.offset - 1] & 255) << 8) + (this.array[this.offset - 2] - 128 & 255); // L: 587
    }

    public int readShortLE() {
        this.offset += 2; // L: 591
        int var1 = ((this.array[this.offset - 1] & 255) << 8) + (this.array[this.offset - 2] & 255); // L: 592
        if (var1 > 32767) { // L: 593
            var1 -= 65536;
        }

        return var1; // L: 594
    }

    public int readShortAdd() {
        this.offset += 2; // L: 598
        int var1 = (this.array[this.offset - 1] - 128 & 255) + ((this.array[this.offset - 2] & 255) << 8); // L: 599
        if (var1 > 32767) { // L: 600
            var1 -= 65536;
        }

        return var1; // L: 601
    }

    public int readShortAddLE() {
        this.offset += 2; // L: 605
        int var1 = ((this.array[this.offset - 1] & 255) << 8) + (this.array[this.offset - 2] - 128 & 255); // L: 606
        if (var1 > 32767) { // L: 607
            var1 -= 65536;
        }

        return var1; // L: 608
    }

    public void writeMediumLE(int var1) {
        this.array[++this.offset - 1] = (byte) var1; // L: 612
        this.array[++this.offset - 1] = (byte) (var1 >> 8); // L: 613
        this.array[++this.offset - 1] = (byte) (var1 >> 16); // L: 614
    } // L: 615

    public int readUnsignedMediumLE() {
        this.offset += 3; // L: 618
        return (this.array[this.offset - 3] & 255) + ((this.array[this.offset - 2] & 255) << 8) + ((this.array[this.offset - 1] & 255) << 16); // L: 619
    }

    public int readUnsignedMediumRME() {
        this.offset += 3; // L: 623
        return ((this.array[this.offset - 1] & 255) << 8) + ((this.array[this.offset - 3] & 255) << 16) + (this.array[this.offset - 2] & 255); // L: 624
    }

    public int readUnsignedMediumME() {
        this.offset += 3; // L: 628
        return (this.array[this.offset - 1] & 255) + ((this.array[this.offset - 3] & 255) << 8) + ((this.array[this.offset - 2] & 255) << 16); // L: 629
    }

    public void writeIntLE(int var1) {
        this.array[++this.offset - 1] = (byte) var1; // L: 633
        this.array[++this.offset - 1] = (byte) (var1 >> 8); // L: 634
        this.array[++this.offset - 1] = (byte) (var1 >> 16); // L: 635
        this.array[++this.offset - 1] = (byte) (var1 >> 24); // L: 636
    } // L: 637

    public void writeIntME(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 >> 8); // L: 640
        this.array[++this.offset - 1] = (byte) var1; // L: 641
        this.array[++this.offset - 1] = (byte) (var1 >> 24); // L: 642
        this.array[++this.offset - 1] = (byte) (var1 >> 16); // L: 643
    } // L: 644

    public void writeIntIME(int var1) {
        this.array[++this.offset - 1] = (byte) (var1 >> 16); // L: 647
        this.array[++this.offset - 1] = (byte) (var1 >> 24); // L: 648
        this.array[++this.offset - 1] = (byte) var1; // L: 649
        this.array[++this.offset - 1] = (byte) (var1 >> 8); // L: 650
    } // L: 651

    public int readIntLE() {
        this.offset += 4; // L: 654
        return (this.array[this.offset - 4] & 255) + ((this.array[this.offset - 3] & 255) << 8) + ((this.array[this.offset - 2] & 255) << 16) + ((this.array[this.offset - 1] & 255) << 24); // L: 655
    }

    public int readIntME() {
        this.offset += 4; // L: 659
        return ((this.array[this.offset - 2] & 255) << 24) + ((this.array[this.offset - 4] & 255) << 8) + (this.array[this.offset - 3] & 255) + ((this.array[this.offset - 1] & 255) << 16); // L: 660
    }

    public int readIntIME() {
        this.offset += 4; // L: 664
        return ((this.array[this.offset - 1] & 255) << 8) + ((this.array[this.offset - 4] & 255) << 16) + (this.array[this.offset - 2] & 255) + ((this.array[this.offset - 3] & 255) << 24); // L: 665
    }

    public void readBytesReversed(byte[] var1, int var2, int var3) {
        for (int var4 = var3 + var2 - 1; var4 >= var2; --var4) {
            var1[var4] = this.array[++this.offset - 1]; // L: 669
        }

    } // L: 670
}
