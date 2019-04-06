package com.example.littlethoughts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.clickView.setOnClickListener(v->{
            Log.d("TodoAdapter", "onBindViewHolder: " + i);
            Toast.makeText(mContext, todoItems.get(i).getName(), Toast.LENGTH_SHORT).show();
        });
        // 有疑问，下面两行代码换过位置之后，从多的数据切换到少的数据的时候会出现越界错误
        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            TodoItem todoItem = todoItems.get(i);
            // 如果要把一个值置为默认值，需要使用setToDefault()方法
            if(isChecked){
                viewHolder.textView.setPaintFlags
                        (viewHolder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.textView.setAlpha(0.5f);
                todoItem.setChecked(true);
            }else{
                todoItem.setToDefault("checked");
                viewHolder.textView.setPaintFlags
                        (viewHolder.textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                viewHolder.textView.setAlpha(1);
            }
            todoItem.update(todoItem.getId());
        });
        viewHolder.checkBox.setChecked(todoItems.get(i).isChecked());
        viewHolder.textView.setTextColor(Color.BLACK);
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
