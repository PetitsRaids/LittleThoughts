package com.example.littlethoughts;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.littlethoughts.adapter.TodoAdapter;
import com.example.littlethoughts.db.TodoItem;
import com.example.littlethoughts.db.TodoList;

import org.litepal.LitePal;

import java.util.List;

public class TodoFragment extends Fragment {

    private TodoAdapter adapter;

    public LinearLayout addTodoLayout;

    public EditText addTodoEdit;

    public List<TodoItem> todoItemList;

    private static TodoList todoList;

    public int childId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo_layout, container, false);
        RecyclerView todoRecyclerView = view.findViewById(R.id.todo_recycler_view);
        Log.d("Todo", "onCreateView");
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            childId = bundle.getInt("list_id");
//        } else {
//            childId = 1;
//        }
        todoItemList = getTodoItem(childId);
        adapter = new TodoAdapter(getContext(), todoItemList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        todoRecyclerView.setLayoutManager(manager);
        todoRecyclerView.setAdapter(adapter);
        MainActivity mainActivity = (MainActivity) getActivity();
        todoRecyclerView.setOnLongClickListener(l -> {
            Vibrator vibrator = (Vibrator) mainActivity.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(70);
            return true;
        });

        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(new TodoItemCallback(todoItemList, adapter));
        itemTouchHelper.attachToRecyclerView(todoRecyclerView);

        addTodoLayout = view.findViewById(R.id.add_todo_layout);
        addTodoLayout.setVisibility(View.GONE);
        addTodoEdit = view.findViewById(R.id.add_todo_edit);
        Button addTodoBtn = view.findViewById(R.id.add_todo_btn);
        addTodoBtn.setOnClickListener(v -> {
            String str = addTodoEdit.getText().toString();
            TodoItem todoItem = new TodoItem();
            todoItem.setTodoList(getTodoList());
            todoItem.setName(str);
            todoItem.setToDefault("checked");
            todoItem.setOrderId(adapter.getItemCount());
            todoItem.save();
            todoItemList.add(todoItem);
            adapter.notifyItemInserted(todoItemList.size() - 1);
            todoRecyclerView.scrollToPosition(todoItemList.size() - 1);
            addTodoEdit.setText("");
            addTodoLayout.setVisibility(View.GONE);
            mainActivity.floatingActionButton.show();
            mainActivity.inputMethodManager
                    .hideSoftInputFromWindow(addTodoEdit.getWindowToken(), 0);  //关闭键盘
            MainActivity.isInputing = 0;
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Todo", "onActivityCreated: ");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            todoItemList.clear();
            todoItemList.addAll(getTodoItem(childId));
            adapter.notifyDataSetChanged();
        }
        Log.d("Todo", "onHiddenChanged");
    }

    public List<TodoItem> getTodoItem(int childId) {
        todoList = LitePal.find(TodoList.class, childId);
        return LitePal.where("todolist_id = ?", String.valueOf(childId))
                .order("orderId").find(TodoItem.class);
    }

    public TodoList getTodoList() {
        return todoList;
    }
}
