package com.example.login2.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UriUtils {
    private static final Set<String> IMAGE_MIME_TYPES = new HashSet<>(
            Arrays.asList("image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp")
    );

    private static final Set<String> VIDEO_MIME_TYPE = new HashSet<>(
            Arrays.asList("video/mp4", "video/x-msvideo", "video/x-ms-wmv",
                    "video/webm", "video/x-matroska", "video/mpeg")
    );

    private static final Set<String> DOCUMENT_MIME_TYPE = new HashSet<>(
            Arrays.asList("application/pdf","application/msword","application/vnd.ms-excel",
                    "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation",
                    "text/plain","application/rtf","text/csv","application/vnd.oasis.opendocument.text",
                    "application/vnd.oasis.opendocument.spreadsheet","application/vnd.oasis.opendocument.presentation",
                    "application/epub+zip","text/html")
    );

    public static String getFileName(Context context, Uri uri) {
        String fileName = null;

        if (uri.getScheme().equals("content")) {
            ContentResolver contentResolver = context.getContentResolver();
            try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    fileName = cursor.getString(nameIndex);
                }
            } catch (Exception e) {
                CustomUtils.showToast(context, e.getMessage());
            }
        }

        return fileName;
    }

    public static String getFileType(Context context, Uri uri) {
        String mimeType = getMimeType(context, uri);
        return getFileType(mimeType);
    }

    private static String getFileType(String mimeType) {
        if(DOCUMENT_MIME_TYPE.contains(mimeType)){
            return Constants.DOCUMENT_FILE;
        } else if(IMAGE_MIME_TYPES.contains(mimeType)) {
            return Constants.IMAGE_FILE;
        } else if(VIDEO_MIME_TYPE.contains(mimeType)){
            return Constants.VIDEO_FILE;
        } else{
            return Constants.UNKNOWN_FILE_TYPE;
        }
    }

    private static String getMimeType(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.getType(uri);
    }
}
