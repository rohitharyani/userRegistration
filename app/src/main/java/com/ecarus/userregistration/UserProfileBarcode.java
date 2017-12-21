package com.ecarus.userregistration;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class UserProfileBarcode extends AppCompatActivity {
    public static Bitmap bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_barcode);


        LinearLayout l = new LinearLayout(this);
        l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        l.setOrientation(LinearLayout.VERTICAL);

        l.setBackgroundColor(Color.parseColor("#0099cc"));
        setContentView(l);


        String barcode_data = SharedPrefManager.getInstance(this).getUserPhone();
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(barcode_data, BarcodeFormat.QR_CODE, 1024, 1024);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            TextView tv1 = new TextView(this);
            tv1.setGravity(Gravity.START);
            tv1.setBackgroundColor(Color.parseColor("#33b5e5"));
            tv1.setText("Ecarus");
            tv1.setTextColor(Color.WHITE);
            tv1.setTextSize(24);
            tv1.setTypeface(Typeface.SANS_SERIF);
            tv1.setTypeface(null,Typeface.BOLD_ITALIC);
            tv1.setPadding(30,25,0,25);
            l.addView(tv1);


            LinearLayout l1 = new LinearLayout(this);
            l1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            l1.setOrientation(LinearLayout.VERTICAL);

            l1.setBackgroundColor(Color.parseColor("#0099cc"));
            l1.setGravity(Gravity.CENTER);

            TextView tv = new TextView(this);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setText("Scan this Ecarus code for a faster check out process!");
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(16);
            tv.setPadding(15,0,15,30);
            l1.addView(tv);

            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bmp);
            imageView.setPadding(0,30,0,0);
            l1.addView(imageView);

            TextView phone = new TextView(this);
            phone.setGravity(Gravity.CENTER_HORIZONTAL);
            phone.setText(SharedPrefManager.getInstance(this).getUserPhone());
            phone.setTextColor(Color.WHITE);
            phone.setTextSize(16);
            phone.setPadding(15,0,15,30);
            l1.addView(phone);

            l.addView(l1);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


/*
    ** Generates Code 128 Barcode **

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;

    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
*/

}
