package com.example.login2.Utils;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CustomUtils {

    private CustomUtils(){}

    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    private static String getFileExtensions(String fileName){
        int startIndexOfExtension = fileName.lastIndexOf('.');
        if(startIndexOfExtension >= 0){
            Log.e("extension",fileName.substring(startIndexOfExtension));
            return fileName.substring(startIndexOfExtension);
        }
        return null;
    }


    public static String getFileExtension(Context context, Uri uri){
        ContentResolver resolver = context.getContentResolver();
        return getExtensionFromMime(resolver.getType(uri));
    }

    private static String getExtensionFromMime(String mime){
        if(mime != null){
            return "."+mime.substring(mime.lastIndexOf('/')+1);
        }
        return null;
    }
    public static String generateFileName(Context context,Uri uri) {
        String extension = getFileExtension(context,uri);
        Log.e("extension",extension);
        String filename = UUID.randomUUID().getLeastSignificantBits() +getFileExtension(context,uri);
        Log.e("filename",filename);

        return filename;
    }

    public static String getLocalDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
        return formatter.format(dateTime);
    }
}
