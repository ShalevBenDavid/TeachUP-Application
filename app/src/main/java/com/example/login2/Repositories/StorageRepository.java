package com.example.login2.Repositories;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageRepository {
    private final FirebaseStorage storage;

    public StorageRepository(){
        storage = FirebaseStorage.getInstance();
    }

    public void uploadFile(Uri fileUri,String storagePath,StorageTaskListener listener){
        StorageReference reference = storage.getReference().child(storagePath);
        Log.e("file uri",fileUri.toString());
        Log.e("path",storagePath);

        reference.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {
            reference.getDownloadUrl().addOnSuccessListener(downloadUrl ->{
                listener.onSuccess(downloadUrl.toString());
            }).addOnFailureListener(e->{
                listener.onError(e.getMessage());
            });
        }).addOnFailureListener(e->{
            listener.onError(e.getMessage());
        });
    }

    public interface StorageTaskListener {
        void onSuccess(String downloadUrl);
        void onError(String error);
    }

}
