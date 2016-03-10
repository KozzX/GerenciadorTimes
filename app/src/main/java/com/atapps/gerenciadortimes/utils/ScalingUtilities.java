package com.atapps.gerenciadortimes.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by ANDRE on 06/03/2016.
 */
public class ScalingUtilities {

    public static Bitmap reduzirQualidade(String path, String tipo) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        bitmap = cropToSquare(bitmap);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Log.d("IMAGEMTAM",w + " " + h);
        if ((w > 300)&&(tipo.equals("TOP"))){
            w = 300;
            h = 300;
        }else if ((w > 60)&&(tipo.equals("CIRCLE"))){
            w = 60;
            h = 60;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);

        Log.d("IMAGEMTAM", bitmap.getHeight() + " " + bitmap.getWidth() + " " + bitmap.getByteCount() + " " + bitmap.getDensity());

        return bitmap;
    }

    public static Bitmap cropToSquare(Bitmap bitmap){
        Log.d("IMAGEMTAM", bitmap.getHeight() + " " + bitmap.getWidth() + " " + bitmap.getByteCount());
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        Log.d("IMAGEMTAM", cropImg.getHeight() + " " + cropImg.getWidth() + " " + cropImg.getByteCount());

        return cropImg;
    }

    /*public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 80;
        int targetHeight = 100;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }*/
}
