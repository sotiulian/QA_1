package com.wwp.QA.Utils;

import android.graphics.Color;
import android.util.Log;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Utils {

    public static int parseColor(int color) {

        /*
        RGB code has 24 bits format (bits 0..23):
        RED[7:0]     GREEN[7:0]   BLUE[7:0]
        23        16 15         8 7          0
        RGB = (R*65536)+(G*256)+B , (when R is RED, G is GREEN and B is BLUE)

        Calculation examples
        White RGB code = 255*65536+255*256+255 = #FFFFFF
        Blue RGB code = 0*65536+0*256+255 = #0000FF
        Red RGB code = 255*65536+0*256+0 = #FF0000
        Green RGB code = 0*65536+255*256+0 = #00FF00
        Gray RGB code = 128*65536+128*256+128 = #808080
        Yellow RGB code = 255*65536+255*256+0 = #FFFF00
        */

        int rgb   = color;

        // protectie la culori imposibile trimise
        if (rgb<0 || rgb>255*65536+255*256+255) {

            rgb = 255*65536+255*256+255; //white

        }

        int red =   (rgb >>  0) & 0xFF;
        int green = (rgb >>  8) & 0xFF;
        int blue =  (rgb >> 16) & 0xFF;
        int alpha = (rgb >> 24) & 0xFF; // transparency

        Log.d("parseColor: ", "Color number =" + color + " parsed as " + rgb + ". ARGB=" + alpha +"," + red + "," + green + "," + blue);

        return Color.rgb(red, green, blue);

    }


    public static String getMD5(String pcoriginal){

        String lcresponse = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pcoriginal.getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            lcresponse = sb.toString();
        } catch (NoSuchAlgorithmException nsae) {

            Log.e("Utils.getMD5", String.format(Locale.US, "NoSuchalgorithmException: %s", nsae.getMessage())); // nsae.getStackTrace()

        }

        //System.out.println("original:" + pcoriginal);
        //System.out.println("digested(hex):" + sb.toString());

        return lcresponse;

    }

    public static Map<String, String> ObjectToMap(Object obj) {

        // https://initialcommit.com/blog/java-convert-object-to-map

        Object value = null;
        Map<String,String> map = new HashMap<String,String>();

        Field[] allFields = obj.getClass().getDeclaredFields();
        for (Field field : allFields) {

            field.setAccessible(true);
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                value = "";
            }

            map.put(field.getName(), (String) value.toString());
        }

        return map;

    }


}
