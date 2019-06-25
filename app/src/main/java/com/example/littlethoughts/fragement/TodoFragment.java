package com.example.littlethoughts.fragement;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.littlethoughts.MainActivity;
import com.example.littlethoughts.R;
import com.example.littlethoughts.TodoItemCallback;
import com.example.littlethoughts.adapter.TodoAdapter;
import com.example.littlethoughts.db.TodoItem;
import com.example.littlethoughts.db.TodoList;
import com.example.littlethoughts.utils.Logger;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class TodoFragment extends Fragment {

    private TodoAdapter adapter;

    private LinearLayout addTodoLayout;

    private EditText addTodoEdit;

    private MainActivity mainActivity;

    private List<TodoItem> todoItemList;

    private TodoList todoList;

    public int childId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo_layout, container, false);
        RecyclerView todoRecyclerView = view.findViewById(R.id.todo_recycler_view);
        todoItemList = new ArrayList<>();
        adapter = new TodoAdapter(getContext(), todoItemList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        todoRecyclerView.setLayoutManager(manager);
        todoRecyclerView.setAdapter(adapter);
        mainActivity = (MainActivity) getActivity();
        todoRecyclerView.setOnLongClickListener(l -> {
            Vibrator vibrator = (Vibrator) mainActivity.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(70);
            return true;
        });

        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(new TodoItemCallback(todoItemList, adapter));
        itemTouchHelper.attachToRecyclerView(todoRecyclerView);

        addTodoLayout = view.findViewById(R.id.add_todo_layout);
        addTodoEdit = view.findViewById(R.id.add_todo_edit);
        Button addTodoBtn = view.findViewById(R.id.add_todo_btn);
        addTodoBtn.setOnClickListener(v -> {
            String str = addTodoEdit.getText().toString();
            TodoItem todoItem = new TodoItem();
            todoItem.setTodoList(todoList);
            todoItem.setName(str);
            todoItem.setToDefault("checked");
            todoItem.setOrderId(adapter.getItemCount());
            todoItem.save();
            todoItemList.add(todoItem);
            adapter.notifyItemInserted(todoItemList.size() - 1);
            todoRecyclerView.scrollToPosition(todoItemList.size() - 1);
            addTodoEdit.setText("");
            addTodoLayout.setVisibility(View.GONE);
            mainActivity.getFloatingActionButton().show();
            mainActivity.inputMethodManager
                    .hideSoftInputFromWindow(addTodoEdit.getWindowToken(), 0);  //关闭键盘
            MainActivity.isInputing = 0;
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList(childId);
        Logger.d("TodoFragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d("TodoFragment onPause");
    }

    public void removeList() {
        new Thread(() -> {
            for (TodoItem todoItem : todoItemList) {
                todoItem.delete();
            }
            todoItemList.clear();
            todoList.delete();
            mainActivity.runOnUiThread(() -> adapter.notifyDataSetChanged());
        }).start();
    }

    public void refreshListName(String name) {
        todoList.setListName(name);
        todoList.update(todoList.getId());
    }

    private void refreshList(int childId) {
        LitePal.where("todolist_id = ?", String.valueOf(childId))
                .findAsync(TodoItem.class).listen(allItems -> {
            todoList = LitePal.find(TodoList.class, childId);
            todoItemList.clear();
            todoItemList.addAll(allItems);
            adapter.notifyDataSetChanged();
        });
    }

    public void hiddenAddLayout() {
        addTodoLayout.setVisibility(View.GONE);
    }

    public void addTodoItem() {
        addTodoLayout.setVisibility(View.VISIBLE);
        mainActivity.getInputMethodManager().toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        addTodoEdit.requestFocus();
    }
}
