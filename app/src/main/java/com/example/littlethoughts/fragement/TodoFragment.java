package com.example.littlethoughts.fragement;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.littlethoughts.MainActivity;
import com.example.littlethoughts.R;
import com.example.littlethoughts.TodoItemCallback;
import com.example.littlethoughts.adapter.TodoAdapter;
import com.example.littlethoughts.db.TodoItem;
import com.example.littlethoughts.db.TodoList;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TodoFragment extends Fragment {

    private TodoAdapter adapter;

    private LinearLayout addTodoLayout;

    public EditText addTodoEdit;

    private List<TodoItem> todoItemList;

    private static TodoList todoList;

    public int childId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshList(childId);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshList(childId);
        }
    }

    public void removeList() {
        new Thread(() -> {
            for (TodoItem todoItem : todoItemList) {
                todoItem.delete();
            }
            todoItemList.clear();
            todoList.delete();
        }).start();
        adapter.notifyDataSetChanged();
    }

    public void refreshListName(String name) {
        todoList.setListName(name);
        todoList.update(todoList.getId());
    }

    private void refreshList(int childId) {
        LitePal.where("todolist_id = ?", String.valueOf(childId))
                .findAsync(TodoItem.class).listen((allItems) -> {
            todoList = LitePal.find(TodoList.class, childId);
            todoItemList.clear();
            todoItemList.addAll(allItems);
            adapter.notifyDataSetChanged();
        });
    }

    private TodoList getTodoList() {
        return todoList;
    }

    public void showAddLayout() {
        addTodoLayout.setVisibility(View.VISIBLE);
    }

    public void unshowAddLayout() {
        addTodoLayout.setVisibility(View.GONE);
    }
}
