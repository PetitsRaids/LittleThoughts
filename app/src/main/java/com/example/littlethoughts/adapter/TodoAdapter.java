package com.example.littlethoughts.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.littlethoughts.R;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private String[] todoList;

    private Context mContext;

    public TodoAdapter(Context context, String[] todoList) {
        mContext = context;
        this.todoList = todoList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textView;

        ViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.todo_check);
            textView = view.findViewById(R.id.todo_tv);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.todo_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.checkBox.setChecked(false);
        viewHolder.textView.setText(todoList[i]);
    }

    @Override
    public int getItemCount() {
        return todoList.length;
    }
}
