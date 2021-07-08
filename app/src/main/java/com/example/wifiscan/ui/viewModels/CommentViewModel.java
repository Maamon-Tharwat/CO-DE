package com.example.wifiscan.ui.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wifiscan.model.CommentModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class CommentViewModel extends ViewModel {

    private static final String TAG = "CommentViewModel";
    private static final String COLLECTION_PATH = "posts";
    private static final String COLLECTION_PATH1 = "comments";

    public MutableLiveData<ArrayList<CommentModel>> comments;

    private final FirebaseFirestore database;

    public CommentViewModel() {
        this.database = FirebaseFirestore.getInstance();
        comments=new MutableLiveData<>();
    }

    public void getComments(String postID){
        comments.setValue(new ArrayList<>());
        database.collection(COLLECTION_PATH).document(postID).collection(COLLECTION_PATH1)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "listen:error", error);
                        return;
                    }
                    assert value != null;
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            Objects.requireNonNull(comments.getValue()).add(dc.getDocument().toObject(CommentModel.class));
                            comments.setValue(comments.getValue());
                        }
                    }

                });
    }

    public void addComment(String postID,CommentModel comment){
        database.collection(COLLECTION_PATH).document(postID).collection(COLLECTION_PATH1)
                .document(comment.getId())
                .set(comment);
    }
}
