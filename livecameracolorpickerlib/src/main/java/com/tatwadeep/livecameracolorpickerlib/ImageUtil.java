package com.tatwadeep.livecameracolorpickerlib;

/**
 * Created by elesh baraiya 27-03-2019
 */

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtil {
    /**
     * Rotate Bitmap
     *
     * @param b
     * @param rotateDegree
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }
}