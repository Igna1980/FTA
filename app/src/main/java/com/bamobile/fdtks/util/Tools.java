package com.bamobile.fdtks.util;

/**
 * MicroJabber, jabber for light java devices. Copyright (C) 2004, Gregoire Athanase
 * This library is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with 
 * this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, 
 * Suite 330, Boston, MA 02111-1307 USA.
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bamobile.fdtks.R;
import com.bamobile.fdtks.activities.MainActivity;
import com.bamobile.fdtks.activities.SplashActivity;


public class Tools {

    /**
     * Returns a SHA1 digest of the given string, in hex values lowercase.
     *
     * @param _str
     */
    public static String sha1(String _str) {
        String res;
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String tmp = _str;
        byte in[] = tmp.getBytes();
        digest.update(in, 0, in.length);
        byte out[] = digest.digest();

        // builds the hex string (lowercase)
        res = "";
        tmp = ""; // tmp = two characters to append to the hex string
        for (int i = 0; i < 20; i++) {
            int unsigned = out[i];
            if (out[i] < 0) {
                unsigned += 256;
            }
            tmp = Integer.toHexString(unsigned);
            if (tmp.length() == 1) {
                tmp = "0" + tmp;
            }
            res = res + tmp;
        }

        return res;
    }

    /**
     * Returns a SHA1 digest of the given array of bytes, in hex values lowercase.
     */
    public static String sha1(byte[] in) {
        String res;
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String tmp;

        digest.update(in, 0, in.length);
        byte out[] = digest.digest();

        // builds the hex string (lowercase)
        res = "";
        tmp = ""; // tmp = two characters to append to the hex string
        for (int i = 0; i < 20; i++) {
            int unsigned = out[i];
            if (out[i] < 0) {
                unsigned += 256;
            }
            tmp = Integer.toHexString(unsigned);
            if (tmp.length() == 1) {
                tmp = "0" + tmp;
            }
            res = res + tmp;
        }

        return res;
    }

    /**
     * Escapes the given string, for xml CDATA.
     */
    public static String escapeCDATA(String _str) {

        String escapeSource = "<>&'\"";
        String escapeDest[] = {"&lt;", "&gt;", "&amp;", "&apos;", "&quot;"};
        char ch;
        int pos;
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < _str.length(); i++) {
            ch = _str.charAt(i);
            pos = escapeSource.indexOf(ch);
            if (pos != -1) {
                res.append(escapeDest[pos]);
            } else {
                res.append(ch);
            }
        }
        return res.toString();
    }

    /**
     * Unescapes the given string, from an xml CDATA.
     */
    public static String unescapeCDATA(String _str) {
        String escapeSource = "<>&'\"";
        String escapeDest[] = {"&lt;", "&gt;", "&amp;", "&apos;", "&quot;"};

        int pos;  // position of the next amp '&' operator
        StringBuffer res = new StringBuffer();

        while ((pos = _str.indexOf('&')) != -1) {
            // found a '&' character
            // take the string until the '&'
            res.append(_str.substring(0, pos));
            _str = _str.substring(pos);

            // unescape the character
            int i = 0;
            boolean found = false;
            do {
                if (_str.startsWith(escapeDest[i])) {
                    found = true;
                } else {
                    i++;
                }
            } while (!found && (i < escapeDest.length));
            if (found) {
                res.append(escapeSource.charAt(i));
                _str = _str.substring(escapeDest[i].length());
            } else {
                // ERROR ***
                System.err.println("Parsing error: wrong escape character");
                _str = _str.substring(1);
            }
        }
        res.append(_str);
        return res.toString();
    }

    public static String formatGtwAddress(String jid) {
        if (jid.indexOf("40") == -1) {
            return jid;
        } else {
            int i = -1;

            if ((i = jid.indexOf("40hotmail")) != -1) {
                return jid.substring(0, i) + "\\" + jid.substring(i, jid.length());
            } else if ((i = jid.indexOf("40yahoo")) != -1) {
                return jid.substring(0, i) + "\\" + jid.substring(i, jid.length());
            } else if ((i = jid.indexOf("40aim")) != -1) {
                return jid.substring(0, i) + "\\" + jid.substring(i, jid.length());
            } else {
                return jid;
            }
        }
    }

    public static String escapeGtwAddress(String jid) {
        if (jid.indexOf("40") == -1) {
            return jid;
        } else {
            int i = -1;

            if ((i = jid.indexOf("40hotmail")) != -1) {
                return jid.substring(0, i) + "@" + jid.substring(i + 2, jid.length());
            } else if ((i = jid.indexOf("40yahoo")) != -1) {
                return jid.substring(0, i) + "@" + jid.substring(i + 2, jid.length());
            } else if ((i = jid.indexOf("40aim")) != -1) {
                return jid.substring(0, i) + "@" + jid.substring(i + 2, jid.length());
            } else {
                return jid;
            }
        }
    }

    public static String hash(String data) {
        FNVHash hash = new FNVHash();
        long hashed = hash.hash(data.getBytes());
        return String.valueOf(hashed);
    }

    public static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            if (imageUrl != null && imageUrl.length() > 0) {            	
                HttpClient client = new DefaultHttpClient();
            	HttpGet get = new HttpGet(addImageToUrl("http://fdtrcksarg-bamobile.rhcloud.com/webresources/entities.camion/getImage", imageUrl));
                try {
                	HttpResponse response = client.execute(get);
                    byte[] content = EntityUtils.toByteArray(response.getEntity());
                    Bitmap bmp = Tools.decodeByteArray(content, 0, content.length);
                    return bmp;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            Log.e("Tools.getBitmapFromURL", ex.getMessage(), ex);
        }
        return null;
    }
    
    public static String addImageToUrl(String url, String imageUrl){
        if(!url.endsWith("?"))
            url += "?";

        List<NameValuePair> params = new LinkedList<NameValuePair>();
        params.add(new BasicNameValuePair("filename",imageUrl));
        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        return url;
    }

    public static Bitmap getBitmapFromURL(Context context, String imageUrl, int width, int height) {
        if (imageUrl != null && imageUrl.length() > 0) {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(imageUrl.replace(" ", "%20"));
            try {
                HttpResponse response = client.execute(get);
                byte[] content = EntityUtils.toByteArray(response.getEntity());
                //get the dimensions of the image
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(content, 0, content.length, opts);

                // get the image and scale it appropriately
                opts.inJustDecodeBounds = false;
                opts.inSampleSize = Math.max(opts.outWidth / width, opts.outHeight / height);

                Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0, content.length, opts);
                if (bitmap == null) {
                    return null;
                }

                int scaledWidth = bitmap.getWidth();
                int scaledHeight = bitmap.getHeight();

                if (scaledWidth < scaledHeight) {
                    float scale = width / (float) scaledWidth;

                    scaledWidth = width;
                    scaledHeight = (int) Math.ceil(scaledHeight * scale);
                    if (scaledHeight < height) {
                        scale = height / (float) scaledHeight;

                        scaledHeight = height;
                        scaledWidth = (int) Math.ceil(scaledWidth * scale);
                    }
                } else {
                    float scale = height / (float) scaledHeight;

                    scaledHeight = height;
                    scaledWidth = (int) Math.ceil(scaledWidth * scale);

                    if (scaledWidth < width) {
                        scale = width / (float) scaledWidth;

                        scaledWidth = width;
                        scaledHeight = (int) Math.ceil(scaledHeight * scale);
                    }
                }

                Bitmap bmp = Bitmap.createScaledBitmap(bitmap,
                        scaledWidth, scaledHeight, false);

                bitmap.recycle();
                bitmap = null;

                return bmp;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static byte[] getFileDataFromURL(String imageUrl) {
        if (imageUrl != null && imageUrl.length() > 0) {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(imageUrl.replace(" ", "%20"));
            try {
                HttpResponse response = client.execute(get);
                byte[] content = EntityUtils.toByteArray(response.getEntity());
                return content;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isSdReadable() {
        boolean mExternalStorageAvailable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = true;
            Log.i("isSdReadable", "External storage card is readable.");
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            Log.i("isSdReadable", "External storage card is readable.");
            mExternalStorageAvailable = true;
        } else {
            // Something else is wrong. It may be one of many other
            // states, but all we need to know is we can neither read nor write
            mExternalStorageAvailable = false;
        }

        return mExternalStorageAvailable;
    }

    /**
     * Create a new bitmap based on a dummy empty image.
     * This method avoid the expensive Bitmap method Bitmap.createBitmap
     *
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createBitmap(int width, int height) {
        Bitmap bmp = BitmapFactory.decodeResource(MainActivity.getInstance().getResources(),
                R.drawable.empty_image, null);
        Bitmap resultBitmap = Bitmap.createScaledBitmap(bmp, width, height, false);
        bmp.recycle();
        bmp = null;
        return resultBitmap;
    }

    public static Bitmap createBitmapFromBase64(byte[] base64Data) {
        Bitmap image = null;
        try {
            byte[] imageData = android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return image;
    }

    public static Bitmap decodeByteArray(byte[] data, int index, int length) {
        try {
            WindowManager wm = (WindowManager) SplashActivity.getInstance().getSystemService(Context.WINDOW_SERVICE);
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inInputShareable = true;
            o.inPurgeable = true;
            o.inDither = false;
            BitmapFactory.decodeByteArray(data, index, length, o);

            //The new size we want to scale to
            final int IMAGE_MAX_SIZE = wm.getDefaultDisplay().getWidth();

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            o.inSampleSize = scale;
            o.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(data, index, length, o);
        } catch (Exception e) {
            Log.e("Tools.decodeByteArray", e.getMessage(), e);
        }
        return null;
    }
    
 // Decodes image and scales it to reduce memory consumption
 	private Bitmap decodeFile(File f) {
 		try {
 			// Decode image size
 			BitmapFactory.Options o = new BitmapFactory.Options();
 			o.inJustDecodeBounds = true;
 			FileInputStream stream1 = new FileInputStream(f);
 			BitmapFactory.decodeStream(stream1, null, o);
 			stream1.close();
  
 			// Find the correct scale value. It should be the power of 2.
 			// Recommended Size 512
 			final int REQUIRED_SIZE = 70;
 			int width_tmp = o.outWidth, height_tmp = o.outHeight;
 			int scale = 1;
 			while (true) {
 				if (width_tmp / 2 < REQUIRED_SIZE
 						|| height_tmp / 2 < REQUIRED_SIZE)
 					break;
 				width_tmp /= 2;
 				height_tmp /= 2;
 				scale *= 2;
 			}
  
 			// Decode with inSampleSize
 			BitmapFactory.Options o2 = new BitmapFactory.Options();
 			o2.inSampleSize = scale;
 			FileInputStream stream2 = new FileInputStream(f);
 			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
 			stream2.close();
 			return bitmap;
 		} catch (FileNotFoundException e) {
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
 		return null;
 	}

    public static String convertBitmapToBase64(Bitmap bitmap) {
        // Convert bitmap to Base64 encoded image for web
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap scaleImage(Bitmap bitmap, int boundBoxInDp) {
        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundBoxInDp) / width;
        float yScale = ((float) boundBoxInDp) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return scaledBitmap;
    }

    public static Bitmap scaleImageInPixels(Bitmap bitmap, int pixels) {
        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) pixels) / width;
        float yScale = ((float) pixels) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return scaledBitmap;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        int scaledWidth = bitmap.getWidth();
        int scaledHeight = bitmap.getHeight();

        if (scaledWidth < scaledHeight) {
            float scale = width / (float) scaledWidth;

            scaledWidth = width;
            scaledHeight = (int) Math.ceil(scaledHeight * scale);
            if (scaledHeight < height) {
                scale = height / (float) scaledHeight;

                scaledHeight = height;
                scaledWidth = (int) Math.ceil(scaledWidth * scale);
            }
        } else {
            float scale = height / (float) scaledHeight;

            scaledHeight = height;
            scaledWidth = (int) Math.ceil(scaledWidth * scale);

            if (scaledWidth < width) {
                scale = width / (float) scaledWidth;

                scaledWidth = width;
                scaledHeight = (int) Math.ceil(scaledHeight * scale);
            }
        }

        Bitmap bmp = Bitmap.createScaledBitmap(bitmap,
                scaledWidth, scaledHeight, false);

        bitmap.recycle();
        bitmap = null;

        return bmp;
    }

    public static double dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static double pxToDp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) px / density);
    }
    
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		  Bitmap output;

		    if (bitmap.getWidth() > bitmap.getHeight()) {
		        output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Config.ARGB_8888);
		    } else {
		        output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Config.ARGB_8888);
		    }

		    Canvas canvas = new Canvas(output);

		    final int color = 0xff424242;
		    final Paint paint = new Paint();
		    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		    float r = 0;

		    if (bitmap.getWidth() > bitmap.getHeight()) {
		        r = bitmap.getHeight() / 2;
		    } else {
		        r = bitmap.getWidth() / 2;
		    }

		    paint.setAntiAlias(true);
		    canvas.drawARGB(0, 0, 0, 0);
		    paint.setColor(color);
		    canvas.drawCircle(r, r, r, paint);
		    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    canvas.drawBitmap(bitmap, rect, rect, paint);
		    
		   
		    return output;
		 
		  }
    
    public static Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap,
            int borderWidth) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        final int width = bitmap.getWidth() + borderWidth;
        final int height = bitmap.getHeight() + borderWidth;

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return canvasBitmap;
    }

//    public static CharSequence createBreadCrum(ar.com.mobisoft.app.util.TreeNode node, String separator) {
//        CharSequence breadCrumb = "";
//        ar.com.mobisoft.app.util.TreeNode tmpNode = node;
//
//        while (tmpNode != null) {
//            if (tmpNode.getReference() != null) {
//                if (tmpNode.getReference() instanceof Categoria) {
//                    Categoria categoria = (Categoria) tmpNode.getReference();
//                    breadCrumb = categoria.nombreCat + separator + breadCrumb;
//                }
//            }
//            tmpNode = tmpNode.getParent();
//        }
//
//        breadCrumb = MainActivity.getInstance().getResources().getString(R.string.home_string)
//                + separator + breadCrumb;
//
//        return breadCrumb;
//    }

//    public static void shareContent(final String contentId, final String subject, final String body) {
//        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//        // what type of data needs to be send by sharing
//        sharingIntent.setType("text/plain");
//        // package names
//        PackageManager pm = MainActivity.getInstance().getPackageManager();
//        // list package
//        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
//        final ShareIntentListAdapter objShareIntentListAdapter = new ShareIntentListAdapter(
//                MainActivity.getInstance(), activityList.toArray());
//
//        // Create alert dialog box
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getInstance());
//        builder.setTitle(MainActivity.getInstance().getResources().getString(R.string.action_share_with));
//        builder.setAdapter(objShareIntentListAdapter, new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int item) {
//
//                ResolveInfo info = (ResolveInfo) objShareIntentListAdapter.getItem(item);
//
//                // save stats here
//                try {
//                    Stats stat = new Stats();
//                    if (contentId == null || contentId.equals("")) {
//                        stat.setField(Stats.FIELD_ID_CONTENT, "app");
//                    } else {
//                        stat.setField(Stats.FIELD_ID_CONTENT, contentId);
//                    }
//                    stat.setField(Stats.FIELD_STAT_DATE,
//                            String.valueOf(new Date().getTime()));
//                    if (info.activityInfo.name.toLowerCase().contains("mail")
//                            || info.activityInfo.name.toLowerCase().contains("correo")) {
//                        stat.setField(Stats.FIELD_STAT_TYPE,
//                                String.valueOf(Stats.STAT_TYPE_CONTENT_SHARE_EMAIL));
//                    } else if (info.activityInfo.name.toLowerCase().contains("facebook")
//                            || info.activityInfo.name.toLowerCase().contains("messenger")) {
//                        stat.setField(Stats.FIELD_STAT_TYPE,
//                                String.valueOf(Stats.STAT_TYPE_CONTENT_SHARE_FB));
//                    } else if (info.activityInfo.name.toLowerCase().contains("twitter")) {
//                        stat.setField(Stats.FIELD_STAT_TYPE,
//                                String.valueOf(Stats.STAT_TYPE_CONTENT_SHARE_TWITTER));
//                    } else if (info.activityInfo.name.toLowerCase().contains("mensajes")
//                            || info.activityInfo.name.toLowerCase().contains("messages")
//                            || info.activityInfo.name.toLowerCase().contains("message")
//                            || info.activityInfo.name.toLowerCase().contains("hangout")
//                            || info.activityInfo.name.toLowerCase().contains("sms")
//                            || info.activityInfo.name.toLowerCase().contains("mms")
//                            || info.activityInfo.name.toLowerCase().contains("whatsapp")
//                            || info.activityInfo.name.toLowerCase().contains("skype")) {
//                        stat.setField(Stats.FIELD_STAT_TYPE,
//                                String.valueOf(Stats.STAT_TYPE_CONTENT_SHARE_SMS));
//                    }
//
//                    stat.insert();
//                } catch (Exception ex) {
//                    Log.e("Stats in shareContent", ex.getMessage(), ex);
//                }
//
//                // start respective activity
//                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//                intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
//                intent.setType("text/plain");
//                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
//                intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
//                intent.putExtra(Intent.EXTRA_TITLE, MainActivity.getInstance().getString(R.string.action_share_with));
//                MainActivity.getInstance().startActivity(intent);
//
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }

//    public static void shareApp() {
//        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//        // what type of data needs to be send by sharing
//        sharingIntent.setType("text/plain");
//        // package names
//        PackageManager pm = MainActivity.getInstance().getPackageManager();
//        // list package
//        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
//        final ShareIntentListAdapter objShareIntentListAdapter = new ShareIntentListAdapter(
//                MainActivity.getInstance(), activityList.toArray());
//
//        // Create alert dialog box
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getInstance());
//        builder.setTitle(MainActivity.getInstance().getResources().getString(R.string.action_share_with));
//        builder.setAdapter(objShareIntentListAdapter, new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int item) {
//
//                ResolveInfo info = (ResolveInfo) objShareIntentListAdapter.getItem(item);
//
//                // start respective activity
//                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//                intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
//                intent.setType("text/plain");
//                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Un amigo te recomienda una aplicacion");
//                intent.putExtra(Intent.EXTRA_TITLE, MainActivity.getInstance().getString(R.string.action_share_with));
//
//                // save stats and set the content body here
//                try {
//                    Stats stat = new Stats();
//                    stat.setField(Stats.FIELD_ID_CONTENT, "app");
//                    stat.setField(Stats.FIELD_STAT_DATE,
//                            String.valueOf(new Date().getTime()));
//                    if (info.activityInfo.name.toLowerCase().contains("mail")
//                            || info.activityInfo.name.toLowerCase().contains("correo")) {
//                        stat.setField(Stats.FIELD_STAT_TYPE,
//                                String.valueOf(Stats.STAT_TYPE_CONTENT_SHARE_EMAIL));
//                        intent.putExtra(android.content.Intent.EXTRA_TEXT,
//                                ParseProyecto.getProyecto(false).sharingTextEmail);
//                    } else if (info.activityInfo.name.toLowerCase().contains("facebook")
//                            || info.activityInfo.name.toLowerCase().contains("messenger")) {
//                        stat.setField(Stats.FIELD_STAT_TYPE,
//                                String.valueOf(Stats.STAT_TYPE_CONTENT_SHARE_FB));
//                        intent.putExtra(android.content.Intent.EXTRA_TEXT,
//                                ParseProyecto.getProyecto(false).sharingTextFB);
//                    } else if (info.activityInfo.name.toLowerCase().contains("twitter")) {
//                        stat.setField(Stats.FIELD_STAT_TYPE,
//                                String.valueOf(Stats.STAT_TYPE_CONTENT_SHARE_TWITTER));
//                        intent.putExtra(android.content.Intent.EXTRA_TEXT,
//                                ParseProyecto.getProyecto(false).sharingTextTW);
//                    } else if (info.activityInfo.name.toLowerCase().contains("mensajes")
//                            || info.activityInfo.name.toLowerCase().contains("messages")
//                            || info.activityInfo.name.toLowerCase().contains("message")
//                            || info.activityInfo.name.toLowerCase().contains("hangout")
//                            || info.activityInfo.name.toLowerCase().contains("sms")
//                            || info.activityInfo.name.toLowerCase().contains("mms")
//                            || info.activityInfo.name.toLowerCase().contains("whatsapp")
//                            || info.activityInfo.name.toLowerCase().contains("skype")) {
//                        stat.setField(Stats.FIELD_STAT_TYPE,
//                                String.valueOf(Stats.STAT_TYPE_CONTENT_SHARE_SMS));
//                        intent.putExtra(android.content.Intent.EXTRA_TEXT,
//                                ParseProyecto.getProyecto(false).sharingTextSMS);
//                    }
//
//                    stat.insert();
//                } catch (Exception ex) {
//                    Log.e("Stats in shareContent", ex.getMessage(), ex);
//                }
//
//                MainActivity.getInstance().startActivity(intent);
//            }
//        });
//
//        AlertDialog alert = builder.create();
//        alert.show();
//    }


    public static void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            try {
                ((ViewGroup) view).removeAllViews();
            } catch (Exception ex) {
                // do nothing
            }
        }
    }

//    public static void printMemoryStats() {
//        Log.i("Memory Stats", "Free memory: " + Runtime.getRuntime().freeMemory()
//                + "\nTotal memory: " + Runtime.getRuntime().totalMemory()
//                + "\nMax memory: " + Runtime.getRuntime().maxMemory());
//    }
//
//    public static void shareUri(final Uri uri, final String subject, final String body, final String mimeType) {
//        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//        // what type of data needs to be send by sharing
//        sharingIntent.setType("text/plain");
//        // package names
//        PackageManager pm = MainActivity.getInstance().getPackageManager();
//        // list package
//        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
//        final ShareIntentListAdapter objShareIntentListAdapter = new ShareIntentListAdapter(
//                MainActivity.getInstance(), activityList.toArray());
//
//        // Create alert dialog box
//        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getInstance());
//        builder.setTitle(MainActivity.getInstance().getResources().getString(R.string.action_share_with));
//        builder.setAdapter(objShareIntentListAdapter, new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int item) {
//
//                ResolveInfo info = (ResolveInfo) objShareIntentListAdapter.getItem(item);
//                // start respective activity
//                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//                intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
//                intent.setType(mimeType);
//                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
//                intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
//                intent.putExtra(Intent.EXTRA_STREAM, uri);
//                intent.putExtra(Intent.EXTRA_TITLE, MainActivity.getInstance().getString(R.string.action_share_with));
//
//                MainActivity.getInstance().startActivity(intent);
//
//            }
//        });
//        MainActivity.getInstance().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                AlertDialog alert = builder.create();
//                alert.show();
//            }
//        });
//    }

    public static String createICalendarContent(String summary, String description, Date start, Date end) {
        SimpleDateFormat dtStartFormat = new SimpleDateFormat("yyyyMMdd'T'hhmmss");
        String dtstart = dtStartFormat.format(start);
        String dtend = dtStartFormat.format(end);

        String ical = "BEGIN:VCALENDAR\r\n" +
                "METHOD:PUBLISH\r\n" +
                "PRODID:-//iCalendar Builder (v1.0)//ES\r\nVERSION:1.0\r\n" +
                "BEGIN:VEVENT\r\n" +
                "SUMMARY:" + summary + "\r\n" +
                "DESCRIPTION:" + description + "\r\n" +
                // "ATTENDEE;CN=test@gmail.com:mailto:test@gmail.com\r\n" +
                "DTSTART:" + dtstart + "\r\n" +
                "DTEND:" + dtend + "\r\n" +
                // "DTSTAMP:20130220T161439Z\r\n" +
                "END:VEVENT\r\n" +
                "END:VCALENDAR";
        return ical;
    }

    public static boolean saveFileToAppContextCacheDir(String fileName, byte[] data) {
        try {
            File dir = MainActivity.getInstance().getApplicationContext().getCacheDir();
            File outputFile = new File(dir, fileName);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    // Input a double latitude or longitude in the decimal format
    // e.g. 87.728056
    public static String decimalToDMS(double coord) {
        String output, degrees, minutes, seconds;

        // gets the modulus the coordinate divided by one (MOD1).
        // in other words gets all the numbers after the decimal point.
        // e.g. mod = 87.728056 % 1 == 0.728056
        //
        // next get the integer part of the coord. On other words the whole number part.
        // e.g. intPart = 87

        double mod = coord % 1;
        int intPart = (int) coord;

        //set degrees to the value of intPart
        //e.g. degrees = "87"

        degrees = String.valueOf(intPart);

        // next times the MOD1 of degrees by 60 so we can find the integer part for minutes.
        // get the MOD1 of the new coord to find the numbers after the decimal point.
        // e.g. coord = 0.728056 * 60 == 43.68336
        //      mod = 43.68336 % 1 == 0.68336
        //
        // next get the value of the integer part of the coord.
        // e.g. intPart = 43

        coord = mod * 60;
        mod = coord % 1;
        intPart = (int) coord;

        // set minutes to the value of intPart.
        // e.g. minutes = "43"
        minutes = String.valueOf(intPart);

        //do the same again for minutes
        //e.g. coord = 0.68336 * 60 == 41.0016
        //e.g. intPart = 41
        coord = mod * 60;
        intPart = (int) coord;

        // set seconds to the value of intPart.
        // e.g. seconds = "41"
        seconds = String.valueOf(intPart);

        // I used this format for android but you can change it
        // to return in whatever format you like
        // e.g. output = "87/1,43/1,41/1"
        // output = degrees + "/1," + minutes + "/1," + seconds + "/1";

        //Standard output of 
        output = degrees + "�" + minutes + "'" + seconds + "\"";

        return output;
    }

    /*
     * Conversion DMS to decimal
     *
     * Input: latitude or longitude in the DMS format ( example: N 43� 36' 15.894")
     * Return: latitude or longitude in decimal format
     * hemisphereOUmeridien => {W,E,S,N}
     *
     */
    public static double dmsToDecimal(String hemisphereOUmeridien, double degres, double minutes, double secondes) {
        double LatOrLon = 0;
        double signe = 1.0;

        if ((hemisphereOUmeridien == "W") || (hemisphereOUmeridien == "S")) {
            signe = -1.0;
        }
        LatOrLon = signe * (Math.floor(degres) + Math.floor(minutes) / 60.0 + secondes / 3600.0);

        return (LatOrLon);
    }
    
    public static Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom 
        Bitmap cs = null; 
     
        int width, height = 0; 
         
        if(c.getWidth() > s.getWidth()) { 
          width = c.getWidth(); 
          height = c.getHeight() + s.getHeight(); 
        } else { 
          width = s.getWidth(); 
          height = c.getHeight() + s.getHeight(); 
        } 
     
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 
     
        Canvas comboImage = new Canvas(cs); 
     
        comboImage.drawBitmap(c, 0f, 0f, null); 
        comboImage.drawBitmap(s, 0f, c.getHeight(), null); 
     
        // this is an extra bit I added, just incase you want to save the new image somewhere and then return the location 
        /*String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png"; 
     
        OutputStream os = null; 
        try { 
          os = new FileOutputStream(loc + tmpImg); 
          cs.compress(CompressFormat.PNG, 100, os); 
        } catch(IOException e) { 
          Log.e("combineImages", "problem combining images", e); 
        }*/ 
     
        return cs; 
      } 

//    public static void addNotification(Context context, String title, String content, Bitmap icon) {
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(context)
//                        .setSmallIcon(R.drawable.ic_launcher)
//                        .setLargeIcon(icon)
//                        .setContentTitle(title)
//                        .setContentText(content)
//                        .setAutoCancel(true);
//        // Creates an explicit intent for an Activity in your app
//        Intent resultIntent = new Intent(context, MainActivity.class);
//
//        // The stack builder object will contain an artificial back stack for the
//        // started Activity.
//        // This ensures that navigating backward from the Activity leads out of
//        // your application to the Home screen.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        // Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(MainActivity.class);
//        // Adds the Intent that starts the Activity to the top of the stack
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);
//        NotificationManager mNotificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        // mId allows you to update the notification later on.
//        mNotificationManager.notify(UpdateDataService.DATA_UPDATE_NOTIFICATION, mBuilder.build());
//    }

}


