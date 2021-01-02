package com.openclassrooms.realestatemanager.presentation.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class BitmapConverterUtil {

    public static <T> Bitmap getBitmapFromVectorOrDrawable(Context context, T resToConvert){
        Bitmap bitmap;

        if (resToConvert instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) resToConvert;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
            if (bitmapDrawable.getIntrinsicWidth() <= 0 || bitmapDrawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(bitmapDrawable.getIntrinsicWidth(), bitmapDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            bitmapDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            bitmapDrawable.draw(canvas);
            return bitmap;
        }

        else if(resToConvert instanceof Integer){

            int resourceId = (Integer) resToConvert;
            Drawable drawable = ContextCompat.getDrawable(context, resourceId);
            assert drawable != null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                drawable = (DrawableCompat.wrap(drawable)).mutate();

            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        }

        else return null;
    }
}
