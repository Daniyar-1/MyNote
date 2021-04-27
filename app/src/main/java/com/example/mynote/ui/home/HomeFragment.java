package com.example.mynote.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynote.App;
import com.example.mynote.R;
import com.example.mynote.interfaces.OnItemClickListener;
import com.example.mynote.models.Task;
import com.example.mynote.ui.home.TaskAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements OnItemClickListener {
    private NavController navController;
    private TaskAdapter adapter;
    private ArrayList<Task> list;
    private Task task;
    private int currentPos;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
//        initResultListener();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });
    }

//Слушатель для отправки данных в приложении без бд
  /*  private void initResultListener() {
        getParentFragmentManager().setFragmentResultListener("form",
                getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Log.e("Home", "onFragmentResult");
                        Task task = (Task) result.getSerializable("task");

                        boolean edit = result.getBoolean("edit");
                        if (edit) {
                            list.set( , task);
                        } else {
                            list.add(0, task);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }*/

    private void initList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
       /* if (list == null) {
            list = new ArrayList<>();
        }*/
        list = new ArrayList<>();
        //Слушатель на изменения в базе данных ROOM
        App.getInstance().getDatabase().taskDao().getAllAlive().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                list.clear();
                list.addAll(App.getInstance().getDatabase().taskDao().getAll());
                adapter.notifyDataSetChanged();

            }
        });
        adapter = new TaskAdapter(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            openForm(null);
        }
        final String[] sortBy = {"Edited Date", "A-z", "z-A"};
        if (item.getItemId() == R.id.action_sort) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Order by:")
                    .setItems(sortBy, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    list.clear();
                                    list.addAll(App.getInstance().getDatabase().taskDao().sortByEditedTime());
                                    adapter.notifyDataSetChanged();
                                    break;
                                case 1:
                                    list.clear();
                                    list.addAll(App.getInstance().getDatabase().taskDao().sortByASC());
                                    adapter.notifyDataSetChanged();
                                    break;
                                case 2:
                                    list.clear();
                                    list.addAll(App.getInstance().getDatabase().taskDao().sortByDESC());
                                    adapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (item.getItemId() == R.id.action_clear_bd) {
            App.getInstance().getDatabase().taskDao().deleteAll();
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlert(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Do you wanna delete «" + task.getTitle() + "» ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                App.getInstance().getDatabase().taskDao().delete(task);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    private void openForm(Task task) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", task);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_navigation_home_to_formFragment, bundle);
    }

    @Override
    public void onItmClick(int position) {
        currentPos = position;
        Task task = list.get(position);
        openForm(task);
    }

    @Override
    public void onItemLongClick(int position) {
        showAlert(list.get(position));
    }

}