package com.bamobile.fdtks.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;

import com.bamobile.fdtks.activities.MainActivity;
import com.bamobile.fdtks.databases.ImageDatabase;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


public class ImageHandler {

    private static String maxWidth = "0";
    private static ImageDatabase imgDatabase = null;
    private static ImageHandler _this;

    private ImageHandler() {
        if (ImageHandler.imgDatabase == null) {
            this.imgDatabase = new ImageDatabase(MainActivity.getInstance().getApplicationContext());
        }
    }

    public static ImageHandler getInstance(int maxWidth) {
        if (_this == null) {
            _this = new ImageHandler();
        }
        _this.setMaxWidth(maxWidth);

        return _this;
    }

    public static ImageHandler getInstance() {
        if (_this == null) {
            _this = new ImageHandler();
        }

        return _this;
    }

    private void setMaxWidth(int maxWidth) {
        this.maxWidth = Integer.toString(maxWidth);
    }

    public Drawable getD4Disk(String url) {
        byte[] fileData;
        if (url != null && url.length() > 0) {
            fileData = imgDatabase.getImageRawData(url);
            if (fileData != null)
                return new BitmapDrawable(Tools.decodeByteArray(fileData, 0, fileData.length));
            else return null;
        }
        return null;
    }

    public void getDrawable(String url, ImageCallBack cb) {
        if (url != null && url.length() > 0) {

            byte[] fd = null;
            if (imgDatabase.exists(url)) {
                fd = imgDatabase.getImageRawData(url);
            }

            if (fd == null) {
                final String imageUrl = url;
                final ImageCallBack fcb = cb;
                AsyncHttpClient client = new AsyncHttpClient();
                String[] allowedContentTypes = new String[]{"image/png", "image/jpeg", "image/gif"};
                client.get(url.replace(" ", "%20"), new BinaryHttpResponseHandler(allowedContentTypes) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                        Bitmap image = Tools.decodeByteArray(binaryData, 0,
                                binaryData.length);
                        imgDatabase.addImage(imageUrl, image);
                        if (fcb != null)
                            fcb.onSuccess(new BitmapDrawable(image));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                        fcb.onFail();
                    }

                });
            } else {
                cb.onSuccess(new BitmapDrawable(Tools.decodeByteArray(fd, 0, fd.length)));
            }
        } else {
            cb.onFail();
        }
    }

    public byte[] get_res(String url) {
        return get_res(url, false);
    }

    public byte[] get_res(String url, Boolean update) {
        if (url != null && url.length() > 0) {
            byte[] fd = null;
            if (imgDatabase.exists(url)) {
                fd = imgDatabase.getImageRawData(url);
            }
            if (fd == null || update) {
                byte[] content = Tools.getFileDataFromURL(url);
                if (content != null) {
                    imgDatabase.addImage(url, content);
                    return content;
                }
            } else
                return fd;
        }
        return null;
    }

    public void local_res(String url) {
        get_res(url);
    }

    public Drawable syncGetDrawable(String url) {
        byte[] imgdata = get_res(url);
        if (imgdata != null)
            return new BitmapDrawable(Tools.decodeByteArray(imgdata, 0, imgdata.length));
        else return null;
    }

    public Bitmap syncGetBitmap(String url) {

        byte[] imgdata = get_res(url);

        try {
            if (imgdata != null)
                return Tools.decodeByteArray(imgdata, 0, imgdata.length);
            else
                return null;
        } catch (Exception e) {
            return null;
        }
    }

    public Drawable getRoundedCornerDrawable(String url) {
        Bitmap bitmap = syncGetBitmap(url);
        if (bitmap == null)
            return null;

        Bitmap output = Tools.createBitmap(bitmap.getWidth(), bitmap.getHeight());

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        canvas = null;
        bitmap.recycle();
        bitmap = null;

        return new BitmapDrawable(output);
    }

    public Drawable gRFromCache(String url, int width, int height) {

        String str = url + Integer.toString(width) + Integer.toString(height);
        return getD4Disk(str);
    }

    public void saveRToCache(Bitmap bmp, String url, int width, int height) {

        String fileName = url + Integer.toString(width) + Integer.toString(height);

        if (!imgDatabase.exists(fileName)) {
            imgDatabase.addImage(fileName, bmp);
        }
    }

    public Drawable getRoundedCornerDrawableFixedSizeWithShadow(String url, int width, int height) {
        Drawable tmp = gRFromCache(url, width, height);
        if (tmp != null) {
            return tmp;
        }

        try {
            Bitmap bitmap = syncGetBitmap(url);
            if (bitmap == null) return null;

            //  bitmap.getHeight()
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height);

            LinearGradient grad = new LinearGradient(0, (int) height / 6, 0, (int) height / 3,
                    0xCC222222, 0x00222222, Shader.TileMode.CLAMP);

            Canvas c = new Canvas(bitmap);
            Paint p = new Paint();
            p.setStyle(Paint.Style.FILL);
            p.setShader(grad);
            c.drawRect(0, 0, width, height, p);

            grad = new LinearGradient(0, (int) 2 * height / 3, 0, (int) 5 * height / 6,
                    0x00222222, 0xCC222222, Shader.TileMode.CLAMP);
            p.setShader(grad);
            c.drawRect(0, 0, width, height, p);

            Bitmap output = Tools.createBitmap(width, height);
            Canvas canvas = new Canvas(output);
            final int color = 0xff000000;
            final Paint paint = new Paint();
            final Rect rect = new Rect(7, 7, width - 7, height - 7);
            final RectF rectF = new RectF(rect);
            final float roundPx = 12;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            paint.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            bitmap.recycle();
            bitmap = null;
            canvas = null;
            saveRToCache(output, url, width, height);
            return new BitmapDrawable(output);
        } catch (Exception e) {
            return null;
        }
    }

    public Drawable getRoundedCornerColorDrawableFixedSizeWithShadow(int bgcolor, int width, int height) {
        try {
            Bitmap bitmap = Tools.createBitmap(width, height);
            LinearGradient grad = new LinearGradient(0, (int) height / 6, 0, (int) height / 3,
                    0xCC222222, 0x00222222, Shader.TileMode.CLAMP);
            Canvas c = new Canvas(bitmap);
            c.drawColor(bgcolor);
            Paint p = new Paint();
            p.setStyle(Paint.Style.FILL);
            p.setShader(grad);
            c.drawRect(0, 0, width, height, p);

            grad = new LinearGradient(0, (int) 2 * height / 3, 0, (int) 5 * height / 6, 0x00222222,
                    0xCC222222, Shader.TileMode.CLAMP);
            p.setShader(grad);
            c.drawRect(0, 0, width, height, p);

            Bitmap output = Tools.createBitmap(width, height);
            Canvas canvas = new Canvas(output);
            final int color = 0xff000000;
            final Paint paint = new Paint();
            final Rect rect = new Rect(7, 7, width - 7, height - 7);
            final RectF rectF = new RectF(rect);
            final float roundPx = 12;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            paint.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            canvas = null;
            bitmap.recycle();
            bitmap = null;
            return new BitmapDrawable(output);
        } catch (Exception e) {
            return null;
        }
    }
}
