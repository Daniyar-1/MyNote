package com.example.mynote.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynote.R;
import com.example.mynote.interfaces.OnItemClickListener;
import com.example.mynote.models.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<Task> list;
    private OnItemClickListener onItemClickListener;

    public TaskAdapter(ArrayList<Task> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle;
        private TextView textDesc;
        private TextView textTime;
        private TextView textUpdatedTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItmClick(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });
            textTitle = itemView.findViewById(R.id.textTitle);
            textDesc = itemView.findViewById(R.id.textDesc);
            textTime = itemView.findViewById(R.id.textTime);
            textUpdatedTime = itemView.findViewById(R.id.textUpdatedTime);
        }

        public void bind(Task task) {
            textTitle.setText(task.getTitle());
            textDesc.setText(task.getDescription());
            textTime.setText(getDate(task.getCreatedAt()));
            textUpdatedTime.setText(getDate(task.getUpdatedAt()));
        }

        private String getDate(long time) {
            DateFormat formatter = new SimpleDateFormat("HH:mm | dd MMM", Locale.getDefault());
            return formatter.format(time);
        }
    }
}
