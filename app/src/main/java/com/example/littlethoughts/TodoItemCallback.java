package com.example.littlethoughts;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.littlethoughts.adapter.TodoAdapter;
import com.example.littlethoughts.db.TodoItem;

import java.util.Collections;
import java.util.List;

public class TodoItemCallback extends ItemTouchHelper.Callback {

    private List<TodoItem> todoItemList;

    private TodoAdapter adapter;

    TodoItemCallback(List<TodoItem> todoItemList, TodoAdapter adapter){
        this.todoItemList = todoItemList;
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }else{
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.LEFT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = viewHolder1.getAdapterPosition();
        if(fromPosition < toPosition){
            for(int i = fromPosition; i < toPosition; i++){
                Collections.swap(todoItemList, i, i + 1);
            }
        }else{
            for(int i = fromPosition; i > toPosition; i--){
                Collections.swap(todoItemList, i, i - 1);
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //remove掉了之后，getAdapterPosition()返回的值就会变成-1
        int val = viewHolder.getAdapterPosition();
        todoItemList.remove(val);
        adapter.notifyItemRemoved(val);
        adapter.notifyItemRangeChanged(val, adapter.getItemCount());
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                viewHolder.itemView.setBackgroundColor(Color.rgb(188, 63, 60));
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(0);
    }
}
