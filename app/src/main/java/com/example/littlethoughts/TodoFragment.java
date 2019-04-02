package com.example.littlethoughts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import static android.support.constraint.Constraints.TAG;

public class TodoFragment extends Fragment {

    RecyclerView todoRecyclerView;

    public TodoAdapter adapter;

    public LinearLayout addTodoLayout;

    List<TodoItem> todoItemList;

    private static TodoList todoList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo_layout, container, false);
        todoRecyclerView = view.findViewById(R.id.todo_recycler_view);
        Bundle bundle = getArguments();
        int listId;
        if (bundle != null){
            listId = bundle.getInt("list_id");
        }else {
            listId = 1;
        }
        getTodoItem(listId);
        adapter = new TodoAdapter(getContext(), todoItemList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        todoRecyclerView.setLayoutManager(manager);
        todoRecyclerView.setAdapter(adapter);
        addTodoLayout = new LinearLayout(getContext());
        EditText addTodoEdit = view.findViewById(R.id.add_todo_edit);
        Button addTodoBtn = view.findViewById(R.id.add_todo_btn);
        addTodoBtn.setOnClickListener(v -> {
            String str = addTodoEdit.getText().toString();
            TodoItem todoItem = new TodoItem();
            todoItem.setTodoList(getTodoList());
            todoItem.setName(str);
            todoItem.setChecked(false);
            todoItem.save();
            todoItemList.add(todoItem);
            adapter.notifyItemInserted(todoItemList.size() - 1);
            todoRecyclerView.scrollToPosition(todoItemList.size() - 1);
            addTodoEdit.setText("");
            addTodoLayout.setVisibility(View.GONE);
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getTodoItem(int listId){
        todoList = LitePal.find(TodoList.class, listId);
        todoItemList = LitePal.where("todolist_id = ?", String.valueOf(listId))
                .find(TodoItem.class);
    }

    public TodoList getTodoList() {
        return todoList;
    }
}
