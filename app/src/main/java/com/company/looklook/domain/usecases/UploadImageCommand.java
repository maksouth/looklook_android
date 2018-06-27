package com.company.looklook.domain.usecases;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;

public class UploadImageCommand {

    //convert firebase async call to observable
    private ReplaySubject<String> replaySubject;
    private StorageReference photosFolder;

    public UploadImageCommand() {
        photosFolder = FirebaseStorage.getInstance()
                .getReference()
                .child("test_users")
                .child("posts");
    }

    public void setImageName(String name) {
        photosFolder = photosFolder.child(name);
    }

    public Observable<String> execute(String imagePath) {
        replaySubject = ReplaySubject.create();
        Uri file = Uri.parse(imagePath);
        photosFolder.putFile(file)
            .addOnSuccessListener(result -> {
                replaySubject.onNext(result.getDownloadUrl().toString());
                replaySubject.onComplete();
            }).addOnFailureListener(failure -> {
                replaySubject.onError(failure);
                replaySubject.onComplete();
            });

        return replaySubject;
    }

}
