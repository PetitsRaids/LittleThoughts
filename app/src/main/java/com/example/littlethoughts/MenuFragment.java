package com.example.littlethoughts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.littlethoughts.adapter.MenuExpandableAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    private List<List<String>> childList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_layout, container, false);
        ExpandableListView expandableListView = view.findViewById(R.id.all_list);
        childList = new ArrayList<>();
        getChildItem();
        MenuExpandableAdapter adapter = new MenuExpandableAdapter(getContext(),
                new String[]{"待办事项", "小想法"}, childList);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnGroupClickListener(((parent, v, groupPosition, id) -> {
            return false;
        }));
        expandableListView.setOnChildClickListener(((parent, v, groupPosition, childPosition, id) -> {
            Toast.makeText(getContext(), childList.get(groupPosition).get(childPosition), Toast.LENGTH_SHORT).show();
            return true;
        }));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getChildItem(){
        List<String> list = new ArrayList<>();
        list.add("哈哈哈");
        list.add("呵呵呵");
        childList.add(list);
        list = new ArrayList<>();
        list.add("追风筝的人");
        list.add("灿烂千阳");
        list.add("群山回唱");
        childList.add(list);
    }

    //    private Button todoButton, thoughtButton;
//
//    private ListView todoListView, thoughtListView;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.menu_layout, container, false);
//        todoButton = view.findViewById(R.id.todo_btn);
//        thoughtButton = view.findViewById(R.id.thought_btn);
//        todoListView = view.findViewById(R.id.todo_list_view);
//        thoughtListView = view.findViewById(R.id.thought_list_view);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, new String[]{"ds", "ds"});
//        todoListView.setAdapter(adapter);
//        todoListView.setVisibility(View.GONE);
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        todoButton.setOnClickListener((view) -> {
//            if (todoListView.getVisibility() == View.VISIBLE)
//                todoListView.setVisibility(View.GONE);
//            else {
//                todoListView.setVisibility(View.VISIBLE);
//            }
//        });
//    }
}
