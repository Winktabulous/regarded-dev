package mgi.utilities;

public final class ColorUtils {

    public static int rgbToHSL16(int red, int green, int blue) {
        final int rgb = ((red & 255) << 16) | ((green & 255) << 8) | (blue & 255);
        return rgbToHSL16(rgb);
    }

    public static int rgbToHSL16(int rgb) {
        double r = (double) (rgb >> 16 & 255) / 256.0;
        double g = (double) (rgb >> 8 & 255) / 256.0;
        double b = (double) (rgb & 255) / 256.0;
        double min = r;
        if (g < min) {
            min = g;
        }
        if (b < min) {
            min = b;
        }
        double d_13_ = r;
        if (g > d_13_) {
            d_13_ = g;
        }
        if (b > d_13_) {
            d_13_ = b;
        }
        double d_14_ = 0.0;
        double d_15_ = 0.0;
        double d_16_ = (min + d_13_) / 2.0;
        if (min != d_13_) {
            if (d_16_ < 0.5) {
                d_15_ = (d_13_ - min) / (d_13_ + min);
            }
            if (d_16_ >= 0.5) {
                d_15_ = (d_13_ - min) / (2.0 - d_13_ - min);
            }
            if (r == d_13_) {
                d_14_ = (g - b) / (d_13_ - min);
            } else if (d_13_ == g) {
                d_14_ = 2.0 + (b - r) / (d_13_ - min);
            } else if (d_13_ == b) {
                d_14_ = 4.0 + (r - g) / (d_13_ - min);
            }
        }
        d_14_ /= 6.0;
        int h = (int) (256.0 * d_14_);
        int s = (int) (256.0 * d_15_);
        int l = (int) (256.0 * d_16_);
        if (s < 0) {
            s = 0;
        } else if (s > 255) {
            s = 255;
        }
        if (l < 0) {
            l = 0;
        } else if (l > 255) {
            l = 255;
        }
        if (l > 243) {
            s >>= 4;
        } else if (l > 217) {
            s >>= 3;
        } else if (l > 192) {
            s >>= 2;
        } else if (l > 179) {
            s >>= 1;
        }
        return ((l >> 1) + (((h & 255) >> 2 << 10) + (s >> 5 << 7)));
    }
    
}
