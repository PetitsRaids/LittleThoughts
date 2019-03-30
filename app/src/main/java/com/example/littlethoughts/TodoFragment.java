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

import com.example.littlethoughts.adapter.TodoAdapter;

public class TodoFragment extends Fragment {

    RecyclerView todoRecyclerView;

    TodoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo_layout, container, false);
        todoRecyclerView = view.findViewById(R.id.todo_recycler_view);
        adapter = new TodoAdapter(getContext(), new String[]{"1", "2", "3", "2", "3", "2", "3", "2", "3", "2", "3", "2", "3", "2", "3", "2", "3"});
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        todoRecyclerView.setLayoutManager(manager);
        todoRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
