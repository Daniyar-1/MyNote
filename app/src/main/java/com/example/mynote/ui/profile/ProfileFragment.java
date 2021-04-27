package com.example.mynote.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mynote.R;
import com.example.mynote.models.PictureItem;

import java.util.List;

public class ProfileFragment extends Fragment {
    private ImageView imageView;
    private RecyclerView recyclerView;
    private ProfilePhotoAdapter photoAdapter;
    private List<PictureItem> pictureItemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photoAdapter = new ProfilePhotoAdapter();
        imageView = view.findViewById(R.id.imgProfile);
        recyclerView = view.findViewById(R.id.prof_recycler);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });
        recyclerView.setAdapter(photoAdapter);

        Glide.with(this).load(R.drawable.profile_recycler).circleCrop().into(imageView);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_photo) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 101);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                Glide.with(this).load(data.getData()).circleCrop().into(imageView);
            }
        }
        if (requestCode == 101 && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                PictureItem item = new PictureItem(data.getData());
                photoAdapter.addToList(item);
            }
        }
    }
}
