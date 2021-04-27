package com.example.mynote.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mynote.App;
import com.example.mynote.R;
import com.example.mynote.models.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class FormFragment extends Fragment {
    private NavController navController;
    private EditText editTitle;
    private EditText editDesc;
    private Task task;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTitle = view.findViewById(R.id.editTitle);
        editDesc = view.findViewById(R.id.editDesc);
        task = (Task) requireArguments().getSerializable("task");
        if (task != null) {
            editTitle.setText(task.getTitle());
            editDesc.setText(task.getDescription());
        }
        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save() {
        String t = editTitle.getText().toString().trim();
        String d = editDesc.getText().toString().trim();
        if (!t.isEmpty() || !d.isEmpty()) {
            if (task != null) {
                task.setTitle(t);
                task.setDescription(d);
                task.setUpdatedAt(System.currentTimeMillis());
                App.getInstance().getDatabase().taskDao().update(task);

            } else {
                task = new Task(t, d, System.currentTimeMillis());
                App.getInstance().getDatabase().taskDao().insert(task);
                saveDataToFirestore();
             /*Bundle bundle = new Bundle();
            bundle.putSerializable("task", task);
            bundle.putBoolean("edit", edit);
            getParentFragmentManager().setFragmentResult("form", bundle);*/
            }
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigateUp();
        } else {
            editTitle.setError("Fill the field");
            editDesc.setError("Fill the field");
        }
    }

    private void saveDataToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Daniyar");
        user.put("last", "Marklen uulu");
        user.put("born", 2001);
        db.collection("users").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentReference> task) {
                if (task.isSuccessful()){

                }else {

                }
            }
        });

    }
}