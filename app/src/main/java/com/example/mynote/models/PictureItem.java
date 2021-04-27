package com.example.mynote.models;

import android.net.Uri;

public class PictureItem {

    private Uri imageUrl;

    public PictureItem(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Uri getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }
}


