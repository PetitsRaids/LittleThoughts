package com.example.littlethoughts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.littlethoughts.adapter.MenuExpandableAdapter;
import com.example.littlethoughts.db.ThoughtsList;
import com.example.littlethoughts.db.TodoList;

import org.litepal.LitePal;

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
        getChildList();
        MenuExpandableAdapter adapter = new MenuExpandableAdapter(getContext(),
                new String[]{"待办事项", "小想法"}, childList);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.drawerLayout.closeDrawers();
                TodoFragment todoFragment = new TodoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("list_id", groupPosition + 1);
                todoFragment.setArguments(bundle);
                mainActivity.replaceFragment(todoFragment);
            }
            return true;
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

     // 获取菜单页所有列表项
    private void getChildList() {
        List<String> list = new ArrayList<>();
        List<TodoList> todoItems = LitePal.findAll(TodoList.class);
        for (TodoList item : todoItems) {
            list.add(item.getListName());
        }
        childList.add(list);
        list = new ArrayList<>();
        List<ThoughtsList> thoughtsLists = LitePal.findAll(ThoughtsList.class);
        for (ThoughtsList item : thoughtsLists) {
            list.add(item.getThoughts());
        }
        childList.add(list);
    }

}
