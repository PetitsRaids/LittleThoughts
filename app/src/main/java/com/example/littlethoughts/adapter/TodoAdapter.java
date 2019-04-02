package com.example.littlethoughts.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.littlethoughts.R;
import com.example.littlethoughts.db.TodoItem;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<TodoItem> todoItems;

    private Context mContext;

    public TodoAdapter(Context context, List<TodoItem> todoItems) {
        mContext = context;
        this.todoItems = todoItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textView;
        View clickView;

        ViewHolder(View view) {
            super(view);
            clickView = view;
            checkBox = view.findViewById(R.id.todo_check);
            textView = view.findViewById(R.id.todo_tv);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.todo_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            notifyItemRemoved(i);
        });
        viewHolder.clickView.setOnClickListener(v->{
            Toast.makeText(mContext, todoItems.get(i).getName(), Toast.LENGTH_SHORT).show();
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.checkBox.setChecked(todoItems.get(i).isChecked());
        viewHolder.checkBox.setFocusable(false);
        viewHolder.textView.setText(todoItems.get(i).getName());
    }

    @Override
    public int getItemCount() {
        if(todoItems == null){
            return 0;
        }else{
            return todoItems.size();
        }
    }

}
