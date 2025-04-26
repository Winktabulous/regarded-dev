package com.zenyte.game.world.region;

import com.zenyte.utils.efficientarea.Polygon;

import java.awt.*;
import java.util.Arrays;

/**
 * @author Kris | 30/12/2018 14:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EfficientPolygon extends Polygon {

    EfficientPolygon(int[] xpoints, int[] ypoints, int npoints) {
        // Fix 4489009: should throw IndexOutofBoundsException instead
        // of OutofMemoryException if npoints is huge and > {x,y}points.length
        if (npoints > xpoints.length || npoints > ypoints.length) {
            throw new IndexOutOfBoundsException("npoints > xpoints.length || " +
                    "npoints > ypoints.length");
        }
        // Fix 6191114: should throw NegativeArraySizeException with
        // negative npoints
        if (npoints < 0) {
            throw new NegativeArraySizeException("npoints < 0");
        }
        // Fix 6343431: Applet compatibility problems if arrays are not
        // exactly npoints in length
        this.npoints = npoints;
        this.xpoints = Arrays.copyOf(xpoints, npoints);
        this.ypoints = Arrays.copyOf(ypoints, npoints);
        this.boundingBox = getBounds();
    }

    private final Rectangle boundingBox;

    @Override
    @Deprecated
    public void addPoint(int x, int y) {
        throw new IllegalStateException();
    }

    @Override
    public boolean contains(double x, double y) {
        if (npoints <= 2 || !boundingBox.contains(x, y)) {
            return false;
        }
        int hits = 0;

        int lastx = xpoints[npoints - 1];
        int lasty = ypoints[npoints - 1];
        int curx, cury;

        // Walk the edges of the polygon
        for (int i = 0; i < npoints; lastx = curx, lasty = cury, i++) {
            curx = xpoints[i];
            cury = ypoints[i];

            if (cury == lasty) {
                continue;
            }

            int leftx;
            if (curx < lastx) {
                if (x >= lastx) {
                    continue;
                }
                leftx = curx;
            } else {
                if (x >= curx) {
                    continue;
                }
                leftx = lastx;
            }

            double test1, test2;
            if (cury < lasty) {
                if (y < cury || y >= lasty) {
                    continue;
                }
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - curx;
                test2 = y - cury;
            } else {
                if (y < lasty || y >= cury) {
                    continue;
                }
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - lastx;
                test2 = y - lasty;
            }

            if (test1 < (test2 / (lasty - cury) * (lastx - curx))) {
                hits++;
            }
        }

        return ((hits & 1) != 0);
    }

}
