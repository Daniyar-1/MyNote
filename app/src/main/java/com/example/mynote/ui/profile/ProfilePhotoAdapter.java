package com.example.mynote.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mynote.R;
import com.example.mynote.models.PictureItem;

import java.util.ArrayList;
import java.util.List;

public class ProfilePhotoAdapter extends RecyclerView.Adapter<ProfilePhotoAdapter.PhotoHolder> {

    private List<PictureItem> pictureItemList = new ArrayList<>();

    public ProfilePhotoAdapter() {
    }

    public void addToList(PictureItem item){
        pictureItemList.add(0,item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_recycler_item, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        holder.bind(pictureItemList.get(position));

    }

    @Override
    public int getItemCount() {
        return pictureItemList.size();
    }




    public class PhotoHolder extends RecyclerView.ViewHolder {

        public ImageView imgViewIcon;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            imgViewIcon = (ImageView) itemView.findViewById(R.id.pli_image_view);
        }

        public void bind(PictureItem pictureItem) {
            Glide.with(itemView.getContext()).load(pictureItem.getImageUrl()).into(imgViewIcon);
        }
    }
}
