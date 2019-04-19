package com.example.littlethoughts;

import android.graphics.Color;
import android.util.Log;

import com.example.littlethoughts.adapter.TodoAdapter;
import com.example.littlethoughts.db.TodoItem;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class TodoItemCallback extends ItemTouchHelper.Callback {

    private List<TodoItem> todoItemList;

    private TodoAdapter adapter;

    public TodoItemCallback(List<TodoItem> todoItemList, TodoAdapter adapter) {
        this.todoItemList = todoItemList;
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.LEFT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = viewHolder1.getAdapterPosition();
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(todoItemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(todoItemList, i, i - 1);
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        TodoItem todoItem1 = todoItemList.get(fromPos);
        TodoItem todoItem2 = todoItemList.get(toPos);
        int val1 = todoItem1.getOrderId();
        int val2 = todoItem2.getOrderId();
        Log.d("CALLBACK", "onMoved: val1 " + val1);
        Log.d("CALLBACK", "onMoved: val2 " + val2);
        Log.d("CALLBACK", "onMoved: fromPos " + fromPos);
        Log.d("CALLBACK", "onMoved: toPos " + toPos);
        if(fromPos == 0){
            todoItem2.setToDefault("orderId");
            todoItem2.update(todoItem2.getId());
        }else{
            todoItem2.setOrderId(fromPos);
            todoItem2.update(todoItem2.getId());
        }
        if(toPos == 0){
            todoItem1.setToDefault("orderId");
            todoItem1.update(todoItem1.getId());
        }else{
            todoItem1.setOrderId(toPos);
            todoItem1.update(todoItem1.getId());
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //remove掉了之后，getAdapterPosition()返回的值就会变成-1
        int val = viewHolder.getAdapterPosition();
        TodoItem todoItem = todoItemList.get(val);
        todoItemList.remove(val);
        adapter.notifyItemRemoved(val);
        adapter.notifyItemRangeChanged(val, adapter.getItemCount());
        todoItem.delete();
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                viewHolder.itemView.setBackgroundColor(Color.rgb(188, 63, 60));
            } else {
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
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
