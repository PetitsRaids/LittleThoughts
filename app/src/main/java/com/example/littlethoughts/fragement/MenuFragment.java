package com.example.littlethoughts.fragement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.littlethoughts.MainActivity;
import com.example.littlethoughts.R;
import com.example.littlethoughts.adapter.MenuExpandableAdapter;
import com.example.littlethoughts.db.ThoughtsList;
import com.example.littlethoughts.db.TodoList;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    private static List<List<String>> childList;

    private static MenuExpandableAdapter adapter;

    private ExpandableListView expandableListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_layout, container, false);
        expandableListView = view.findViewById(R.id.all_list);
        childList = new ArrayList<>();
        getChildList();
        adapter = new MenuExpandableAdapter(getContext(),
                new String[]{"待办事项", "小想法"}, childList);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.drawerLayout.closeDrawers();
                mainActivity.showHideFragment(groupPosition, childPosition);
            }
            return true;
        });
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean expand0 = sharedPreferences.getBoolean("expand0", false);
        boolean expand1 = sharedPreferences.getBoolean("expand1", false);
        if(expand0){
            expandableListView.expandGroup(0);
        }
        if(expand1){
            expandableListView.expandGroup(1);
        }
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

    public void editChild(int groupId, int childId, String name){
        childList.get(groupId).set(childId, name);
        adapter.notifyDataSetChanged();
    }

    public void removeChild(int groupId, int childId){
        childList.get(groupId).remove(childId);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editor.putBoolean("expand0", expandableListView.isGroupExpanded(0));
        editor.putBoolean("expand1", expandableListView.isGroupExpanded(1));
        editor.apply();
    }
}
