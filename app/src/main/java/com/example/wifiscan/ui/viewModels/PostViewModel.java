package com.example.wifiscan.ui.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wifiscan.model.PostModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class PostViewModel extends ViewModel {
    private static final String TAG = "PostViewModel";
    private static final String COLLECTION_PATH = "posts";
    public MutableLiveData<ArrayList<PostModel>> posts = new MutableLiveData<>();
    private FirebaseFirestore database;

    public void getPosts() {
        posts.setValue(new ArrayList<>());

        database = FirebaseFirestore.getInstance();
        database.collection(COLLECTION_PATH).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w(TAG, "listen:error", error);
                return;
            }
            assert value != null;
            for (DocumentChange dc : value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    Objects.requireNonNull(posts.getValue()).add(dc.getDocument().toObject(PostModel.class));
                    posts.setValue(posts.getValue());
                }
            }

        });
    }
    public void addPost(PostModel post){
        database=FirebaseFirestore.getInstance();
        database.collection(COLLECTION_PATH)
                .document(post.getId())
                .set(post);
    }

}
